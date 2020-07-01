package com.example.ringtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class PhoneManageService extends Service {


    private boolean isStop;
    int time = 10;

    PhoneStateListener phoneStateListener;

    TelephonyManager telephonyManager;

    public PhoneManageService() {
    }


    @Override
    public void onCreate() {
        
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener  = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // 평소 상태
                    Toast.makeText(PhoneManageService.this, "일반 상태", Toast.LENGTH_SHORT).show();
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // 전화벨 울림
                    Toast.makeText(PhoneManageService.this, "전화벨 울림", Toast.LENGTH_SHORT).show();

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // 전화 받음
                    Toast.makeText(PhoneManageService.this, "전화 받음", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PhoneManageService.this, MyService.class);
                    startService(intent);
                }
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }
        };

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }
}
