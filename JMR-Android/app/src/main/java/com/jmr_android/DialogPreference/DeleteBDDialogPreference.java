package com.jmr_android.DialogPreference;

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

public class DeleteBDDialogPreference extends CustomDialogPreference{
    private String title;
    private String message;
    private String positiveButtonString;
    private String positiveButtonOnClickString;
    private String cancelButtonString;
    private MainActivity mainActivity;

    public DeleteBDDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
          title = "¿Eliminar base de datos?";
          message = "Se eliminará todo el contenido de la base de datos";
          positiveButtonString = "Eliminar";
          positiveButtonOnClickString = "Base de datos eliminada";
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
                mainActivity.deleteDataBase();
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