package com.natercio.myhome;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.WallpaperManager;
import android.content.*;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 7/21/13
 * Time: 8:22 PM
 */

public class Launcher extends FragmentActivity {

    // PRIVATE ATTRIBUTES

    private ActionBar actionBar;

    private ViewPager appsViewPager;
    private AppsDrawerPagerAdapter appsDrawerPagerAdapter;

    private ArrayList<Fragment> pagerFragments;

    //Preferences
    private SharedPreferences preferences;

    private int homeScreensCount = 2;
    private int defaultHomeScreen = homeScreensCount - 1;

    ////////////////////
    // PUBLIC METHODS //
    ////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.home_action_android_settings:
                intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.home_action_launcher_settings:
                appsViewPager.setCurrentItem(defaultHomeScreen + 2);
                return true;

            default:
                return false;
        }
    }

    public void setHomeScreensCount(int homeScreensCount) {
        if (pagerFragments == null) {
            pagerFragments = new ArrayList<>(homeScreensCount + 2);
            pagerFragments.add(new SmartAppsDrawerFragment());
            pagerFragments.add(new PreferencesFragment());
        }

        if (pagerFragments.size() - 2 > homeScreensCount) {
            //remove homescreens
            while (pagerFragments.size() - 2 > homeScreensCount)
                pagerFragments.remove(0);

        } else if (pagerFragments.size() - 2 < homeScreensCount) {
            //add homescreens
            while (pagerFragments.size() - 2 < homeScreensCount)
                pagerFragments.add(0, new WidgetDrawerFragment());
        }

        this.homeScreensCount = homeScreensCount;
        this.defaultHomeScreen = homeScreensCount - 1;

        appsDrawerPagerAdapter = new AppsDrawerPagerAdapter(getFragmentManager());
        appsViewPager.setAdapter(appsDrawerPagerAdapter);
    }


    ///////////////////////
    // PROTECTED METHODS //
    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        setupPreferences();

        setupViewPager();

        setupActionBar();

        setupWallpaper();

        setupIntentRecievers();
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    private void setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        homeScreensCount = preferences.getInt(PreferencesFragment.KEY_PREF_HOME_SCREENS, 2);
    }

    private void setupViewPager() {
        appsViewPager = (ViewPager) findViewById(R.id.home_root);

        setHomeScreensCount(homeScreensCount);

        appsViewPager.setDrawingCacheEnabled(true);
        appsViewPager.setOffscreenPageLimit(2);
        appsViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (appsViewPager.getCurrentItem() <= homeScreensCount -1 && actionBar.isShowing() || preferences.getBoolean(PreferencesFragment.KEY_PREF_HIDE_ACT_BAR, false))
                            actionBar.hide();
                        break;

                    case ViewPager.SCROLL_STATE_SETTLING:
                        if (appsViewPager.getCurrentItem() > homeScreensCount -1 && !preferences.getBoolean(PreferencesFragment.KEY_PREF_HIDE_ACT_BAR, false))
                            actionBar.show();
                        break;

                }

            }
        });
    }

    private void setupActionBar() {
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        actionBar.hide();
        /*
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowHomeEnabled(false);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                appsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            }
        };

        //Add "Smart" tab
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_smart).setTabListener(tabListener));

        //Add "Widgets" tab
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_widgets).setTabListener(tabListener));
        */
    }

    private void setupWallpaper() {
        try {
            WallpaperManager wallpaperManager = (WallpaperManager) this.getSystemService(Context.WALLPAPER_SERVICE);
            wallpaperManager.setResource(R.drawable.default_wallpaper);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Drawable wallpaper = peekWallpaper();
        if (wallpaper == null) {
            try {
                clearWallpaper();
            } catch (IOException e) {
                Log.e(this.getClass().getName(), "Failed to clear wallpaper " + e);
            }
        } else {
            getWindow().setBackgroundDrawable(new ClippedDrawable(wallpaper));
        }
    }

    private void setupIntentRecievers() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(new PackageActionsReceiver(), filter);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            boolean alreadyOnHome = ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

            if (alreadyOnHome)
                appsViewPager.setCurrentItem(defaultHomeScreen);

        }

    }

    @Override
    public void onBackPressed() {
        if (appsViewPager.getCurrentItem() > defaultHomeScreen)
            appsViewPager.setCurrentItem(appsViewPager.getCurrentItem()-1);
    }


    /////////////////////
    // PRIVATE CLASSES //
    /////////////////////

    private final class AppsDrawerPagerAdapter extends FragmentStatePagerAdapter {

        public AppsDrawerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return pagerFragments.get(i);
        }

        @Override
        public int getCount() {
            return pagerFragments.size();
        }
    }

    private final class PackageActionsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                Uri data = intent.getData();
                ((SmartAppsDrawerFragment)pagerFragments.get(homeScreensCount)).packageAdded(data.getEncodedSchemeSpecificPart());
            }

            if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                Uri data = intent.getData();
                ((SmartAppsDrawerFragment)pagerFragments.get(homeScreensCount)).packageRemoved(data.getEncodedSchemeSpecificPart());
            }
        }
    }

    private final class ClippedDrawable extends Drawable {
        private final Drawable mWallpaper;

        public ClippedDrawable(Drawable wallpaper) {
            mWallpaper = wallpaper;
        }

        @Override
        public void draw(Canvas canvas) {
            mWallpaper.draw(canvas);
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            // Ensure the wallpaper is as large as it really is, to avoid stretching it
            // at drawing time
            mWallpaper.setBounds(left, top, left + mWallpaper.getIntrinsicWidth(),
                    top + mWallpaper.getIntrinsicHeight());
        }

        public void setAlpha(int alpha) {
            mWallpaper.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mWallpaper.setColorFilter(colorFilter);
        }

        public int getOpacity() {
            return mWallpaper.getOpacity();
        }
    }

}
