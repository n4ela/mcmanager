package mcmanager.android.activity.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import mcmanager.android.R;
import mcmanager.android.activity.InfoMovie;
import mcmanager.android.activity.MediaCenterPlayer;
import mcmanager.android.bobj.EpisodeAndroid;
import mcmanager.android.utils.LogDb;
import mcmanager.kinopoisk.info.Thumb;

import java.util.List;

public class SerialListViewAdapter extends ArrayAdapter<EpisodeAndroid> {
    private final Activity context;
    private final List<EpisodeAndroid> serialString;

    public SerialListViewAdapter(List<EpisodeAndroid> serialString, Activity context) {
        super(context, R.layout.serial_list_view, serialString);
        this.context = context;
        this.serialString = serialString;
    }

    void putContent(EpisodeAndroid str) {
        serialString.add(str);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.serial_list_view, null, true);
        TextView textView = (TextView) rowView.findViewById(R.id.serialNumberText);
        ImageButton play = (ImageButton) rowView.findViewById(R.id.playSerial);
        final EpisodeAndroid episode = serialString.get(position);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MediaCenterPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString(MediaCenterPlayer.FILE_PATH, episode.getFilenameandpath());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        textView.setText("Сезон: " + episode.getSeason() + ", серия: " + episode.getEpisode());
        return rowView;
    }
}
