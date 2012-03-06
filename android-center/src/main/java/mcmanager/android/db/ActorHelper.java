package mcmanager.android.db;

import static mcmanager.android.utils.LogDb.log;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mcmanager.android.db.WhereQuery.Type;
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


    private final SQLiteDatabase db;

    public ActorHelper(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Сохранить или обновить информаци об актерах
     * @param actorList - список актеров фильма
     * @throws CoreException
     */
    public void saveOrUpdate(List<Actor> actorList, long filmId) throws CoreException {
        log.trace("Начало сохранение/обновление информации об актерах в фильме id: " + filmId);

        for (Actor actor : actorList) {
            if (!update(actor, filmId)) {
                save(actor, filmId);
            }
        }
        log.trace("Сохранение/обновление информации об актерах в фильме id: " + filmId + " успешно завершенно");

    }

    /**
     * Обновить информацию об актерее
     * @param actor    - информация об актере 
     * @param filmId   - id фильма
     * @return true - если обновление прошло, false - если обновление не прошло и требуется сохранение
     * @throws CoreException
     */
    private boolean update(Actor actor, long filmId) throws CoreException {
        log.trace("Начало обновления информация об актере: " + actor.getName() + " в фильме: " + filmId);
        Cursor cursor = null;
        Cursor serialCursor = null;
        boolean updateOk = false;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name = '" + actor.getName()  + "' AND _id = " +
                    "(SELECT actor_id FROM " + ActorToMovieHelper.TABLE_NAME + " WHERE movie_id = " + filmId + ")", new String[]{});
            if (cursor.moveToFirst()) {
                long actorId = cursor.getLong(cursor.getColumnIndex("_id"));
                ContentValues content = new ContentValues();
                content.put("_id", actorId);
                WhereQuery whereQuery = new WhereQuery(content, Type.AND);
                db.update(TABLE_NAME, createContentValue(actor), whereQuery.getWhere(), whereQuery.getArgs());
                updateOk = true;
            } else {
                WhereQuery whereQuery = new WhereQuery(createContentValue(actor), Type.AND);
                serialCursor = db.query(TABLE_NAME, null, whereQuery.getWhere(), whereQuery.getArgs(), null, null, null);
                if (serialCursor.moveToFirst()) {
                    new ActorToMovieHelper(db).save(serialCursor.getLong(serialCursor.getColumnIndex("_id")), filmId);
                    updateOk = true;
                }
            }
        } finally {
            CloseUtils.close(cursor);
            CloseUtils.close(serialCursor);
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
     * @throws CoreException
     */
    private void save(Actor actor, long filmId) throws CoreException {
        log.trace("Начало сохранение информация об актере: " + actor.getName() + " в фильме: " + filmId);
        ContentValues content = createContentValue(actor);
        content.put("cache_thumb", ImageUtils.loadImage(actor.getThumb(), ImageType.ACTOR, true));
        long actorId = Long.parseLong(SqlCommonHelper.getId(TABLE_NAME, db));
        content.put("_id", actorId);
        db.insert(TABLE_NAME, null, content);
        new ActorToMovieHelper(db).save(actorId, filmId);
        log.trace("Сохранена информация об актере: " + actor.getName() + " в фильме: " + filmId);
    }

    /**
     * Получить список актеров по ид фильму
     * @param filmId - ид фильма
     * @return список актеров
     */
    public Set<Actor> load(long filmId) {
        log.trace("Начало получени информации об актерах по id фильма: " + filmId);
        Set<Pair<String, Long>> argQuery = new LinkedHashSet<Pair<String, Long>>();
        for (Long actorId : new ActorToMovieHelper(db).load(filmId)) {
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
