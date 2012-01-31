package mcmanager.android.dao;

import static mcmanager.android.utils.LogDb.log;

import java.util.LinkedHashSet;
import java.util.Set;

import mcmanager.android.dao.WhereQuery.Type;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;
import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Вспомогательный класс для работы с сущностью Movie (Фильмы)
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 * Date: 29.01.2012
 */
public class MovieHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "mcmanager";
    private final static String TABLE_NAME = "movie";
    private final static int VERSION = 1;
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
                                              "PRIMARY KEY(title, originaltitle, year))";
    
    
    public MovieHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(ActorToMovieHelper.CREATE_TABLE);
        db.execSQL(ActorHelper.CREATE_TABLE);
        db.execSQL(ThumbHelper.CREATE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Сохранить или обновить информацию о фильме
     * @param movie - информация о фильме
     * @throws CoreException
     */
    public synchronized void saveOrUpdate(Movie movie) throws CoreException {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            
            ContentValues content = new ContentValues();
            content.put("title", movie.getTitle());
            content.put("originaltitle", movie.getOriginaltitle());
            content.put("year", movie.getYear());
            WhereQuery where = new WhereQuery(content, Type.AND);
            log.trace("Начало сохранение/обновление информации о фильме: " + content);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            try {
                db.beginTransaction();
                log.trace("По заданным параметрам найденно: " + cursor.getCount() + " записей");
                if (cursor.getCount() == 0) {
                    save(movie, db);
                } else {
                    cursor.moveToFirst();
                    long movieId = cursor.getLong(cursor.getColumnIndex("_id"));
                    update(movie, movieId, db);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
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
     * @param db    - база
     * @throws CoreException
     */
    private void save(Movie movie, SQLiteDatabase db) throws CoreException {
        log.trace("Начало сохранение информации о фильме: " + movie.getTitle());
        ContentValues content = createContentValue(movie);
        content.put("_id", SqlCommonHelper.getId(TABLE_NAME, db));
        long filmId = db.insert(TABLE_NAME, null, content);
        ActorHelper.saveOrUpdate(movie.getActor(), filmId, db);
        ThumbHelper.saveOrUpdate(movie.getThumb(), filmId, db);
        log.trace("Сохранение информации о фильме: " + movie.getTitle() + " успешно завершенно");
        //TODO
        //values.put("fileinfo", movie.getFileinfo());
    }
    
    /**
     * Обновить информацию о фильме
     * @param movie - информация о фильме
     * @param id    - ид фильма в базе
     * @param db    - база
     * @throws CoreException
     */
    private void update(Movie movie, long id, SQLiteDatabase db) throws CoreException {
        log.trace("Начало обновления информации о фильме: " + movie.getTitle() + " id: " + id);
        Log.d("ID", String.valueOf(id));
        db.update(TABLE_NAME, createContentValue(movie), "_id = ?", new String[] {String.valueOf(id)});
        ActorHelper.saveOrUpdate(movie.getActor(), id, db);
        log.trace("Начало обновления информации о фильме: " + movie.getTitle() + " id: " + id);
    }
    
    /**
     * Преобразовать контент для сохранения в базе
     * @param movie - информация о фильме
     * @return контент для сохранения в базе
     */
    private ContentValues createContentValue(Movie movie) {
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
        return values;
    }
    
    /**
     * Загрузить информацию о фильме по условию
     * @param where - условия запроса
     * @return 
     */
    public Set<Movie> load(WhereQuery where) {
        Set<Movie> movies = new LinkedHashSet<Movie>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            if (where == null) {
                where = new WhereQuery();
            }
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
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
                Set<Actor> actors = ActorHelper.load(Long.parseLong(id), db);
                if (actors != null) {
                    movie.getActor().addAll(actors);
                }
                Set<Thumb> thumbs = ThumbHelper.load(Long.parseLong(id), db);
                if (thumbs != null) {
                    movie.getThumb().addAll(thumbs);
                }
                movies.add(movie);
            }
            return movies;
        } finally {
            CloseUtils.close(cursor);
        }
    }



}
