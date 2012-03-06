package mcmanager.android.activity.gui;

import java.util.List;

import mcmanager.android.R;
import mcmanager.kinopoisk.info.Thumb;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ThumbAdapter extends ArrayAdapter<Thumb> {
    private final Activity context;
    private final List<Thumb> thumbs;
    private final int width;
    private final int height;

    public ThumbAdapter(List<Thumb> thumbs, int width , int height, Activity context) {
        super(context, R.layout.thumb_view, thumbs);
        this.context = context;
        this.thumbs = thumbs;
        this.width = width;
        this.height = height;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.thumb_view, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.thumbImage);
        Bitmap bitmap = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeFile(thumbs.get(position).getValue()), 
                            width, 
                            height, 
                            true);
        imageView.setImageBitmap(bitmap);
        return rowView;
    }
    
    
}

