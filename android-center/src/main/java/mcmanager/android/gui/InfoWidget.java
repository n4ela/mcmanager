package mcmanager.android.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import mcmanager.android.R;
import mcmanager.android.utils.LogDb;
import mcmanager.kinopoisk.info.Movie;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoWidget extends LinearLayout {

    private final int width;
    private final int height;

    public InfoWidget(Movie movie, int width, int height, Context context) {
        super(context);
        this.width = width;
        this.height = height;

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.info_widget, this);

        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(movie.getTitle());
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewDescription.setText(movie.getPlot());
        ImageView image = (ImageView) findViewById(R.id.imageViewPoster);
//        try {
            if (!movie.getThumb().isEmpty()) {
                Log.e("IMAGE: ", movie.getThumb().get(0).getValue());
                Bitmap bitmap = new BitmapFactory().decodeFile(movie.getThumb().get(0).getValue());
                image.setImageBitmap(bitmap);
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            LogDb.log.error("Ошибка при загрузки изображения: ", e);
//        }
    }

    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutMain);
        layout.setLayoutParams(new LayoutParams(width, height));
        ImageView image = (ImageView) findViewById(R.id.imageViewPoster);
        Log.d("h:", String.valueOf(image.getHeight()));
        Log.d("w:", String.valueOf(image.getWidth()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
