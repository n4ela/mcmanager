package mcmanager.android.dao;

import mcmanager.android.exception.CoreException;
import mcmanager.kinopoisk.info.Movie;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "mcmanager";
    private final static String TABLE_NAME = "movie";
    private final static int VERSION = 1;
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " " + 
                                              "(_id INTEGER, " +
                                              "title TEXT, " +
                                              "originaltitle TEXT, " +
                                              "sorttitle TEXT, " +
                                              "_set TEXT, " +
                                              "rating TEXT, " +
                                              "year TEXT, " +
                                              "top250 TEXT, " +
                                              "votes TEXT, " +
                                              "outline TEXT, " +
                                              "plot TEXT, " +
                                              "tagline TEXT, " +
                                              "runtime TEXT, " +
                                              "mpaa TEXT, " +
                                              "playcount TEXT, " +
                                              "watched TEXT, " +
                                              "ext_id TEXT, " +
                                              "filenameandpath TEXT, " +
                                              "trailer TEXT, " +
                                              "genre TEXT, " +
                                              "credits TEXT, " +
                                              "artist TEXT, " +
                                              "director TEXT, " +
                                              "PRIMARY KEY(title, originaltitle, year))";
    
    
    public MovieHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(ActorToMovieHelper.CREATE_TABLE);
        db.execSQL(ActorHelper.CREATE_TABLE);
        db.execSQL(ThumbHelper.CREATE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        
    }
    
    private void save(Movie movie, SQLiteDatabase db) throws CoreException {
        //DONE
        //values.put("actor", movie.getActor());
        //TODO
        //values.put("fileinfo", movie.getFileinfo());
        //DONE
        //values.put("thumb", movie.getThumb());
        ContentValues content = createContentValue(movie);
        content.put("_id", SqlCommonHelper.getId(TABLE_NAME, db));
        long filmId = db.insert(TABLE_NAME, null, content);
        ActorHelper.saveOrUpdate(movie.getActor(), filmId, db);
        ThumbHelper.save(movie.getThumb(), filmId, db);
    }
    
    private void update(Movie movie, long id, SQLiteDatabase db) throws CoreException {
        Log.d("ID", String.valueOf(id));
        db.update(TABLE_NAME, createContentValue(movie), "_id = ?", new String[] {String.valueOf(id)});
        ActorHelper.saveOrUpdate(movie.getActor(), id, db);
    }
    
    private static final String SINGLE_SELECT_MOVIE = 
            "SELECT * FROM " + TABLE_NAME + " WHERE ";
    
    public synchronized void saveOrUpdate(Movie movie) throws CoreException {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put("title", movie.getTitle());
            values.put("originaltitle", movie.getOriginaltitle());
            values.put("year", movie.getYear());
            cursor = db.rawQuery(SINGLE_SELECT_MOVIE + SqlCommonHelper.createWhere(values), null);
            if (cursor.getCount() > 1) {
                throw new CoreException("Найдено более одной записи по запросу");
            }

            try {
                db.beginTransaction();
                if (cursor.getCount() == 0) {
                    save(movie, db);
                    Log.w("MY", "SAVE");
                } else {
                    cursor.moveToFirst();
                    update(movie, cursor.getLong(cursor.getColumnIndex("_id")), db);
                }
                Log.w("MY", "SUCCES");
                db.setTransactionSuccessful();
            } finally {
                Log.w("MY", "END");
                db.endTransaction();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
    
    private ContentValues createContentValue(Movie movie) {
        ContentValues values = new ContentValues();
        values.put("title", movie.getTitle());
        values.put("originaltitle", movie.getOriginaltitle());
        values.put("sorttitle", movie.getSorttitle());
        values.put("_set", movie.getSet());
        values.put("rating", movie.getRating());
        values.put("year", movie.getYear());
        values.put("top250", movie.getTop250());
        values.put("votes", movie.getVotes());
        values.put("outline", movie.getOutline());
        values.put("plot", movie.getPlot());
        values.put("tagline", movie.getTagline());
        values.put("runtime", movie.getRuntime());
        values.put("mpaa", movie.getMpaa());
        values.put("playcount", movie.getPlaycount());
        values.put("watched", movie.getWatched());
        values.put("ext_id", movie.getId());
        values.put("filenameandpath", movie.getFilenameandpath());
        values.put("trailer", movie.getTrailer());
        values.put("genre", movie.getGenre());
        values.put("credits", movie.getCredits());
        values.put("director", movie.getDirector());
        values.put("artist", movie.getArtist());
        return values;
    }
    
    
    
    
    public void loadAll() {
        
    }
    


}
