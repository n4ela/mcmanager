package mcmanager.android.dao;

import java.util.List;

import mcmanager.android.dao.SqlCommonHelper.WhereQuery;
import mcmanager.android.exception.CoreException;
import mcmanager.kinopoisk.info.Actor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ActorHelper {

    private final static String TABLE_NAME = "actor";
    
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + 
                                              "(_id INTEGER, " +
                                              "name TEXT, " +
                                              "role TEXT, " +
                                              "thumb TEXT, " +
                                              "PRIMARY KEY(name, role, thumb))";
    
    public static synchronized void saveOrUpdate(List<Actor> actorList, long filmId, SQLiteDatabase db) throws CoreException {
        for (Actor actor : actorList) {
            saveOrUpdate(actor, filmId, db);
        }
    }
    
    private static void saveOrUpdate(Actor actor, long filmId, SQLiteDatabase db) throws CoreException {
        if (update(actor, filmId, db) == 0) {
            save(actor, filmId, db);
        }
    }
    
    private static long update(Actor actor, long filmId, SQLiteDatabase db) throws CoreException {
        WhereQuery whereQuery = new WhereQuery(createContentValue(actor));
        whereQuery.create();
        long result = db.update(TABLE_NAME, createContentValue(actor), whereQuery.getWhere(), whereQuery.getArgs());
        Log.d("Q", String.valueOf(result));
        if (result != 0) {
            ActorToMovieHelper.saveOrUpdate(result, filmId, db);
        }
        return result;
    }
    
    private static void save(Actor actor, long filmId, SQLiteDatabase db) throws CoreException {
        Log.e("SAVE ACTOR", SqlCommonHelper.getId(TABLE_NAME, db));
        ContentValues content = createContentValue(actor);
        content.put("_id", SqlCommonHelper.getId(TABLE_NAME, db));
        long actorId = db.insert(TABLE_NAME, null, content);
        ActorToMovieHelper.saveOrUpdate(actorId, filmId, db);
    }
    
    private static ContentValues createContentValue(Actor actor) {
        ContentValues content = new ContentValues();
        content.put("name", actor.getName());
        content.put("role", actor.getRole());
        content.put("thumb", actor.getThumb());
        return content;
    }

}
