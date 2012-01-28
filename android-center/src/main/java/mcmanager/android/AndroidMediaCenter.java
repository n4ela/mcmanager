package mcmanager.android;

import java.io.File;

import mcmanager.android.dao.MovieHelper;
import mcmanager.android.settings.Settings;
import mcmanager.kinopoisk.info.Movie;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AndroidMediaCenter extends Activity {

    private static String TAG = "android-center";

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main_screen);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                try {
                    loadInfoFilm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                break;
        }
        return true;
    }

    private void loadInfoFilm() throws Exception {
        int COLUM = 3;
        int ROW = 3;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String filmPath = "/sdcard/android-test/Films";
        File rootFileDir = new File(filmPath);
        if (filmPath.isEmpty() || !rootFileDir.exists()) { 
            throw new Exception("Не найден путь: " + filmPath);
        }
        FileUtils fileUtils = new FileUtils();
        for (File file : FileUtils.listFiles(rootFileDir, new String[]{"nfo"}, true)) {
            parseFilm(file);
        }		
    }

    private void parseFilm(File file) throws Exception {
        Log.d("1", file.getAbsolutePath());
        Serializer serializer = new Persister();
        Movie movie = serializer.read(Movie.class, file);
        MovieHelper movieHelper = null;
        try {
            movieHelper = new MovieHelper(AndroidMediaCenter.this);
            movieHelper.saveOrUpdate(movie);
        } finally {
            if (movieHelper != null) {
                movieHelper.close();
            }
        }
        
        
//        InfoWidget widget = new InfoWidget(AndroidMediaCenter.this, movie.getTitle());
//        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_screen);
//        Button button = new Button(AndroidMediaCenter.this);
//        mainLayout.addView(widget);
    }
}

