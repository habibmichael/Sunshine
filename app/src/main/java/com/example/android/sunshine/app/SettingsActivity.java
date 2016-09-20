package com.example.android.sunshine.app;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // addPreferencesFromResource(R.xml.pref_general);
       //display fragment
        if(savedInstanceState==null) {
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content,
                    new SettingsFragment()).commit();
        }



    }



}
