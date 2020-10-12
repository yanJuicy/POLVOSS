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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class TestService extends Service {

    Thread counter;     // 전화 받았을 때 서비스에서 하는 작업
    Vibrator vibrator;  // 진동 관리 변수
    PhoneStateListener phoneStateListener;
    TelephonyManager telephonyManager;
    ArrayList<String> contactList;      // 전화번호부를 담을 객ㅊ체
    SharedPreferences sf;               // DB 객체
    boolean isServiceStop;
    boolean isCount;

    WindowManager wm;
    View mView;

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);   // 진동 객체 초기화, 안드로이드 9까지 통화중 진동 가능 (아마도)
        Log.d("PhoneManageService", "PhoneManageService 생성");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStop=true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");
        showPopup(); // 오버레이 팝업을 보여줌

        sendSMS();
        sendNotification();
        return super.onStartCommand(intent, flags, startId);
    }



        /**
         * 팝업 보여주기
         */
        private void showPopup() {
            // 오버레이 서비스 시작
            startService(new Intent(getApplicationContext(), OverlayServiceTest.class));
        }


        /***
         * 문자 보내기
         * */
        private void sendSMS() {
            sf = getSharedPreferences("settingFile", MODE_PRIVATE);

            String phoneNum1 = sf.getString("contactPhone1", "");
            String phoneNum2 = sf.getString("contactPhone2", "");
            String phoneNum3 = sf.getString("contactPhone3", "");
            String phoneNum4 = sf.getString("contactPhone4", "");
            String phoneNum5 = sf.getString("contactPhone5", "");

            String sms = "[경찰 폴보스 앱 자동발신]\n모르는 전화와 장시간 통화 중으로 사기피해를 당할 수 있으니 확인전화 해주세요.\n피해신고 112";
            //String sms2 = "사기전화일 수 있으니 사용자에게 안심전화를 걸어주세요. 지급정지, 피해신고 긴급전화 112";

            if (!phoneNum1.equals("")) { // 번호가 존재하면 문자 전송
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum1, null, sms, null, null);
                    //smsManager.sendTextMessage(phoneNum1, null, sms2, null, null);
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            if (!phoneNum2.equals("")) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum2, null, sms, null, null);
                    //smsManager.sendTextMessage(phoneNum1, null, sms2, null, null);
                    // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            if (!phoneNum3.equals("")) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum3, null, sms, null, null);
                    //smsManager.sendTextMessage(phoneNum1, null, sms2, null, null);
                    // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            if (!phoneNum4.equals("")) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum4, null, sms, null, null);
                    //smsManager.sendTextMessage(phoneNum1, null, sms2, null, null);
                    // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            if (!phoneNum5.equals("")) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum5, null, sms, null, null);
                    //smsManager.sendTextMessage(phoneNum1, null, sms2, null, null);
                    // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

//        try{
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNum1, null, sms, null, null);
//            // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
//        }catch(Exception e){
//            // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
        }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.logo4);
        builder.setContentTitle("테스트 실행");
        builder.setContentText("보호자에게 메세지가 전송되었는지 확인하세요.");

        Intent intent = new Intent(this, DesignActivity.class);
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

//        long[] vibrate = {0, 7000};
//        builder.setVibrate(vibrate);
//        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }

    }


