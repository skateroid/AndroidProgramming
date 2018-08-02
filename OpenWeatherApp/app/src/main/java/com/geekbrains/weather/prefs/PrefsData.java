package com.geekbrains.weather.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.geekbrains.weather.ui.base.BaseActivity;

public class PrefsData implements PrefsHelper {

    private SharedPreferences sharedPreferences;

    public PrefsData(BaseActivity baseActivity) {
        this.sharedPreferences = baseActivity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public String getSharedPreferences(String keyPref) {
        return sharedPreferences.getString(keyPref, "");
    }

    @Override
    public void saveSharedPreferences(String keyPref, String value) {
        sharedPreferences.edit().putString(keyPref, value).apply();
    }

    @Override
    public void deleteSharedPreferences(String keyPref) {
        sharedPreferences.edit().remove(keyPref).apply();
    }
}
