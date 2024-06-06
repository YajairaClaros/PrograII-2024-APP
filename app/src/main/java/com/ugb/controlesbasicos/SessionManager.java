package com.ugb.controlesbasicos;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "userSession";
    private static final String KEY_DUI = "dui";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveDUI(String dui) {
        editor.putString(KEY_DUI, dui);
        editor.commit();
    }

    public String getDUI() {
        return sharedPreferences.getString(KEY_DUI, null);
    }
}