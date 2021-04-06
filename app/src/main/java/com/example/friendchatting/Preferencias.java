package com.example.friendchatting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import android.preference.ListPreference;
import androidx.preference.PreferenceManager;

public class Preferencias extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);
        LoadSetting();

    }

    private void LoadSetting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean night = sp.getBoolean("NIGHT",false);
        if(night){
            getListView().setBackgroundColor(Color.parseColor("#222222"));

        }else{
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }

        android.preference.Preference checkBoxPreference = findPreference("NIGHT");
        checkBoxPreference.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object o) {
                boolean si = (boolean)o;
                if(si){
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                }else{
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return false;
            }


        });


        Preference lp = findPreference("ORIENTACION");
        String orientation = sp.getString("ORIENTACION","false");
        if("1".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        }else if("2".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if("3".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String items = (String)o;
                if(preference.getKey().equals("ORIENTACION")){
                    switch (items){
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                            break;
                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }



                }
                return false;
            }
        });

    }

    @Override
    protected void onResume(){
        LoadSetting();
        super.onResume();
    }
}
