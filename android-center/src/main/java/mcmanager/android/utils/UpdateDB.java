package mcmanager.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mcmanager.android.bobj.EpisodeAndroid;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.bobj.TVShowAndroid;
import mcmanager.android.db.HelperFactory;
import mcmanager.android.db.SerialsHelper;
import mcmanager.android.utils.FileUtils.FileName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;

public class UpdateDB {

    private final static String[] extension = new String[] {"avi", "mkv"};

    public static void reload(Context context, boolean hard) throws Exception {
        NotificationHelper helper = new NotificationHelper(context);
        helper.createNotification(100);
        String filmPath = "/sdcard/xbmc/Films";
        File rootFileDir = new File(filmPath);
        if (filmPath.isEmpty() || !rootFileDir.exists()) {
            throw new Exception("Не найден путь: " + filmPath);
        }

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
                helper.progressUpdate();
                new HelperFactory(context).saveOrUpdate(movie);
            }
        }
        helper.completed();
    }

    public static void reloadSerials(Context context, boolean hard) throws Exception {
        String filmPath = "/sdcard/xbmc/Serials";
        File rootFileDir = new File(filmPath);
        if (filmPath.isEmpty() || !rootFileDir.exists()) {
            throw new Exception("Не найден путь: " + filmPath);
        }

        LogDb.log.info("START");
        for (File file : FileUtils.listFiles(rootFileDir, new NameFileFilter("tvshow.nfo"), TrueFileFilter.INSTANCE)) {
            LogDb.log.info(file);
            Serializer serializer = new Persister();
            try {
            TVShowAndroid movie = serializer.read(TVShowAndroid.class, file);
            if (!hard) {
                movie.setUpdateTime(file.lastModified());
            }
            long serialId = new HelperFactory(context).saveOrUpdate(movie);
            for (File episode : FileUtils.listFiles(file.getParentFile(), extension, true)) {
                LogDb.log.info("EPISODE: " + episode.getAbsolutePath());
                File episodeFile = new FileName(episode.getAbsolutePath()).getPathFileName("nfo");
                if (episodeFile.exists()) {
                    Serializer serializerEpisode = new Persister();
                    EpisodeAndroid episodeAndroid = serializerEpisode.read(EpisodeAndroid.class, episodeFile);
                    episodeAndroid.setFilenameandpath(episodeFile.getAbsolutePath());
                    Pattern seasonRegexp = Pattern.compile("\\.[sS]([0-9]*)[eE]([0-9]*)");
                    LogDb.log.info("episodeFile.getName(): " + episodeFile.getName());
                    Matcher matcher = seasonRegexp.matcher(episodeFile.getName());
                    if (matcher.find() && matcher.groupCount() == 2) {
                        episodeAndroid.setSeason(matcher.group(1));
                        episodeAndroid.setEpisode(matcher.group(2));
                        new HelperFactory(context).saveOrUpdate(episodeAndroid, serialId);
                    } else {
                        LogDb.log.error("Из имени файла: " + episodeFile +
                                " не удалось получить номер сезона и номер серии");
                    }
                    
                }
            }
            } catch (FileNotFoundException e) {
            }

        }

    }
}
