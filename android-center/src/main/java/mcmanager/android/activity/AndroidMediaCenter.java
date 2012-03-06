package mcmanager.android.activity;

import android.app.*;
import mcmanager.android.R;
import mcmanager.android.activity.fragment.MovieFragment;
import mcmanager.android.settings.Settings;
import mcmanager.android.utils.LogDb;
import mcmanager.android.utils.UpdateDB;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AndroidMediaCenter extends Activity {

    private static String TAG = "android-center";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Фильмы").setTabListener(
                new TabListener<MovieFragment>(this, "movie", MovieFragment.class)));
        actionBar.addTab(actionBar.newTab().setText("Сериалы").setTabListener(
                new TabListener<MovieFragment>(this, "serials", MovieFragment.class)));

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
//                                UpdateDB.reloadSerials(AndroidMediaCenter.this, item.getItemId() == R.id.rebase_hard);
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

}

