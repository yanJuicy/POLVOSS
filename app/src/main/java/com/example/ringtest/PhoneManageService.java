package com.example.ringtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class PhoneManageService extends Service {

    private boolean isStop;
    int time = 10;
    boolean exists = false;
    Thread counter;
    Vibrator vibrator;
    PhoneStateListener phoneStateListener;
    TelephonyManager telephonyManager;
    ArrayList<String> contactList;
    SharedPreferences sf;

    public PhoneManageService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Log.d("PhoneManageService", "PhoneManageService 생성");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PhoneManageService", "PhoneManageService 시작");
        final String phoneNum = intent.getStringExtra("phoneNum");
        final int timeCheckId = intent.getIntExtra("timeCheckId", 1);

        Log.d("PhoneManageService", phoneNum + " " + timeCheckId);

        final Intent serviceIntent = new Intent(PhoneManageService.this, MyService.class);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener  = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // 평소 상태
                    Log.d("PhoneManageService", "일반 상태");
                    if (isStop) {
                        Log.d("PhoneManageService", "My 서비스 종료");
                        //stopService(serviceIntent);

                        isStop = false;
                    }
                    // Toast.makeText(PhoneManageService.this, "일반 상태", Toast.LENGTH_SHORT).show();
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // 전화벨 울림
                    Log.d("PhoneManageService", "전화벨 울림");
                    // Toast.makeText(PhoneManageService.this, "전화벨 울림", Toast.LENGTH_SHORT).show();

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // 전화 받음
                    Log.d("PhoneManageService", "전화 받음");
                    // Toast.makeText(PhoneManageService.this, "전화 받음", Toast.LENGTH_SHORT).show();
                    contactList = getContacts();
                    Log.d("PhoneManageService", "전화번호부 사이즈: " + contactList.size());

                    if (!contactList.contains(phoneNumber)) {
                        Log.d("PhoneManageService", "카운트 서비스 시작");
                        counter = new Thread(new Counter());
                        counter.start();
                    }

                    // startService(serviceIntent);
                    isStop = true;
                }
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public ArrayList<String> getContacts() {
        ArrayList<String> contacts = new ArrayList<String>();
        int idx = 0;
        Cursor c = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        while (c.moveToNext()) {
            String phNumber = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phNumber.indexOf('-') != -1){
                phNumber = phNumber.replaceAll("-", "");
            }
            contacts.add(phNumber);
        }
        c.close();

        return contacts;
    }

    private class Counter implements Runnable {

        private int count;
        private Handler handler = new Handler();

        @Override
        public void run() {
            for (count = 0; count < 10; count++) {


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Count", count+"");
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            vibrator.vibrate(7000);
            sendSMS();
            show();


        }
    }

    private void sendSMS() {
        sf = getSharedPreferences("settingFile", MODE_PRIVATE);

        String phoneNo = sf.getString("phoneNum", "");
        String sms = "위험위험";

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알림 제목");
        builder.setContentText("알림 세부 텍스트");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        builder.setColor(Color.RED);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0, 7000};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
