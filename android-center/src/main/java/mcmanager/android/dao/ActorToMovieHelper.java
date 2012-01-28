package mcmanager.android.dao;

import mcmanager.android.exception.CoreException;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 */
public class ActorToMovieHelper {

    private final static String TABLE_NAME = "actor2movie";
    
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + 
                                               "(actor_id INTEGER, " +
                                               "movie_id INTEGER, " +
                                               "PRIMARY KEY(actor_id, movie_id))";
    
    private static final String SELECT_ACTOR_MOVIE = 
            "SELECT * FROM " + TABLE_NAME + " WHERE actor_id = ? AND movie_id = ?";
    
    public synchronized static void saveOrUpdate(long actorId, long filmId, SQLiteDatabase db) throws CoreException {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SELECT_ACTOR_MOVIE, new String[] {String.valueOf(actorId), 
                                                                   String.valueOf(filmId)});
            if (cursor.getCount() > 1) {
                throw new CoreException("Найдено более одной записи по запросу");
            }
            if (cursor.getCount() == 0) {
                save(actorId, filmId, db);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private static void save(long actorId, long filmId, SQLiteDatabase db) {
        db.insert(TABLE_NAME, null, createContentValue(actorId, filmId));
    }
    
    private static ContentValues createContentValue(long actorId, long filmId) {
        ContentValues content = new ContentValues();
        content.put("actor_id", actorId);
        content.put("movie_id", filmId);
        return content;
    }
}