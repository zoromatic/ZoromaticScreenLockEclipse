package com.zoromatic.screenlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ScreenLockActivity extends Activity {
	static final int RESULT_ENABLE = 1;

	DevicePolicyManager deviceManager;
	ActivityManager activityManager;
	ComponentName compName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);

		deviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		compName = new ComponentName(this, ScreenLockDeviceAdmin.class);

		boolean active = deviceManager.isAdminActive(compName);
		if (active) {
			deviceManager.lockNow();
		}
		else {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					getResources().getString(R.string.additional_message));
			startActivityForResult(intent, RESULT_ENABLE);
		}
		
		finish();
	}
}