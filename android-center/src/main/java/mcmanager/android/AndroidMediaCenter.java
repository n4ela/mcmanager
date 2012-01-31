package mcmanager.android;

import mcmanager.android.dao.MovieHelper;
import mcmanager.android.dao.WhereQuery;
import mcmanager.android.gui.InfoWidget;
import mcmanager.android.settings.Settings;
import mcmanager.android.utils.UpdateDB;
import mcmanager.kinopoisk.info.Movie;
import mcmanager.kinopoisk.info.Thumb;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

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
        try {
//            UpdateDB.reload(this);
            Button button = (Button) findViewById(R.id.button);
//            TableLayout filmLayout = (TableLayout) findViewById(R.id.tableLayoutFilm);
//            TableRow tableRow = new TableRow(AndroidMediaCenter.this);
            
//            int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
//            int height = getWindowManager().getDefaultDisplay().getHeight() / 3;
//            createViewMovie();
            Movie movie = new Movie();
            movie.setTitle("KLOPOLK");
            movie.setPlot("XCVBNM<lkjhgfcvbnm");
            Thumb thumb = new Thumb();
            thumb.setValue("http://st.kinopoisk.ru/images/poster/800253.jpg");
            movie.getThumb().add(thumb);
//            tableRow.addView(widget);
//            widget = new InfoWidget(movie, width, height, AndroidMediaCenter.this);
//            tableRow.addView(widget);
//            widget = new InfoWidget(movie, width, height, AndroidMediaCenter.this);
//            tableRow.addView(widget);
//            filmLayout.addView(tableRow);
//            tableRow = new TableRow(AndroidMediaCenter.this);
//            tableRow.addView(new Button(AndroidMediaCenter.this));
//            tableRow.addView(new Button(AndroidMediaCenter.this));
//            tableRow.addView(new Button(AndroidMediaCenter.this));
//            filmLayout.addView(tableRow);
            
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View paramView) {
                    createViewMovie();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void createViewMovie() {
        MovieHelper movieHelper = new MovieHelper(AndroidMediaCenter.this);
        int row = 0;
        int colum = 0;
        TableLayout filmLayout = (TableLayout) findViewById(R.id.tableLayoutFilm);
        TableRow tableRow = new TableRow(AndroidMediaCenter.this);
        for (Movie movie : movieHelper.load(new WhereQuery())) {
            int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
            int height = getWindowManager().getDefaultDisplay().getHeight() / 3;
            InfoWidget widget = new InfoWidget(movie, width, height, AndroidMediaCenter.this);
            tableRow.addView(widget);
            Log.e("COUNT: ", String.valueOf(tableRow.getChildCount()));
            row++;
            Log.d("GO", "GO");
            if (++colum == 3) {
                Log.d("ADD", "ADD");
                Log.e("COUNT: ", String.valueOf(tableRow.getChildCount()));
                filmLayout.addView(tableRow);
                tableRow = new TableRow(AndroidMediaCenter.this);
                colum = 0;
            }
            if (row == 6)
                break;
        }
    }
    
    //    private void parseFilm(File file) throws Exception {
//        Log.d("1", file.getAbsolutePath());
//        Serializer serializer = new Persister();
//        Movie movie = serializer.read(Movie.class, file);
//        MovieHelper movieHelper = null;
//        try {
//            movieHelper = new MovieHelper(AndroidMediaCenter.this);
//            movieHelper.saveOrUpdate(movie);
//            Set<Movie> myNewMovie = movieHelper.load(new WhereQuery());
//            for (Movie movies : myNewMovie) {
//                InfoWidget widget = new InfoWidget(AndroidMediaCenter.this, movies.getTitle(), movies.getPlot());
//                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_screen);
//                mainLayout.addView(widget);
//            }
//        } finally {
//            if (movieHelper != null) {
//                movieHelper.close();
//            }
//        }
//        
        
//        InfoWidget widget = new InfoWidget(AndroidMediaCenter.this, movie.getTitle());
//        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_screen);
//        Button button = new Button(AndroidMediaCenter.this);
//        mainLayout.addView(widget);
//    }
}

