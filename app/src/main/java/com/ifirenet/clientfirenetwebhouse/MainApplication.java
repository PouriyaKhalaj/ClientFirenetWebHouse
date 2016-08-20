package com.ifirenet.clientfirenetwebhouse;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import com.ifirenet.clientfirenetwebhouse.Utils.TypefaceUtil;

import java.util.Locale;

/**
 * Created by ZHeydari on 2/13/2016.
 */
public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/irsans.ttf");
        updateLocale(this, "fa");
    }

    /**
     * update Locale of application in to FA
     *
     * @param context
     * @param lang
     */
    private static void updateLocale(Context context, String lang) {
        Log.i(TAG, "updateLocale : " + lang);
        Configuration configuration = new Configuration();
        if (!TextUtils.isEmpty(lang)) {
            configuration.locale = new Locale(lang);
        } else {
            configuration.locale = Locale.getDefault();
        }
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());

    }
}
