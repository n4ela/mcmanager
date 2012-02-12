package mcmanager.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {

    public static enum ImageType {ACTOR, THUMB};
    
    public static String loadImage(String url, ImageType type, boolean replace) {
        URL fileUrl = null;
        OutputStream os = null;
        File file = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            fileUrl = new URL(url);
            conn = (HttpURLConnection)fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            file = getCacheImage(url, type, replace);
            byte[] buffer = new byte[4096];
            os = new FileOutputStream(file);
            int b = -1;
            while ((b = is.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            LogDb.log.error("Ошибка при разборе url: " + url, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                }
            }
            CloseUtils.close(os);
        }
        return file == null ? null : file.getAbsolutePath();
    }

    public static File getCacheImage(String url, ImageType type, boolean replace) {
        File dir = null;
        File file = null;
        switch (type) {
            case ACTOR:
                dir = FileUtils.getCacheActorDir();
                break;
            default:
                dir = FileUtils.getCacheDir();
                break;
        }
        if (replace == false) {
            file = prepareFile(url, dir);
        } else {
            file = new FileName(dir, url).getPathFileName();
        }
        return file;
    }
    
    private static File prepareFile(String url, File cacheDir) {
        File file = null;
        FileName fileName = new FileName(cacheDir, url);
        do {
            file = fileName.getFileName();
        } while (file.exists());
        return file;
    }
    
    static class FileName {
        int count = 0;
        String name = "";
        String ext = "";
        File dir;
        
        FileName(File dir, String fileName) {
            fileName = fileName.lastIndexOf('/') == -1 ? fileName : fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());
            int index = fileName.lastIndexOf('.');
            name = fileName.substring(0, index);
            ext = fileName.substring(index + 1);
            this.dir = dir;
        }

        public File getPathFileName() {
            return new File(dir, name + "." + ext);
        }
        
        public File getFileName() {
            return new File(dir, count++ == 0 ? name + "." + ext : name + "_" + String.valueOf(count) + "." + ext); 
        }
    }


}
