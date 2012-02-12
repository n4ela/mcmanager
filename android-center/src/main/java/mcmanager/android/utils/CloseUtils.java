package mcmanager.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mcmanager.android.dao.MovieHelper;

import android.database.Cursor;

public class CloseUtils {
    public static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void close(MovieHelper movieHelper) {
        if (movieHelper != null) {
            movieHelper.close();
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
    
}
