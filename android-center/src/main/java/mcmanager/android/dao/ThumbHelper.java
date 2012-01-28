package mcmanager.android.dao;

import java.util.List;

import mcmanager.kinopoisk.info.Thumb;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ThumbHelper {
    private final static String TABLE_NAME = "thumb";
    
    public final static String CREATE_TABLE = "CREATE TABLE " + 
            "(value TEXT, " +
            "colors TEXT, " +
            "dim TEXT, " +
            "preview TEXT, " +
            "season TEXT, " +
            "type TEXT, " +
            "id_film INTEGER," +
            "PRIMARY KEY(value, id_film))";
    
    private static final String SELECT_THUMB = 
            "SELECT * FROM " + TABLE_NAME + " WHERE ";
    
    public synchronized static void save(List<Thumb> thumbs, long filmId, SQLiteDatabase db) {
        for (Thumb thumb : thumbs) {
            save(thumb, filmId, db);    
        }
    }
    
    private static void save(Thumb thumb, long filmId, SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SELECT_THUMB + SqlCommonHelper.createWhere(createContentValue(thumb, filmId)), null);
            if (cursor.getCount() == 0) {
                db.insert(TABLE_NAME, null, createContentValue(thumb, filmId));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    private static ContentValues createContentValue(Thumb thumb, long filmId) {
        ContentValues content = new ContentValues();
        content.put("value", thumb.getValue());
        content.put("colors", thumb.getColors());
        content.put("dim", thumb.getDim());
        content.put("preview", thumb.getPreview());
        content.put("season", thumb.getSeason());
        content.put("type", thumb.getType());
        content.put("id_film", filmId);
        return content;
    }

}
