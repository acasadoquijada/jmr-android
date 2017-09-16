package com.jmr_android.fragment;

/**
 * Created by alejandro on 03/09/2017.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alejandro.jmr_android.R;
import com.jmr_android.activity.MainActivity;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class AditionalFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference myPref = (Preference) findPreference("myKey");
    private ListPreference mListPreference;
    private int previousImageNumber = 200;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.aditional);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

    }


    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        // setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {

        }

        /*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("enable_background", false);
        editor.putString("server_address", "server1.com");
        editor.apply();
*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}