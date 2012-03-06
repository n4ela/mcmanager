package mcmanager.android.db;

import static mcmanager.android.utils.LogDb.log;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import mcmanager.android.db.WhereQuery.Type;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.ImageUtils;
import mcmanager.android.utils.ImageUtils.ImageType;
import mcmanager.android.utils.LogDb;
import mcmanager.kinopoisk.info.Thumb;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Вспомогательный класс для работы с информацие о постреах
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 * Date: 29.01.2012
 */
class ThumbHelper {
    private final static String TABLE_NAME = "thumb";
    
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (value TEXT, " +
            "colors TEXT, " +
            "dim TEXT, " +
            "preview TEXT, " +
            "season TEXT, " +
            "type TEXT, " +
            "id_film INTEGER, " +
            "cache_value TEXT, " +
            "PRIMARY KEY(value, id_film))";

    private final SQLiteDatabase db;
    public  ThumbHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public Set<Thumb> load(long filmId) {
        Cursor cursor = null;
        log.trace("Начало получения постеров по id фильма: " + filmId);
        try {
            ContentValues content = new ContentValues();
            content.put("id_film", filmId);
            WhereQuery where = new WhereQuery(content, Type.AND);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            Set<Thumb> thumbs = new LinkedHashSet<Thumb>();
            while (cursor.moveToNext()) {
                Thumb thumb = new Thumb();
                thumb.setValue(cursor.getString(cursor.getColumnIndex("cache_value")));
                thumb.setColors(cursor.getString(cursor.getColumnIndex("colors")));
                thumb.setDim(cursor.getString(cursor.getColumnIndex("dim")));
                thumb.setPreview(cursor.getString(cursor.getColumnIndex("preview")));
                thumb.setSeason(cursor.getString(cursor.getColumnIndex("season")));
                thumb.setType(cursor.getString(cursor.getColumnIndex("type")));
                thumbs.add(thumb);
            }
            log.trace("Получение постеров по id фильма: " + filmId + 
                      " завершенно, найдено постеров: " + thumbs.size());
            return thumbs;
        } finally {
            CloseUtils.close(cursor);
        }
        
    }
    
    /**
     * Сохранить или обновить информацию о постере к фильму
     * @param thumb  - постер к фильму
     * @param filmId - ид фильма
     */
    public void saveOrUpdate(Thumb thumb, long filmId) {
        ContentValues content = new ContentValues();
        content.put("value", thumb.getValue());
        content.put("id_film", filmId);
        WhereQuery where = new WhereQuery(content, Type.AND);
        log.trace("Начало сохранение/обновление о информации постере: " + content + " к фильму id: " + filmId);
        
        content = createContentValue(thumb, filmId);
        long result = db.update(TABLE_NAME, content, where.getWhere(), where.getArgs());
        if (result == 0) {
            log.trace("Сохранение информации о постере: " + content + " к фильму id: " + filmId + " прошло успешно");
            db.insert(TABLE_NAME, null, content);
        } else {
            log.trace("Обновление информации о постере: " + content + " к фильму id: " + filmId + " прошло успешно");
        }
    }
    
    /**
     * Построить контент для сохранения в бд
     * @param thumb  - сущность обложки фильма
     * @param filmId - ид фильма
     * @return контент для сохранения в бд
     */
    private ContentValues createContentValue(Thumb thumb, long filmId) {
        ContentValues content = new ContentValues();
        content.put("value", thumb.getValue());
        content.put("colors", thumb.getColors());
        content.put("dim", thumb.getDim());
        content.put("preview", thumb.getPreview());
        content.put("season", thumb.getSeason());
        content.put("type", thumb.getType());
        content.put("id_film", filmId);
        
        File cacheValue = ImageUtils.getCacheImage(thumb.getValue(), ImageType.THUMB, true);
        if (cacheValue.exists()) {
            LogDb.log.info("YES");
            content.put("cache_value", cacheValue.getAbsolutePath());
        } else {
            LogDb.log.info("NO");
            String strPath = ImageUtils.loadImage(thumb.getValue(), ImageType.THUMB, true);
            if (strPath != null) {
                content.put("cache_value", strPath);
            }
        }
        return content;
    }

}
