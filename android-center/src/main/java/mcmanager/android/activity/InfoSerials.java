package mcmanager.android.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import mcmanager.android.R;
import mcmanager.android.activity.gui.SerialListViewAdapter;
import mcmanager.android.bobj.EpisodeAndroid;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.bobj.TVShowAndroid;
import mcmanager.android.db.HelperFactory;
import mcmanager.android.db.Loader;
import mcmanager.android.db.WhereQuery;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class InfoSerials extends Activity {

    public static final String MOVIE_TAG = "movie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        TVShowAndroid tvshow = (TVShowAndroid) bundle.getSerializable(MOVIE_TAG);
        setContentView(R.layout.info_serial);
        loadMovie(tvshow);
    }

    private void loadMovie(final TVShowAndroid tvshow) {
        ImageView thumb = (ImageView) findViewById(R.id.thumbSerial);
        Bitmap bitmap = BitmapFactory.decodeFile(tvshow.getActiveThumb());
        thumb.setImageBitmap(bitmap);
        TextView title = (TextView) findViewById(R.id.serialTitleText);
        title.setText(tvshow.getTitle());

        setText(findViewById(R.id.serialDirectorText), tvshow.getDirector());
        setText(findViewById(R.id.serialCreditsText), tvshow.getCredits());
        setText(findViewById(R.id.serialGenreText), tvshow.getGenre());
        setText(findViewById(R.id.serialYearText), tvshow.getYear());
        setText(findViewById(R.id.serialRatingText), tvshow.getRating());
        setText(findViewById(R.id.serialTaglineText), tvshow.getTagline());
        setText(findViewById(R.id.serialMPAAText), tvshow.getMpaa());
        setText(findViewById(R.id.serialPlotText), tvshow.getPlot());
        setText(findViewById(R.id.serialCountLabel), null);
        loadSerial(tvshow);
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

    private void loadSerial(TVShowAndroid tvshow) {
        Loader loader = null;
        try {
            ContentValues values = new ContentValues();
            values.put("id_serials", tvshow.getAndroidId());
            WhereQuery whereQuery = new WhereQuery(values, WhereQuery.Type.AND);
            loader = new HelperFactory(InfoSerials.this).getLoader(whereQuery, EpisodeAndroid.class);
            loader.open();
            final ListView listView = (ListView) findViewById(R.id.serialListView);
            final SerialListViewAdapter list = new SerialListViewAdapter(new ArrayList<EpisodeAndroid>(), this);
            listView.setAdapter(list);
            while (loader.hasNext()) {
                final EpisodeAndroid episode = (EpisodeAndroid) loader.next();
                list.add(episode);
            }
        } finally {
            CloseUtils.close(loader);
        }

    }
}
