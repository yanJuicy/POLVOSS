package com.polvoss.ringtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class AutoRunReceiver extends BroadcastReceiver {

    private SharedPreferences sf;
    boolean powerOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AutoRunReceiver", "시작되서 호출됨");
        sf = context.getSharedPreferences("settingFile", MODE_PRIVATE);
        powerOn = sf.getBoolean("power", false);

        if(powerOn)
        {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent it = new Intent(context, PhoneManageService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(it);
                } else {
                    context.startService(it);
                }
            }
        }

    }
}

