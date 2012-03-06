package mcmanager.android.db;

import static mcmanager.android.utils.LogDb.log;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import mcmanager.android.bobj.DataBaseObject;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.db.WhereQuery.Type;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.LogDb;
import mcmanager.android.utils.StringUtils;
import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Thumb;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Вспомогательный класс для работы с сущностью Movie (Фильмы)
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 * Date: 29.01.2012
 */
class MovieHelper {

    final static String TABLE_NAME = "movie";

    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " " +
            "(_id INTEGER, " +
            "title TEXT, " +
            "originaltitle TEXT, " +
            "sorttitle TEXT, " +
            "_set TEXT, " +
            "rating TEXT, " +
            "year TEXT, " +
            "top250 TEXT, " +
            "votes TEXT, " +
            "outline TEXT, " +
            "plot TEXT, " +
            "tagline TEXT, " +
            "runtime TEXT, " +
            "mpaa TEXT, " +
            "playcount TEXT, " +
            "watched TEXT, " +
            "ext_id TEXT, " +
            "filenameandpath TEXT, " +
            "trailer TEXT, " +
            "genre TEXT, " +
            "credits TEXT, " +
            "artist TEXT, " +
            "director TEXT, " +
            "active_thumb TEXT, " +
            "update_time INTEGER, " +
            "view boolean, " +
            "PRIMARY KEY(title, originaltitle, year))";

    private final SQLiteDatabase db;

