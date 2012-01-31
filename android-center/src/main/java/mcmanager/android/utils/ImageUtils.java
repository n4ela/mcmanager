package mcmanager.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

public class ImageUtils {

    public static String loadImage(String url) {
        URL fileUrl = null;
        FileOutputStream byteArrayOutputStream = null;
        File file = null;
        try {
            fileUrl = new URL(url);
            HttpURLConnection conn= (HttpURLConnection)fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            file = prepareFile(fileUrl.getFile(), FileUtils.getCacheDir());
            int b = -1;
            byteArrayOutputStream = new FileOutputStream(file);
            while ((b = is.read()) != -1) {
                byteArrayOutputStream.write(b);
            }
        
        } catch (IOException e) {
            LogDb.log.error("Ошибка при разборе url: " + url, e);
        } finally {
            CloseUtils.close(byteArrayOutputStream);
        }
        return file == null ? null : file.getAbsolutePath();
    }

    private static File prepareFile(String url, File cacheDir) {
        class FileName {
            int count = 0;
            String name = "";
            String ext = "";
            FileName(String fileName) {
                int index = fileName.lastIndexOf('.');
                name = fileName.substring(0, index);
                ext = fileName.substring(index + 1);
            }

            public String getFileName() {
                return count++ == 0 ? name + "." + ext : name + "_" + String.valueOf(count) + "." + ext; 
            }
        }
        File file = null;
        FileName fileName = new FileName(url.substring(url.lastIndexOf('/') + 1, url.length()));
        do {
            file = new File(cacheDir, fileName.getFileName());
        } while (file.exists());
        return file;
    }


}
