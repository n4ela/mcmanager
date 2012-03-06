package mcmanager.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mcmanager.android.utils.LogDb;

import java.util.Arrays;
import java.util.Collections;

class SqlCommonHelper {

    public synchronized static String getFilmId(SQLiteDatabase db) {
        long movieId = Long.parseLong(getId(MovieHelper.TABLE_NAME, db));
        long serialId  = Long.parseLong(getId(SerialsHelper.TABLE_NAME, db));
        long episodeId = Long.valueOf(getId(EpisodeHelper.TABLE_NAME, db));
        return String.valueOf(Collections.max(Arrays.asList(movieId, serialId, episodeId)));
    }
            
    public synchronized static String getId(String tableName, SQLiteDatabase db) {
        Cursor cursor = null;
        String id = null;
        try {
            LogDb.log.info("TABLE: " + "SELECT IFNULL(MAX(_id), 0) + 1 from " + tableName);
            cursor = db.rawQuery("SELECT IFNULL(MAX(_id), 0) + 1 from " + tableName, new String[]{});
            cursor.moveToFirst();
            id = cursor.getString(0);
            return id;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
