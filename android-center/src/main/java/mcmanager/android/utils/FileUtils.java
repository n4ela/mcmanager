package mcmanager.android.utils;

import java.io.File;

import android.os.Environment;

public class FileUtils {

    public static File getCacheActorDir() {
        File cacheFile = new File(Environment.getExternalStorageDirectory(), "/mcmanager/cache/actor/");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }
    
    public static File getCacheThumbDir() {
        File cacheFile = new File(Environment.getExternalStorageDirectory(), "/mcmanager/cache/thumb/");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }
    
    static class FileName {
        int count = 0;
        String name = "";
        String ext = "";
        String dir = "";
        
        FileName(String filePath) {
            String fileName = "";
            if (filePath.lastIndexOf('/') == -1) {
                fileName = filePath;
                dir = "";
            } else {
                fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
                dir = filePath.substring(0, filePath.lastIndexOf('/'));
            }
            int index = fileName.lastIndexOf('.');
            name = fileName.substring(0, index);
            ext = fileName.substring(index + 1);
        }

        public File getPathDir() {
            return new File(dir);
        }
        
        public File getPathFileName(File dir) {
            return new File(dir, name + "." + ext);
        }
        
        public File getPathFileName(String ext) {
            return new File(dir, name + "." + ext);
        }
        
        public File getPathFileName() {
            return new File(dir, name + "." + ext);
        }
        
        public File getFileName(File dir) {
            return new File(dir, count++ == 0 ? name + "." + ext : name + "_" + String.valueOf(count) + "." + ext); 
        }
        
        public File getFileName() {
            return new File(dir, count++ == 0 ? name + "." + ext : name + "_" + String.valueOf(count) + "." + ext); 
        }
        
    }
}
