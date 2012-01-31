package mcmanager.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlCommonHelper {

    public synchronized static String getId(String tableName, SQLiteDatabase db) {
        Cursor cursor = null;
        String id = null;
        try {
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
    
//    public static String createWhere(ContentValues args) {
//        StringBuilder builder = new StringBuilder("");
//        for (String key : args.keySet()) {
//            if (args.get(key) != null) {
//                builder.append(key + " = '" + args.getAsString(key) + "' AND ");
//            }
//        }
//        String result = builder.toString();
//        if (result.length() > 0) {
//            result = result.substring(0, result.length() - 5);
//        }
//        return result;
//    }
    
}
