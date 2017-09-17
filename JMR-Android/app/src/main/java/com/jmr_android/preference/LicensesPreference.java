package com.jmr_android.preference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

/**
 * Created by alejandro on 16/09/2017.
 */

public class LicensesPreference extends CustomDialogPreference {
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

            case "fabDeveloper":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Clans"));

                getContext().startActivity(browserIntent);
                break;

            case "fabLicense":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.apache.org/licenses/LICENSE-2.0"));

                getContext().startActivity(browserIntent);
                break;

            case "bottomDeveloper":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/sephiroth74"));

                getContext().startActivity(browserIntent);
                break;

            case "bottomLicense":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://opensource.org/licenses/MIT"));

                getContext().startActivity(browserIntent);
                break;

            case "glideDeveloper":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/bumptech"));

                getContext().startActivity(browserIntent);
                break;

            case "glideLicense":
                browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/bumptech/glide/blob/master/LICENSE"));

                getContext().startActivity(browserIntent);
                break;
        }

    }
}
