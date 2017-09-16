package com.jmr_android.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.example.alejandro.jmr_android.R;
import com.jmr_android.DialogPreference.CustomDialogPreference;
import com.jmr_android.activity.MainActivity;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.takisoft.fix.support.v7.preference.SwitchPreferenceCompat;

import android.support.v14.preference.SwitchPreference;

/**
 * Created by alejandro on 03/09/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference myPref = (Preference) findPreference("myKey");
    private ListPreference mListPreference;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        CheckBoxPreference singleColorDescriptorButton = (CheckBoxPreference)
                findPreference("singleColor");

        CheckBoxPreference structureColorDescriptorButton = (CheckBoxPreference)
                findPreference("structureColor");

        switch (key) {

            case "singleColor":

                if (singleColorDescriptorButton.isChecked()) {
                    ((MainActivity) getActivity()).setActiveDescriptor
                            (ConsultFragment.SINGLE_COLOR_DESCRIPTOR);

                    structureColorDescriptorButton.setChecked(false);

                } else {

                    if (!structureColorDescriptorButton.isChecked()) {
                        singleColorDescriptorButton.setChecked(true);
                    }

                }

                break;

            case "structureColor":

                if (structureColorDescriptorButton.isChecked()) {
                    ((MainActivity) getActivity()).setActiveDescriptor
                            (ConsultFragment.MPEG7_COLOR_STRUCTURE);

                    singleColorDescriptorButton.setChecked(false);

                } else {
                    if (!singleColorDescriptorButton.isChecked()) {
                        structureColorDescriptorButton.setChecked(true);
                    }
                }

                break;

            case "imagesNumber":

                EditTextPreference editTextPreferenceImages = (EditTextPreference)
                        findPreference(key);

                String text = editTextPreferenceImages.getText();

                /* Comprobar si son mas que size de gallery(); */
                editTextPreferenceImages.setSummary("Imágenes a consultar: " + text);

                ((MainActivity) getActivity()).setImageConsultNumber(Integer.valueOf(text));

                SwitchPreference switchPreferenceCompatrence = (SwitchPreference)
                        findPreference("switchAllImages");

                if(switchPreferenceCompatrence.isChecked()){
                    switchPreferenceCompatrence.setChecked(false);
                }

                break;

            case "deleteDB":
                ((MainActivity) getActivity()).deleteDataBase();
                break;


            case "switchAllImages":

                switchPreferenceCompatrence = (SwitchPreference)
                        findPreference(key);

                if(switchPreferenceCompatrence.isChecked()){
                    ((MainActivity) getActivity()).setAllImageConsult();

                    editTextPreferenceImages = (EditTextPreference)
                            findPreference("imagesNumber");

                    editTextPreferenceImages.setSummary("Imágenes a consultar: -");

                    ((MainActivity) getActivity()).setAllImageConsult();
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