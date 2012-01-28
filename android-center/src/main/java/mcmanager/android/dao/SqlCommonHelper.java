package mcmanager.android.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public static String createWhere(ContentValues args) {
        StringBuilder builder = new StringBuilder("");
        for (String key : args.keySet()) {
            if (args.get(key) != null) {
                builder.append(key + " = '" + args.getAsString(key) + "' AND ");
            }
        }
        String result = builder.toString();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 5);
        }
        return result;
    }
    
    public static class WhereQuery {
        private String where;
        private String[] args;
        private ContentValues content;
        
        public WhereQuery(ContentValues content) {
            this.content = content;
        }
        
        public void create() {
            String tmpWhere = "";
            List<String> list = new ArrayList<String>();
            for (String key : content.keySet()) {
                if (content.get(key) != null) {
                    tmpWhere += key + " = ? AND ";
                    list.add(content.getAsString(key));
                }
            }
            args = list.toArray(new String[list.size()]);
            if (tmpWhere.length() > 0) {
                where = tmpWhere.substring(0, tmpWhere.length() - 5);
            }
        }

        public String getWhere() {
            return where;
        }

        public String[] getArgs() {
            return args;
        }
        
        
    }
    
}
