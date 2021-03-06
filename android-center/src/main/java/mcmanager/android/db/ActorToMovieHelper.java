package mcmanager.android.db;

import static mcmanager.android.utils.LogDb.log;

import java.util.LinkedHashSet;
import java.util.Set;

import mcmanager.android.db.WhereQuery.Type;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Вспомогательный класс для связи актер - фильма
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 * Date: 29.01.2012
 */
class ActorToMovieHelper {

    final static String TABLE_NAME = "actor2movie";

    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(actor_id INTEGER, " +
            "movie_id INTEGER, " +
            "PRIMARY KEY(actor_id, movie_id))";

    private final SQLiteDatabase db;
    public ActorToMovieHelper(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Сохрание информации об актере
     * @param actorId - ид актера
     * @param filmId  - ид фильма
     * @throws CoreException
     */
    public void save(long actorId, long filmId) throws CoreException {
        log.trace("Начало сохранение связки актер: " + actorId + " с фильмом: " + filmId);
        Cursor cursor = null;
        try {
            ContentValues content = createContentValue(actorId, filmId);
            WhereQuery where = new WhereQuery(content, Type.AND);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            if (cursor.getCount() == 0) {
                db.insert(TABLE_NAME, null, content);
                log.trace("Cохранение связки актер: " + actorId + " с фильмом: " + filmId + " успешно завершенно");
            } else {
                log.trace("Cохранение связки актер: " + actorId + " с фильмом: " + filmId + " не требуется " + cursor.getCount());
            }
        } finally {
            CloseUtils.close(cursor);
        }
        log.trace("Cохранение связки актер: " + actorId + " с фильмом: " + filmId + " успешно завершенно");
    }

    /**
     * Получить id актеров привязанного к фильму по ид  
     * @param filmId - ид фильма
     * @return список <ид фильма, ид актера> 
     */
    public Set<Long> load(long filmId) {
        log.trace("Начало получени id актеров по id фильма: " + filmId);
        Cursor cursor = null;
        try {
            ContentValues content = new ContentValues();
            content.put("movie_id", filmId);
            WhereQuery where = new WhereQuery(content, Type.AND);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            Set<Long> actorIds = new LinkedHashSet<Long>();
            while (cursor.moveToNext()) {
                long actorId = cursor.getLong(cursor.getColumnIndex("actor_id"));
                actorIds.add(actorId);
            }
            log.trace("Получени id актеров по id фильма: " + filmId + " успешно завершенно, " +
                    "полученно " + actorIds.size() + " записей");
            return actorIds;
        } finally {
            CloseUtils.close(cursor);
        }
    }

    /**
     * Построить контент для таблицы на основе:
     * @param actorId - ид актера
     * @param filmId  - ид фильма
     * @return контент для сохранения в бд
     */
    private ContentValues createContentValue(long actorId, long filmId) {
        ContentValues content = new ContentValues();
        content.put("actor_id", actorId);
        content.put("movie_id", filmId);
        return content;
    }
}