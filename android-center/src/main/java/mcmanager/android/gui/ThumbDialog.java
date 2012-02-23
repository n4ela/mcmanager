package mcmanager.android.gui;

import java.util.List;

import org.apache.commons.io.input.ClosedInputStream;

import mcmanager.android.R;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.dao.MovieHelper;
import mcmanager.android.exception.CoreException;
import mcmanager.android.utils.CloseUtils;
import mcmanager.kinopoisk.info.Thumb;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ThumbDialog extends Dialog {

    private final Activity context; 
    private final int width;
    private final int height;
    private final MovieAndroid movie;
    
    public ThumbDialog(int width, int height,  Activity context, MovieAndroid movie) {
        super(context);
        this.context = context;
        this.movie = movie;
        this.width = width;
        this.height = height;
        setCanceledOnTouchOutside(true);
        setTitle("Обложки");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thumb_dialog);
        final ListView view = (ListView)findViewById(R.id.thumbListView);
        view.setAdapter(new ThumbAdapter(movie.getThumb(), width, height, context));
        LayoutParams params = getWindow().getAttributes(); 
        params.width = width; 
        params.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((LayoutParams) params);
        view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Thumb thumb = (Thumb) view.getItemAtPosition(position);
                movie.setActiveThumb(thumb.getValue());
                MovieHelper movieHelper = null;
                try {
                    movieHelper = new MovieHelper(context);
                    movieHelper.saveOrUpdate(movie);
                    dismiss();
                } catch (CoreException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.close(movieHelper);
                }
            }
        });
    }
        
}
