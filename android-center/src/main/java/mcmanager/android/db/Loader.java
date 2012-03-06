package mcmanager.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mcmanager.android.bobj.DataBaseObject;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.StringUtils;
import mcmanager.kinopoisk.info.Actor;
import mcmanager.kinopoisk.info.Thumb;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Loader {

    private final String tableName;
    private final SQLiteDatabase db;
    private final WhereQuery where;

    protected Cursor cursor = null;

    public  Loader(WhereQuery where, String tableName, SQLiteDatabase db) {
        this.where = where == null ? new WhereQuery() : where;
        this.tableName = tableName;
        this.db = db;
    }

    public void open() {
        if (cursor == null) {
            cursor = db.query(tableName, null, where.getWhere(), where.getArgs(), null, null, null);
        }
    }

    public void close() {
        CloseUtils.close(cursor);
        CloseUtils.close(db);
    }

    public long getSize() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public boolean hasNext() {
        if (cursor == null) {
            throw new RuntimeException("cursos is null");
        }
        return cursor.moveToNext();
    }

    public DataBaseObject next() {
        return build();
    }

    protected abstract DataBaseObject build();
}
