package com.zoromatic.screenlock;  
  
import android.app.admin.DeviceAdminReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.widget.Toast;  
  
public class ScreenLockDeviceAdmin extends DeviceAdminReceiver{  
  
  
    static SharedPreferences getSamplePreferences(Context context) {  
        return context.getSharedPreferences(  
          DeviceAdminReceiver.class.getName(), 0);  
    }  
  
    void showToast(Context context, CharSequence msg) {  
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();  
    }  
  
    @Override  
    public void onEnabled(Context context, Intent intent) {  
        showToast(context, context.getResources().getString(R.string.message_enabled));  
    }  
  
    @Override  
    public CharSequence onDisableRequested(Context context, Intent intent) {  
        return context.getResources().getString(R.string.optional_message);  
    }  
  
    @Override  
    public void onDisabled(Context context, Intent intent) {  
        showToast(context, context.getResources().getString(R.string.message_disabled));  
    }      
} 
