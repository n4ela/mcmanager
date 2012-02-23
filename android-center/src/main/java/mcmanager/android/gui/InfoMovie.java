package mcmanager.android.gui;

import mcmanager.android.R;
import mcmanager.android.activity.MediaCenterPlayer;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.utils.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class InfoMovie extends Activity {

    public static final String MOVIE_TAG = "movie";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        MovieAndroid movie = (MovieAndroid) bundle.getSerializable(MOVIE_TAG);
        setContentView(R.layout.info_movie);
        loadMovie(movie);
    }
    
    private void loadMovie(final MovieAndroid movie) {
        ImageView thumb = (ImageView) findViewById(R.id.thumbMovie);
        Bitmap bitmap = BitmapFactory.decodeFile(movie.getActiveThumb());
        thumb.setImageBitmap(bitmap);
        TextView title = (TextView) findViewById(R.id.movieTitleText);
        title.setText(movie.getTitle());
        
        setText(findViewById(R.id.movieOriginalTitleText), movie.getOriginaltitle());
        setText(findViewById(R.id.movieDirectorText), movie.getDirector());
        setText(findViewById(R.id.movieCreditsText), movie.getCredits());
        setText(findViewById(R.id.movieGenreText), movie.getGenre());
        setText(findViewById(R.id.movieYearText), movie.getYear());
        setText(findViewById(R.id.movieRatingText), movie.getRating());
        setText(findViewById(R.id.movieTaglineText), movie.getTagline());
        setText(findViewById(R.id.movieMPAAText), movie.getMpaa());
        setText(findViewById(R.id.moviePlotText), movie.getPlot());
        
        Button play = (Button) findViewById(R.id.moviePlay);
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoMovie.this, MediaCenterPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString(MediaCenterPlayer.FILE_PATH, movie.getFilenameandpath());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    
    private void setText(View text, String value) {
        if (!StringUtils.isEmpty(value)) {
            if (text instanceof TextView) {
                TextView textView = (TextView)text;
                textView.setText(value);
            }
        } else {
            ((TableRow)text.getParent()).setVisibility(View.GONE);
        }
    }
}
