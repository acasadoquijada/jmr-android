package com.jmr_android.DialogPreference;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.example.alejandro.jmr_android.R;
import com.jmr_android.activity.MainActivity;
/**
 * Created by alejandro on 16/09/2017.
 */

public class LicensesPreference extends DialogPreference {
    public LicensesPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LicensesPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LicensesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LicensesPreference(Context context) {
        super(context);
    }
}
