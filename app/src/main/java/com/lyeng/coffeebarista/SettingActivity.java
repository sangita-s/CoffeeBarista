package com.lyeng.coffeebarista;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private static SharedPreferences prefs;
    public static final String KEY_PREF_NOTIFICATION = "notification";
    public static final String KEY_PREF_SORT = "sort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.xml.activity_setting);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
