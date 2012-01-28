package mcmanager.android.gui;

import mcmanager.android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoWidget extends LinearLayout {

    public InfoWidget(Context context, String title, String img) {
        super(context);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.info_widget, this);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(title);
        ImageView image = (ImageView) findViewById(R.id.imageViewPoster);
//        Bitmap bitmap = new BitmapFactory().decode
//        image.setImageBitmap(bm)
    }
}
