package mcmanager.android;

import mcmanager.android.bobj.MovieAndroid;
import mcmanager.android.dao.MovieHelper;
import mcmanager.android.dao.MovieHelper.LoadMovie;
import mcmanager.android.dao.WhereQuery;
import mcmanager.android.gui.InfoWidget;
import mcmanager.android.settings.Settings;
import mcmanager.android.utils.CloseUtils;
import mcmanager.android.utils.LogDb;
import mcmanager.android.utils.UpdateDB;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AndroidMediaCenter extends Activity {

    private static String TAG = "android-center";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Фильмы").setTabListener(
                new TabListener<MovieFragment>(this, "simple", MovieFragment.class)));
        //        try {
        //            Button button = (Button) findViewById(R.id.button);
        //            button.setOnClickListener(new OnClickListener() {
        //                @Override
        //                public void onClick(View paramView) {
        //                    try {
        //                        UpdateDB.reload(AndroidMediaCenter.this);
        //                    } catch (Exception e) {
        //                        e.printStackTrace();
        //                    }
        //
        //                }
        //            });
        //            ScrollView parentScroll = (ScrollView)findViewById(R.id.mainScroll);
        //            parentScroll.setOnTouchListener(new View.OnTouchListener() {
        //                public boolean onTouch(View v, MotionEvent event) {
        //                    View view = findViewById(R.id.infoScroll); 
        //                    if (view != null) {
        //                        view.getParent().requestDisallowInterceptTouchEvent(false);
        //                    }
        //                    return false;
        //                }
        //            });
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        createViewMovie();
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.Settings:
                    Intent i = new Intent(this, Settings.class);
                    startActivity(i);
                    break;
                case R.id.rebase_hard:
                case R.id.rebase_soft:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UpdateDB.reload(AndroidMediaCenter.this, item.getItemId() == R.id.rebase_hard);
                            } catch (Exception e) {
                                LogDb.log.error("Критичная ошибка при обновление БД: ", e);
                            }

                        }
                    }).start();
                    break;
            }
        } catch (Throwable e) {
            LogDb.log.error("Критичная ошибка при обновление БД: ", e);
        }
        return true;
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            if (mFragment instanceof MovieFragment) {
                ((MovieFragment)mFragment).repaint();
            }
        }
    }


    public static class MovieFragment extends Fragment {

        private View movieWidget;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            movieWidget = inflater.inflate(R.layout.main_screen, container, false);
            repaint();
            return movieWidget;
        }

        public void repaint() {
            TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
            filmLayout.removeAllViews();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MovieHelper movieHelper = null;
                    try {
                        movieHelper = new MovieHelper(getActivity());
                        LoadMovie movies = movieHelper.load(new WhereQuery());
                        while (true) {
                            final MovieAndroid movie = movies.next();
                            if (movie == null) break;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Activity activity = getActivity();
                                    int width = activity.getWindowManager().getDefaultDisplay().getWidth() / 3;
                                    int height = activity.getWindowManager().getDefaultDisplay().getHeight() / 3;
                                    InfoWidget widget = new InfoWidget(movie, width, height, activity);
                                    TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
                                    synchronized (filmLayout) {
                                        Log.i("COUNT: ", String.valueOf(filmLayout.getChildCount()));
                                        TableRow tableRow = (TableRow) filmLayout.getChildAt(filmLayout.getChildCount() - 1);
                                        if (tableRow == null || tableRow.getChildCount() == 3) {
                                            tableRow = new TableRow(getActivity());
                                            filmLayout.addView(tableRow);
                                        }
                                        tableRow.addView(widget);
                                        filmLayout.postInvalidate();
                                    }
                                }
                            });
                        }
                    } finally {
                        CloseUtils.close(movieHelper);
                    }
                }
            }).start();
        }

        private class CreateViewMovie extends AsyncTask<MovieAndroid, Void, InfoWidget> {

            @Override
            protected InfoWidget doInBackground(MovieAndroid... movies) {
                for (MovieAndroid movie : movies) {
                    Activity activity = getActivity();
                    int width = activity.getWindowManager().getDefaultDisplay().getWidth() / 3;
                    int height = activity.getWindowManager().getDefaultDisplay().getHeight() / 3;
                    InfoWidget widget = new InfoWidget(movie, width, height, activity);
                    return widget;
                }
                return null;

            }

            protected void onPostExecute(InfoWidget result) {
                TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
                Log.i("COUNT: ", String.valueOf(filmLayout.getChildCount()));
                TableRow tableRow = (TableRow) filmLayout.getChildAt(filmLayout.getChildCount() - 1);
                if (tableRow == null || tableRow.getChildCount() == 3) {
                    tableRow = new TableRow(getActivity());
                    filmLayout.addView(tableRow);
                }
                tableRow.addView(result);
            }

        }

        //        public void createViewMovie() {
        //            MovieHelper movieHelper = null;
        //            try {
        //                Activity activity = getActivity();
        //                movieHelper = new MovieHelper(activity);
        //                int colum = 0;
        //                TableLayout filmLayout = (TableLayout) movieWidget.findViewById(R.id.tableLayoutFilm);
        //                filmLayout.removeAllViews();
        //                TableRow tableRow = new TableRow(activity);
        //                movieHelper = new MovieHelper(activity);
        //                LoadMovie movies = movieHelper.load(new WhereQuery());
        //                MovieAndroid movie = null;
        //                while ((movie = movies.next()) != null) {
        //                    int width = activity.getWindowManager().getDefaultDisplay().getWidth() / 3;
        //                    int height = activity.getWindowManager().getDefaultDisplay().getHeight() / 3;
        //                    InfoWidget widget = new InfoWidget(movie, width, height, activity);
        //                    tableRow.addView(widget);
        //                    if (++colum == 3) {
        //                        filmLayout.addView(tableRow);
        //                        tableRow = new TableRow(activity);
        //                        colum = 0;
        //                    }
        //                }
        //            } finally {
        //                CloseUtils.close(movieHelper);
        //            }
        //        }

    }
}

