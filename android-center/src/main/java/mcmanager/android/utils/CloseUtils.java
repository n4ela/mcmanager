package mcmanager.android.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mcmanager.android.db.Loader;

public class CloseUtils {
    public static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void close(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(Loader loader) {
        if (loader != null) {
            loader.close();
        }
    }
}
