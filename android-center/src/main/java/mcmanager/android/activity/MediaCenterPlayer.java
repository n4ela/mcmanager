package mcmanager.android.activity;

import mcmanager.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MediaCenterPlayer extends Activity {

    public final static String FILE_PATH = "FILE_PATH"; 

    private VideoView mMediaPlayer;
    private Bundle extras;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.media_player);
        extras = getIntent().getExtras();
        mMediaPlayer = (VideoView) findViewById(R.id.surface_view);
        mMediaPlayer.setVideoPath(extras.getString(FILE_PATH));
        mMediaPlayer.setMediaController(new MediaController(this));
        mMediaPlayer.requestFocus();
        mMediaPlayer.start();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMediaPlayer.getDuration();
        mMediaPlayer.getCurrentPosition();
    }

}