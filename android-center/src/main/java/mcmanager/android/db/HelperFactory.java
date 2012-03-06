package mcmanager.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mcmanager.android.bobj.DataBaseObject;
import mcmanager.android.bobj.EpisodeAndroid;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.bobj.TVShowAndroid;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;

import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HelperFactory extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "mcmanager";
    private final static int VERSION = 1;
    private final static Lock lock = new ReentrantLock();
    private final static Condition condition = lock.newCondition();

    public HelperFactory(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieHelper.CREATE_TABLE);
        db.execSQL(ActorToMovieHelper.CREATE_TABLE);
        db.execSQL(ActorHelper.CREATE_TABLE);
        db.execSQL(ThumbHelper.CREATE_TABLE);
        db.execSQL(SerialsHelper.CREATE_TABLE);
        db.execSQL(EpisodeHelper.CREATE_TABLE);
    }

    public Long saveOrUpdate(DataBaseObject object) {
        return saveOrUpdate(object, null);
    }

    public Long saveOrUpdate(DataBaseObject object, Long id) {
        synchronized (condition) {
            Long result = null;
            SQLiteDatabase db = null;
            try {
                db = getWritableDatabase();
                while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
                    condition.await();
                }
                db.beginTransaction();
                if (object instanceof MovieAndroid) {
                    MovieHelper helper = new MovieHelper(db);
                    helper.saveOrUpdate((MovieAndroid)object);
                } else if (object instanceof TVShowAndroid) {
                    SerialsHelper helper = new SerialsHelper(db);
                    result = helper.saveOrUpdate((TVShowAndroid) object);
                } else if (object instanceof EpisodeAndroid) {
                    EpisodeHelper helper = new EpisodeHelper(db);
                    helper.saveOrUpdate((EpisodeAndroid)object, id);
                }
                db.setTransactionSuccessful();
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                condition.notifyAll();
                if (db != null) {
                    db.endTransaction();
                }
                CloseUtils.close(db);
            }
            return result;
        }
    }

    public Loader getLoader(WhereQuery whereQuery, Class clazz) {
        synchronized (condition) {
            SQLiteDatabase db = null;
            try {
                db = getWritableDatabase();
                while (db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads()) {
                    condition.await();
                }
                Loader loader = null;
                if (clazz.equals(MovieAndroid.class)) {
                    loader = new MovieHelper(db).load(whereQuery);
                } else if (clazz.equals(TVShowAndroid.class)) {
                    loader = new SerialsHelper(db).load(whereQuery);
                } else if (clazz.equals(EpisodeAndroid.class)) {
                    loader = new EpisodeHelper(db).load(whereQuery);
                }
                return loader;
            } catch (Throwable e) {
                CloseUtils.close(db);
                throw new RuntimeException(e);
            } finally {
                condition.notifyAll();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
