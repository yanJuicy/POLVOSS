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
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class MyService extends Service {

    SharedPreferences sf;
    private boolean isStop;
    int time = 10;
    Thread counter;
    String phoneNum;
    int timeCheckId;
    Vibrator vibrator;
    ArrayList<String> contactList;

    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        contactList = getContacts();
        Log.d("MyService", "MyService 생성");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        phoneNum = sf.getString("phoneNum", "");


        counter = new Thread(new Counter());
        counter.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "MyService 시작");
        Log.d("MyService", phoneNum + " " + timeCheckId);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    private class Counter implements Runnable {

        private int count;
        private int alertcount;

        private Handler handler = new Handler();

        @Override
        public void run() {
            for (count = 0; count < 3; count++) {
                if (isStop) break;
                
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                for(alertcount = 0; alertcount<10; alertcount++){
                    show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    private void sendSMS() {
            String phoneNo = phoneNum;
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

}
