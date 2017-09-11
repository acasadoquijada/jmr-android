package com.example.alejandro.jmr_android.fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alejandro.jmr_android.R;
import com.example.alejandro.jmr_android.activity.MainActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by alejandro on 03/09/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference myPref = (Preference) findPreference("myKey");
    private ListPreference mListPreference;
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.settings);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        CheckBoxPreference singleColorDescriptorButton = (CheckBoxPreference)
                findPreference("singleColor");

        CheckBoxPreference structureColorDescriptorButton =  (CheckBoxPreference)
                findPreference("structureColor");

        switch (key){

            case "singleColor":

                if(singleColorDescriptorButton.isChecked()){
                    ((MainActivity)getActivity()).setActiveDescriptor
                            (ConsultFragment.SINGLE_COLOR_DESCRIPTOR);

                    structureColorDescriptorButton.setChecked(false);

                }
                else{

                    if(!structureColorDescriptorButton.isChecked()){
                        singleColorDescriptorButton.setChecked(true);
                    }

                }

                break;

            case "structureColor":

                if(structureColorDescriptorButton.isChecked()){
                    ((MainActivity)getActivity()).setActiveDescriptor
                            (ConsultFragment.MPEG7_COLOR_STRUCTURE);

                    singleColorDescriptorButton.setChecked(false);

                }

                else{
                    if(!singleColorDescriptorButton.isChecked()){
                        structureColorDescriptorButton.setChecked(true);
                    }
                }

                break;

        }

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