    public MovieHelper(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Сохранить или обновить информацию о фильме
     * @param movie - информация о фильме
     * @throws CoreException
     */
    public void saveOrUpdate(MovieAndroid movie) throws CoreException {
        Cursor cursor = null;
        try {
            ContentValues content = new ContentValues();
            content.put("title", movie.getTitle());
            content.put("originaltitle", movie.getOriginaltitle());
            content.put("year", movie.getYear());
            WhereQuery where = new WhereQuery(content, Type.AND);
            log.trace("Начало сохранение/обновление информации о фильме: " + content);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            log.trace("По заданным параметрам найденно: " + cursor.getCount() + " записей");
            if (cursor.getCount() == 0) {
                save(movie);
            } else {
                cursor.moveToFirst();
                long movieId = cursor.getLong(cursor.getColumnIndex("_id"));
                Long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                if (movie.getUpdateTime() == null || updateTime < movie.getUpdateTime()) {
                    update(movie, movieId);
                }
            }
            log.trace("Cохранение/обновление информации о фильме: " + content + " успешно завершенно");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    /**
     * Сохранить информацию о фильме
     * @param movie - информация о фильме
     * @throws CoreException
     */
    private void save(MovieAndroid movie) throws CoreException {
        log.trace("Начало сохранение информации о фильме: " + movie.getTitle());

        ContentValues content = createContentValue(movie);

        long filmId = Long.parseLong(SqlCommonHelper.getFilmId(db));
        content.put("_id", filmId);

        db.insert(TABLE_NAME, null, content);

        new ActorHelper(db).saveOrUpdate(movie.getActor(), filmId);

        for (Thumb thumb : movie.getThumb()) {
            new ThumbHelper(db).saveOrUpdate(thumb, filmId);
        }
        log.trace("Сохранение информации о фильме: " + movie.getTitle() + " успешно завершенно");
        //TODO
        //values.put("fileinfo", movie.getFileinfo());
    }

    /**
     * Обновить информацию о фильме
     * @param movie - информация о фильме
     * @param id    - ид фильма в базе
     * @throws CoreException
     */
    private void update(MovieAndroid movie, long id) throws CoreException {
        log.trace("Начало обновления информации о фильме: " + movie.getTitle() + " id: " + id);
        ContentValues values = createContentValue(movie);
        db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});
        new ActorHelper(db).saveOrUpdate(movie.getActor(), id);
        for (Thumb thumb : movie.getThumb()) {
            new ThumbHelper(db).saveOrUpdate(thumb, id);
        }
        log.trace("Начало обновления информации о фильме: " + movie.getTitle() + " id: " + id);
    }

    /**
     * Преобразовать контент для сохранения в базе
     * @param movie - информация о фильме
     * @return контент для сохранения в базе
     */
    private ContentValues createContentValue(MovieAndroid movie) {
        ContentValues values = new ContentValues();
        values.put("title", movie.getTitle());
        values.put("originaltitle", movie.getOriginaltitle());
        values.put("sorttitle", movie.getSorttitle());
        values.put("_set", movie.getSet());
        values.put("rating", movie.getRating());
        values.put("year", movie.getYear());
        values.put("top250", movie.getTop250());
        values.put("votes", movie.getVotes());
        values.put("outline", movie.getOutline());
        values.put("plot", movie.getPlot());
        values.put("tagline", movie.getTagline());
        values.put("runtime", movie.getRuntime());
        values.put("mpaa", movie.getMpaa());
        values.put("playcount", movie.getPlaycount());
        values.put("watched", movie.getWatched());
        values.put("ext_id", movie.getId());
        values.put("filenameandpath", movie.getFilenameandpath());
        values.put("trailer", movie.getTrailer());
        values.put("genre", movie.getGenre());
        values.put("credits", movie.getCredits());
        values.put("director", movie.getDirector());
        values.put("artist", movie.getArtist());
        values.put("update_time", new Date().getTime());
        if (movie.getActiveThumb() != null) {
            values.put("active_thumb", movie.getActiveThumb());
        }
        return values;
    }

    /**
     * Загрузить информацию о фильме по условию
     * @param where - условия запроса
     * @return
     */
    public Loader load(WhereQuery where) {
        return new Loader(where, TABLE_NAME, db) {
            @Override
            protected DataBaseObject build() {
                MovieAndroid movie = new MovieAndroid();
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                movie.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                movie.setOriginaltitle(cursor.getString(cursor.getColumnIndex("originaltitle")));
                movie.setSorttitle(cursor.getString(cursor.getColumnIndex("sorttitle")));
                movie.setSet(cursor.getString(cursor.getColumnIndex("_set")));
                movie.setRating(cursor.getString(cursor.getColumnIndex("rating")));
                movie.setYear(cursor.getString(cursor.getColumnIndex("year")));
                movie.setTop250(cursor.getString(cursor.getColumnIndex("top250")));
                movie.setVotes(cursor.getString(cursor.getColumnIndex("votes")));
                movie.setOutline(cursor.getString(cursor.getColumnIndex("outline")));
                movie.setPlot(cursor.getString(cursor.getColumnIndex("plot")));
                movie.setTagline(cursor.getString(cursor.getColumnIndex("tagline")));
                movie.setRuntime(cursor.getString(cursor.getColumnIndex("runtime")));
                movie.setMpaa(cursor.getString(cursor.getColumnIndex("mpaa")));
                movie.setPlaycount(cursor.getString(cursor.getColumnIndex("playcount")));
                movie.setWatched(cursor.getString(cursor.getColumnIndex("watched")));
                movie.setId(cursor.getString(cursor.getColumnIndex("ext_id")));
                movie.setFilenameandpath(cursor.getString(cursor.getColumnIndex("filenameandpath")));
                movie.setTrailer(cursor.getString(cursor.getColumnIndex("trailer")));
                movie.setGenre(cursor.getString(cursor.getColumnIndex("genre")));
                movie.setCredits(cursor.getString(cursor.getColumnIndex("credits")));
                movie.setDirector(cursor.getString(cursor.getColumnIndex("director")));
                movie.setArtist(cursor.getString(cursor.getColumnIndex("artist")));

                Set<Actor> actors = new ActorHelper(db).load(Long.parseLong(id));
                movie.getActor().addAll(actors);

                Set<Thumb> thumbs = new ThumbHelper(db).load(Long.parseLong(id));
                if (thumbs != null) {
                    movie.getThumb().addAll(thumbs);
                }

                String activeThumb = cursor.getString(cursor.getColumnIndex("active_thumb"));
                if (!StringUtils.isEmpty(activeThumb)) {
                    movie.setActiveThumb(activeThumb);
                } else if (!movie.getThumb().isEmpty()) {
                    movie.setActiveThumb(movie.getThumb().get(0).getValue());
                }
                return movie;
            }
        };
    }

}
