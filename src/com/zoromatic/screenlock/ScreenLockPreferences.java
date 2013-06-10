package com.zoromatic.screenlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenLockPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	static final int RESULT_ENABLE = 1;
	
	DevicePolicyManager deviceManager;
	ActivityManager activityManager;
	ComponentName compName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		deviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		compName = new ComponentName(this, ScreenLockDeviceAdmin.class);
		
		PreferenceManager localPrefs = getPreferenceManager();
        localPrefs.setSharedPreferencesName(Preferences.PREFS_NAME);
        
        addPreferencesFromResource(R.xml.screenlock_prefs);
        
        CheckBoxPreference lockScreenAdmin = (CheckBoxPreference)findPreference(Preferences.PREF_LOCK_SCREEN_ADMIN);
        
        if (lockScreenAdmin != null) {
        	boolean active = deviceManager.isAdminActive(compName);
        	lockScreenAdmin.setChecked(active);
        	lockScreenAdmin.setSummary(active?getResources().getString(R.string.enabled):getResources().getString(R.string.disabled));
        }
        
        Preference about = findPreference(Preferences.PREF_ABOUT_KEY);
        
        if (about != null) {
        	about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
				public boolean onPreferenceClick(Preference p) {
                	AboutDialog about = new AboutDialog(ScreenLockPreferences.this);
                	about.setTitle("About Zoromatic ScreenLock");
                	about.show();
                    return true;
                }
            });        	        	
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case 0:
    		if (resultCode == RESULT_OK){
                
            }
    		break;
		case RESULT_ENABLE:
			boolean result = (resultCode == Activity.RESULT_OK);
			if (result) {
				Log.i("LockScreenDeviceAdmin", "Admin enabled!");				
			} else {
				Log.i("LockScreenDeviceAdmin", "Admin enable FAILED!");				
			}			
			
			CheckBoxPreference lockScreenAdmin = (CheckBoxPreference)findPreference(Preferences.PREF_LOCK_SCREEN_ADMIN);
	        
	        if (lockScreenAdmin != null) {
	        	lockScreenAdmin.setChecked(result);
	        	lockScreenAdmin.setSummary(result?getResources().getString(R.string.enabled):getResources().getString(R.string.disabled));
	        }
		}
		super.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
    public void onBackPressed() {
        
		finish();
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	   
		try {
			if (key.equals(Preferences.PREF_LOCK_SCREEN_ADMIN)) {
				CheckBoxPreference lockScreenAdmin = (CheckBoxPreference)findPreference(Preferences.PREF_LOCK_SCREEN_ADMIN);
		        
		        if (lockScreenAdmin != null) {
		        	if (lockScreenAdmin.isChecked()) {
		        		Intent intent = new Intent(
		    					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		    			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
		    			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
		    					getResources().getString(R.string.additional_message));
		    			startActivityForResult(intent, RESULT_ENABLE);
		        	}
		        	else {
		        		deviceManager.removeActiveAdmin(compName);
		        	}	        		
		        }	        	
			}
		} catch (SecurityException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
		    
    @Override
    protected void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);	       	        
    }	     
}