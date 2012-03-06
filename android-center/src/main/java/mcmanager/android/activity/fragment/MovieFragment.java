package mcmanager.android.activity.fragment;

import android.app.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import mcmanager.android.R;
import mcmanager.android.activity.gui.MovieInfoWidget;
import mcmanager.android.bobj.DataBaseObject;
import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.bobj.TVShowAndroid;
import mcmanager.android.db.HelperFactory;
import mcmanager.android.db.Loader;
import mcmanager.android.db.WhereQuery;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.LogDb;

public class MovieFragment extends Fragment {

    private View movieWidget;
    private Activity parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = getActivity();
        movieWidget = inflater.inflate(R.layout.main_screen, container, false);
        repaint();
        return movieWidget;
    }

    public void repaint() {
        final int width = parent.getWindowManager().getDefaultDisplay().getWidth() / 3;
        final int height = parent.getWindowManager().getDefaultDisplay().getHeight() / 3;
        final ProgressDialog progressDialog = new ProgressDialog(parent);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Обновления отображения");
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
                TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
                filmLayout.removeAllViews();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Loader loader = null;
                try {
                    //TODO тут должно быть более сложное условие на проверки класса
                    Class clazz = getTag().equals("movie") ? MovieAndroid.class : TVShowAndroid.class;
                    loader = new HelperFactory(getActivity()).getLoader(new WhereQuery(),clazz);
                    loader.open();
                    LogDb.log.info("SIZEKK: " + loader.getSize());
                    progressDialog.setMax((int) loader.getSize());
                    while (loader.hasNext()) {
                        final DataBaseObject movie = loader.next();
                        if (movie == null) break;
                        progressDialog.incrementProgressBy(1);
                        final LinearLayout widget = new MovieInfoWidget(movie, getTag(), width, height, parent);
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
                                TableRow tableRow = (TableRow) filmLayout.getChildAt(filmLayout.getChildCount() - 1);
                                if (tableRow == null || tableRow.getChildCount() == 3) {
                                    tableRow = new TableRow(getActivity());
                                    filmLayout.addView(tableRow);
                                }
                                tableRow.addView(widget);
                            }
                        });
                    }
                    progressDialog.dismiss();
                } finally {
                    CloseUtils.close(loader);
                }
            }
        }).start();

    }
}
