/**package com.ifirenet.clientfirenetwebhouse.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 05/09/2015.

public class SharedPreference {
    Context context;

    public SharedPreference(Context context){
        this.context = context;
    }
    //Set shared preferences
    public void Set(String MY_PREFS_NAME, String InputKey, String inputStr){
        SharedPreferences shp = context.getSharedPreferences(MY_PREFS_NAME, 0);
        SharedPreferences.Editor shpE = shp.edit();
        shpE.putString(InputKey, inputStr);
        shpE.commit();
    }
    //get shared preferences
    public String Get(String MY_PREFS_NAME, String InputKey) {
        SharedPreferences shp = context.getSharedPreferences(MY_PREFS_NAME, 0);
        return shp.getString(InputKey, "NotFind");
    }
    //Delete shared Preference
    public void Delete(String MY_PREFS_NAME){
        SharedPreferences pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    //Remove row on Shared Preferences
    public void Remove(String MY_PREFS_NAME, String InputKey){
        SharedPreferences shp = context.getSharedPreferences(MY_PREFS_NAME, 0);
        shp.edit().remove("text").commit();
    }
}
 */
