package com.jmr_android.preference;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.Toast;

import com.jmr_android.activity.MainActivity;

/**
 * Created by alejandro on 16/09/2017.
 */

public class CalculateBDDialogPreference extends CustomDialogPreference{
    private String title;
    private String message;
    private String positiveButtonString;
    private String positiveButtonOnClickString;
    private String cancelButtonString;
    private ProgressDialog progress;
    private MainActivity mainActivity;

    public CalculateBDDialogPreference(Context context, AttributeSet attrs) {
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


                mainActivity = (MainActivity) MainActivity.act;

                 progress = ProgressDialog.show(getContext(), "Calculando",
                        "Calculando base de datos", true);

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mainActivity.calculateDataBase();
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                progress.dismiss();
                                Toast.makeText(getContext(),positiveButtonOnClickString, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
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