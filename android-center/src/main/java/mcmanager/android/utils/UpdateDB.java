package mcmanager.android.utils;

import java.io.File;

import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.dao.MovieHelper;
import mcmanager.android.utils.FileUtils.FileName;
import mcmanager.kinopoisk.info.Movie;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;

public class UpdateDB {

    private final static String[] extension = new String[] {"avi", "mkv"};
    
    public static void reload(Context context, boolean hard) throws Exception {
        String filmPath = "/sdcard/xbmc/Films";
        File rootFileDir = new File(filmPath);
        if (filmPath.isEmpty() || !rootFileDir.exists()) { 
            throw new Exception("Не найден путь: " + filmPath);
        }
        MovieHelper movieHelper = null;
        try {
            movieHelper = new MovieHelper(context);
            for (File file : FileUtils.listFiles(rootFileDir, extension, true)) {
                File movieFile = new FileName(file.getAbsolutePath()).getPathFileName("nfo");
                Log.e("FILE", movieFile.getAbsolutePath());
                if (movieFile.exists()) {
                    LogDb.log.trace("Разбор файла: " + movieFile.getAbsolutePath());
                    Serializer serializer = new Persister();
                    MovieAndroid movie = serializer.read(MovieAndroid.class, movieFile);
                    movie.setFilenameandpath(file.getAbsolutePath());
                    if (!hard) {
                        movie.setUpdateTime(file.lastModified());
                    }
                    movieHelper.saveOrUpdate(movie);
                }
            }       
        } finally {
            CloseUtils.close(movieHelper);
        }
    }
}
