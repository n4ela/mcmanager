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
    
    public static File getCacheDir() {
        File cacheFile = new File(Environment.getExternalStorageDirectory(), "/mcmanager/cache/");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }
}
