package mcmanager.android.utils;

import java.io.File;

import mcmanager.android.dao.MovieHelper;
import mcmanager.kinopoisk.info.Movie;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;

public class UpdateDB {

    public static void reload(Context context) throws Exception {
        String filmPath = "/sdcard/android-test/Films";
        File rootFileDir = new File(filmPath);
        if (filmPath.isEmpty() || !rootFileDir.exists()) { 
            throw new Exception("Не найден путь: " + filmPath);
        }
        MovieHelper movieHelper = null;
        try {
            movieHelper = new MovieHelper(context);
            for (File file : FileUtils.listFiles(rootFileDir, new String[]{"nfo"}, true)) {
                LogDb.log.trace("Разбор файла: " + file.getAbsolutePath());
                Serializer serializer = new Persister();
                Movie movie = serializer.read(Movie.class, file);
                movieHelper.saveOrUpdate(movie);
            }       
        } finally {
            CloseUtils.close(movieHelper);
        }
    }
}
