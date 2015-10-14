package com.zoromatic.screenlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class ScreenLockActivity extends Activity {
	static final int RESULT_ENABLE = 1;

	DevicePolicyManager deviceManager;
	ActivityManager activityManager;
	ComponentName compName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVisible(false);
		//setContentView(R.layout.main);

		deviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		compName = new ComponentName(this, ScreenLockDeviceAdmin.class);

		boolean active = deviceManager.isAdminActive(compName);
		if (active) {
			// ICS workaround 
			Handler handlerUI = new Handler();
             handlerUI.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                	 try {
                    	 deviceManager.lockNow();
                     } catch (SecurityException e) {
                    	 Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_disabled), Toast.LENGTH_LONG).show();
                     }
                 }
             }, 200);
             finish();
             // ICS workaround finish
             
             try {
            	 deviceManager.lockNow();
             } catch (SecurityException e) {
            	 Toast.makeText(this, getResources().getString(R.string.message_disabled), Toast.LENGTH_LONG).show();
             }
		}
		else {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					getResources().getString(R.string.additional_message));
			intent.putExtra("force-locked", DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
			startActivityForResult(intent, RESULT_ENABLE);
		}
		
		finish();
	}
}