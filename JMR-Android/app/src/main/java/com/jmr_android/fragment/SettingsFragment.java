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

/**
 * Created by alejandro on 03/09/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CalculateBDDialogPreference2 calculateBDDialogPreference2;
    private Preference myPref = (Preference) findPreference("myKey");
    private ListPreference mListPreference;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // additional setup

        calculateBDDialogPreference2 = new CalculateBDDialogPreference2(getContext(),null);

        calculateBDDialogPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });

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

            case "images":

                EditTextPreference editTextPreferenceImages = (EditTextPreference)
                        findPreference(key);

                String text = editTextPreferenceImages.getText();

                /* Comrpobar si son mas que size de gallery(); */
                editTextPreferenceImages.setSummary("Imágenes a consultar: " + text);

                ((MainActivity) getActivity()).setImageConsultNumber(Integer.valueOf(text));

                break;

            case "deleteDB":


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
    public class CalculateBDDialogPreference2 extends CustomDialogPreference {
        private String title;
        private String message;
        private String positiveButtonString;
        private String positiveButtonOnClickString;
        private String cancelButtonString;

        public CalculateBDDialogPreference2(Context context, AttributeSet attrs) {
            super(context, attrs);
            title = "¿Calcular base de datos?";
            message = "Se calculará la base de datos, reduciendo el tiempo de consulta";
            positiveButtonString = "Calcular";
            positiveButtonOnClickString = "Base de datos calculada";
            cancelButtonString = "Cancelar";
        }

        @Override
        protected void onClick() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(true);
            dialog.setPositiveButton(positiveButtonString, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {


                    //((MainActivity) getContext()).calculateDataBase();

                    //reset database
                    Toast.makeText(getContext(),positiveButtonOnClickString, Toast.LENGTH_SHORT).show();
                }
            });

            dialog.setNegativeButton(cancelButtonString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int which)
                {
                    dlg.cancel();
                }
            });

            AlertDialog al = dialog.create();
            al.show();
        }
    }

}