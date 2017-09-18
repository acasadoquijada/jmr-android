package com.jmr_android.preference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

/**
 * Created by alejandro on 16/09/2017.
 */

public class AditionalPreference extends CustomDialogPreference {
    public AditionalPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AditionalPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AditionalPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AditionalPreference(Context context) {
        super(context);
    }

    @Override
    protected void onClick() {

        String key = getKey();
        Intent browserIntent;
        switch (key){

            case "JMRADeveloper":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/acasadoquijada/"));

                getContext().startActivity(browserIntent);
                break;

            case "JMRALicense":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.gnu.org/licenses/gpl-3.0.en.html"));

                getContext().startActivity(browserIntent);
                break;

            case "JMRAVersion":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/acasadoquijada/jmr-android"));

                getContext().startActivity(browserIntent);
                break;

            case "descriptorInfo":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://es.wikipedia.org/wiki/Descriptores_visuales#Descriptores_de_informaci.C3.B3n_general"));

                getContext().startActivity(browserIntent);
                break;

            case "CBIRInfo":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://es.wikipedia.org/wiki/Consulta_de_im%C3%A1genes_mediante_ejemplo"));

                getContext().startActivity(browserIntent);
                break;
        }

    }
}
