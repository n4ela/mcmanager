package mcmanager.android.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.util.Pair;

public class WhereQuery {
    public enum Type {
        AND,
        OR
    }
    private String where;
    private String[] args;

    public WhereQuery() {
        
    }
    
    public WhereQuery(ContentValues content, Type type) {
        if (type == Type.AND) {
            createAnd(content);
        }
    }

    public WhereQuery(Set<Pair<String, Long>> content, Type type) {
        if (type == Type.OR) {
            createOr(content);
        }
    }

    private void createOr(Set<Pair<String,Long>> content) {
        String tmpWhere = "";
        List<String> list = new ArrayList<String>();
        for (Pair<String, Long> pair : content) {
            if (pair.second != null) {
                tmpWhere += pair.first + " = ? OR ";
                list.add(String.valueOf(pair.second));
            }
        }
        
        if (!list.isEmpty()) {
            args = list.toArray(new String[list.size()]);
        
        }
        where = !tmpWhere.isEmpty() ? tmpWhere.substring(0, tmpWhere.length() - 4) : null;
    }

    private void createAnd(ContentValues content) {
        String tmpWhere = "";
        List<String> list = new ArrayList<String>();
        for (String key : content.keySet()) {
            if (content.get(key) != null) {
                tmpWhere += key + " = ? AND ";
                list.add(content.getAsString(key));
            }
        }
        
        if (!list.isEmpty()) {
            args = list.toArray(new String[list.size()]);
        }
        where = !tmpWhere.isEmpty() ? tmpWhere.substring(0, tmpWhere.length() - 5) : null;
    }

    public String getWhere() {
        return where;
    }

    public String[] getArgs() {
        return args;
    }


}
