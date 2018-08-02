package com.geekbrains.weather.prefs;

public interface PrefsHelper {

    String getSharedPreferences(String keyPref);

    void saveSharedPreferences(String keyPref, String value);

    void deleteSharedPreferences(String keyPref);
}