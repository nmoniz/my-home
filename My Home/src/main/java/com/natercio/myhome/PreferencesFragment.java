package com.natercio.myhome;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 10/8/13
 * Time: 12:19 AM
 */
public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_HIDE_ACT_BAR = "pref_hide_action_bar";
    public static final String KEY_PREF_HOME_SCREENS = "pref_home_screens";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if (rootView != null) {
            rootView.setBackgroundResource(R.color.transparency_low);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_HIDE_ACT_BAR)) {
            if (sharedPreferences.getBoolean(KEY_PREF_HIDE_ACT_BAR, false))
                getActivity().getActionBar().hide();
            else
                getActivity().getActionBar().show();

        } else if (key.equals(KEY_PREF_HOME_SCREENS))
            ((Launcher) getActivity()).setHomeScreensCount(sharedPreferences.getInt(KEY_PREF_HOME_SCREENS, 2));
    }

}
