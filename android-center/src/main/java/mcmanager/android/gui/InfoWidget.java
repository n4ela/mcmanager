package mcmanager.android.gui;

import mcmanager.android.R;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.utils.LogDb;
import mcmanager.android.utils.StringUtils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InfoWidget extends LinearLayout {

    private final int width;
    private final int height;

    public InfoWidget(final MovieAndroid movie, final int width, final int height, final Activity context) {
        super(context);
        this.width = width;
        this.height = height;

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.info_widget, this);

        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(movie.getTitle());

        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewDescription.setText(movie.getPlot());
        ScrollView childScroll = (ScrollView)findViewById(R.id.infoScroll);
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        
        ImageButton buttonInfo = (ImageButton) findViewById(R.id.imageButtonInfo);
        buttonInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoMovie.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(InfoMovie.MOVIE_TAG, movie);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        
        final ImageView image = (ImageView) findViewById(R.id.imageViewPoster);
        try {
            if (!StringUtils.isEmpty(movie.getActiveThumb())) {
                Bitmap bitmap = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(movie.getActiveThumb()), 
                        width / 2, 
                        height, 
                        true);
                image.setImageBitmap(bitmap);
                image.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View paramView) {
                        ThumbDialog dialog = 
                                new ThumbDialog(width, height * 2, context, movie);
                        dialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface paramDialogInterface) {
                                Bitmap bitmap = Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeFile(movie.getActiveThumb()), 
                                        width / 2, 
                                        height, 
                                        true);
                                image.setImageBitmap(bitmap);
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogDb.log.error("Ошибка при загрузки изображения: ", e);
        }
    }

    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutMain);
        layout.setLayoutParams(new LayoutParams(width, height));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
    
}
