package com.sportsboards2d.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

import com.sportsboards2d.R;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class SettingsViewer extends PreferenceActivity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.settings);
        
        CheckBoxPreference lineEnabled = (CheckBoxPreference) findPreference("lineEnabled");
        lineEnabled.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				SharedPreferences prefs = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = prefs.edit();
				
				if(prefs.getBoolean("lineEnabled", false)==false){
					editor.putBoolean("lineEnabled", true);
				}
				else{
					editor.putBoolean("lineEnabled", false);
				}
				
				editor.commit();	
				
				return true;
			}
        	
        });
    }
}
