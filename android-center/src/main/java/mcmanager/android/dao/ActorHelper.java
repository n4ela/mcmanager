package mcmanager.android.dao;

import static mcmanager.android.utils.LogDb.log;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mcmanager.android.dao.WhereQuery.Type;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.ImageUtils;
import mcmanager.android.utils.ImageUtils.ImageType;
import mcmanager.kinopoisk.info.Actor;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

/**
 * Вспомогательный класс для работы с сущность {@link Actor в БД}
 * @author Ivanov Dmitrij (ivanovdw@gmail.com)
 *
 * Date: 29.01.2012
 */
class ActorHelper {

    private final static String TABLE_NAME = "actor";

    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + 
            "(_id INTEGER, " +
            "name TEXT, " +
            "role TEXT, " +
            "thumb TEXT, " +
            "cache_thumb TEXT, " +
            "PRIMARY KEY(name, role, thumb))";

    /**
     * Сохранить или обновить информаци об актерах
     * @param actorList - список актеров фильма
     * @param filmId    - id фильма
     * @param db        - база
     * @throws CoreException
     */
    public synchronized static void saveOrUpdate(List<Actor> actorList, long filmId, SQLiteDatabase db) throws CoreException {
        log.trace("Начало сохранение/обновление информации об актерах в фильме id: " + filmId);
        Set<Long> actorIds = ActorToMovieHelper.load(filmId, db);
        for (Actor actor : actorList) {
            if (!update(actor, actorIds, filmId, db)) {
                save(actor, filmId, db);
            }
        }
        log.trace("Сохранение/обновление информации об актерах в фильме id: " + filmId + " успешно завершенно");

    }

    /**
     * Обновить информацию об актерее
     * @param actor    - информация об актере 
     * @param filmId   - id фильма
     * @param db       - база
     * @param actorIds - список актеров которые уже есть в базе
     * @return true - если обновление прошло, false - если обновление не прошло и требуется сохранение
     * @throws CoreException
     */
    private static boolean update(Actor actor, Set<Long> actorIds, long filmId, SQLiteDatabase db) throws CoreException {
        log.trace("Начало обновления информация об актере: " + actor.getName() + " в фильме: " + filmId);
        ContentValues content = new ContentValues();
        content.put("name", actor.getName());
        WhereQuery whereQuery = new WhereQuery(content, Type.AND);
        Cursor cursor = null;
        boolean updateOk = false;
        try {
            cursor = db.query(TABLE_NAME, null, whereQuery.getWhere(), whereQuery.getArgs(), null, null, null);
            while (cursor.moveToNext()) {
                long actorId = cursor.getLong(cursor.getColumnIndex("_id"));
                if (actorIds.contains(actorId)) {
                    content = new ContentValues();
                    content.put("_id", actorId);
                    whereQuery = new WhereQuery(content, Type.AND);
                    String thumbCache = cursor.getString(cursor.getColumnIndex("cache_thumb"));
                    ContentValues updateContent = createContentValue(actor);
                    if (thumbCache == null || !new File(thumbCache).exists()) {
                        content.put("cache_thumb", ImageUtils.loadImage(actor.getThumb(), ImageType.ACTOR, true));
                    } else {
                        content.put("cache_thumb", thumbCache);
                    }
                    db.update(TABLE_NAME, updateContent, whereQuery.getWhere(), whereQuery.getArgs());
                    ActorToMovieHelper.save(actorId, filmId, db);
                    log.trace("Обновлена информация об актере: " + actor.getName() + " в фильме: " + filmId);
                    updateOk = true;
                }
            }
        } finally {
            CloseUtils.close(cursor);
        }
        if (!updateOk) {
            log.trace("Обновление информация об актере: " + actor.getName() + " в фильме: " + filmId + " не требуется");
        }
        return updateOk;
    }

    /**
     * Сохранение информации об актере
     * @param actor  - информация об актере
     * @param filmId - id фильма
     * @param db     - база
     * @throws CoreException
     */
    private static void save(Actor actor, long filmId, SQLiteDatabase db) throws CoreException {
        log.trace("Начало сохранение информация об актере: " + actor.getName() + " в фильме: " + filmId);
        ContentValues content = createContentValue(actor);
        content.put("cache_thumb", ImageUtils.loadImage(actor.getThumb(), ImageType.ACTOR, true));
        content.put("_id", SqlCommonHelper.getId(TABLE_NAME, db));
        long actorId = db.insert(TABLE_NAME, null, content);
        ActorToMovieHelper.save(actorId, filmId, db);
        log.trace("Сохранена информация об актере: " + actor.getName() + " в фильме: " + filmId);
    }

    /**
     * Получить список актеров по ид фильму
     * @param filmdId - ид фильма
     * @param db      - база
     * @return список актеров
     */
    public synchronized static Set<Actor> load(long filmId, SQLiteDatabase db) {
        log.trace("Начало получени информации об актерах по id фильма: " + filmId);
        Set<Pair<String, Long>> argQuery = new LinkedHashSet<Pair<String, Long>>();
        for (Long actorId : ActorToMovieHelper.load(filmId, db)) {
            argQuery.add(Pair.create("_id", actorId));
        }

        Cursor cursor = null;
        try {
            WhereQuery where = new WhereQuery(argQuery, Type.OR);
            cursor = db.query(TABLE_NAME, null, where.getWhere(), where.getArgs(), null, null, null);
            Set<Actor> actors = new LinkedHashSet<Actor>();
            while(cursor.moveToNext()) {
                Actor actor = new Actor();
                actor.setName(cursor.getString(cursor.getColumnIndex("name")));
                actor.setRole(cursor.getString(cursor.getColumnIndex("role")));
                String thumbCache = cursor.getString(cursor.getColumnIndex("cache_thumb"));
                actor.setThumb(thumbCache);
                actors.add(actor);
            }
            log.trace("Получени информации об актерах по id фильма: " + filmId + 
                    " успешно завершенно, полученно " +  actors.size() + " записей");
            return actors;
        } finally {
            CloseUtils.close(cursor);
        }
    }

    /**
     * Построить контент на основе бина {@link Actor}
     * @param actor - информация об актере
     * @return контент для заполнениея бд
     */
    private static ContentValues createContentValue(Actor actor) {
        ContentValues content = new ContentValues();
        content.put("name", actor.getName());
        content.put("role", actor.getRole());
        content.put("thumb", actor.getThumb());
        return content;
    }

}
