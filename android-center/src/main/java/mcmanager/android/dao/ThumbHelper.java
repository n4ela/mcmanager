package mcmanager.android.dao;

import static mcmanager.android.utils.LogDb.log;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mcmanager.android.dao.WhereQuery.Type;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.ImageUtils;
import mcmanager.android.utils.ImageUtils.ImageType;
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
public class ThumbHelper {
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
    
//    /**
//     * Сохранить или обновить информацию о постере к фильму
//     * @param thumbs - список постеров к фильму
//     * @param filmId - ид фильма
//     * @param db     - база
//     */
//    public synchronized static void saveOrUpdate(List<Thumb> thumbs, long filmId, SQLiteDatabase db) {
//        log.trace("Начало сохранение/обновление информации о постерах к фильму id: " + filmId);
//        for (Thumb thumb : thumbs) {
//            saveOrUpdate(thumb, filmId, db);    
//        }
//        log.trace("Сохранение/обновление информации о постерах к фильму id: " + filmId + " прошло успешно");
//    }
    
    public synchronized static Set<Thumb> load(long filmId, SQLiteDatabase db) {
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
     * @param db     - база
     */    
    public static void saveOrUpdate(Thumb thumb, String cacheThumb, long filmId, SQLiteDatabase db) {
        ContentValues content = new ContentValues();
        content.put("value", thumb.getValue());
        content.put("id_film", filmId);
        WhereQuery where = new WhereQuery(content, Type.AND);
        log.trace("Начало сохранение/обновление о информации постере: " + content + " к фильму id: " + filmId);
        ContentValues contentUpdate = createContentValue(thumb, filmId);
        contentUpdate.put("cache_value", ImageUtils.getCacheImage(thumb.getValue(), ImageType.THUMB, true).getAbsolutePath());
        long result = db.update(TABLE_NAME, content, where.getWhere(), where.getArgs());
        if (result == 0) {
            log.trace("Сохранение информации о постере: " + content + " к фильму id: " + filmId + " прошло успешно");
            ContentValues contentInsert = createContentValue(thumb, filmId);
            contentInsert.put("cache_value", cacheThumb);
            db.insert(TABLE_NAME, null, contentInsert);
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
