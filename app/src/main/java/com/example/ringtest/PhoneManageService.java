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
import android.graphics.PixelFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class PhoneManageService extends Service {

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

    public PhoneManageService() {
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
        Log.d("PhoneManageService", "PhoneManageService 종료");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra("stop",false))
            stopSelf();

        Log.d("PhoneManageService", "PhoneManageService 시작");
        //final String phoneNum = intent.getStringExtra("phoneNum");
        //final int timeCheckId = intent.getIntExtra("timeCheckId", 1);

        //Log.d("PhoneManageService", phoneNum + " " + timeCheckId);

        startForegroundService();//포어그라운드 동작

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener  = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // 평소 상태
                    Log.d("PhoneManageService", "일반 상태");
                    isCount = false;
                    Popup.isVibrate = false;
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // 전화벨 울림
                    Log.d("PhoneManageService", "전화벨 울림");


                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // 전화 받음
                    Log.d("PhoneManageService", "전화 받음");
                    // Toast.makeText(PhoneManageService.this, "전화 받음", Toast.LENGTH_SHORT).show();
                    contactList = getContacts();
                    // Log.d("PhoneManageService", "전화번호부 사이즈: " + contactList.size());

                    if(!contactList.contains(phoneNumber)) {
                        Log.d("PhoneManageService", "카운트 서비스 시작");
                        isCount = true;
                        counter = new Thread(new Counter());
                        counter.start();
                    }
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

    /***
     * 포어그라운드 함수
     * */
    private void startForegroundService(){
        //오래오에서는 채널이 필수이다! 채널
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.lock3);//아이콘 설정
        //위 포어그라운들 아이콘으로 뜰 디스크립션
        builder.setContentTitle("Call Stop");
        builder.setContentTitle("보이스 피싱 및 스미싱 보호중");


        Intent intent = new Intent(this, DesignActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);//팬딩 인텐트 지정


        //노티피케이션 매니저 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {     // 오레오 버전 이상 노티피케이션 알림 설정
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1, builder.build());
    }

    /***
     * 전화번호부 가져오기
     * */
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

    /***
     * 카운팅 작업, 설정 시간 만큼 카운트
     * */
    private class Counter implements Runnable {

        private int count;

        private int vibratetime = 7000;
        private Handler handler = new Handler();
        private int settingTime = 10;
        //요기가 위험 Toast 출력내용
        private int alerttime = 4; //toast 알림 출력 시간(n * 3.5 초 )

        @Override
        public void run() {
            if (isServiceStop) return; // 서비스가 종료된 상태면 실행 앟남

            // DB에서 설정된 시간을 가져옴
            sf = getSharedPreferences("settingFile", MODE_PRIVATE);
            long min = sf.getLong("min", 0);
            boolean voice = sf.getBoolean("voice_fishing", false);

            if(voice){
                //settingTime = (int) min * 60;
                settingTime = 0;

                int check = 0; // check와 settingTime을 비교해서 첫 경고 알람을 보냄
                int check1 = 0; // 두번째 경고 알람
                int check2 = 0; //
                int check3 = 0;

                /**첫번째 알람 카운팅**/
                for (count = 0; count < settingTime; count++) {   // 설정 시간만큼 카운트
                    if (isCount) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Count", count + "");
                            }
                        });
                        try {
                            sleep(1000);
                            check++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /**첫번째 알람 출력**/
                // 설정 시간 이상 통화가 계속되면
                if (check >= settingTime) {
                    //showPopup();    // 팝업 보여주기

                    // 커스텀 토스트 보냄
                    handler.post(new Runnable() {//toast and overlay 보여주기
                        @Override
                        public void run() {
                            for( int i=0; i<alerttime; i++) {
                                //요기가 커스텀 토스트
                                //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
                                showPopup(); // 오버레이 팝업을 보여줌
                            }

                            // 보호자에게 문자를 보낸다
                            sendSMS();

                            // 노티피케이션 알람을 보낸다.
                            sendNotification();

                        }
                    });


                    /**두번째 알람 카운팅**/
                    for (count = check ; count < settingTime + 5; count++) {   // 설정 시간만큼 카운트
                        if (isCount) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("Count", count + "");
                                }
                            });
                            try {
                                sleep(1000);
                                check++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    /**두번째 알람 출력**/
                    if(check >= settingTime+5){
                        handler.post(new Runnable() {//toast and overlay 보여주기
                            @Override
                            public void run() {
                                for( int i=0; i<alerttime; i++) {
                                    //요기가 커스텀 토스트
                                    //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
                                    showPopup1(); // 오버레이 팝업을 보여줌
                                }

                                // 보호자에게 문자를 보낸다
                                //sendSMS();

                                // 노티피케이션 알람을 보낸다.
                                //sendNotification();

                            }
                        });


                        /**세번째 알람 카운팅**/
                        for (count = check ; count < settingTime + 10; count++) {   // 설정 시간만큼 카운트
                            if (isCount) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("Count", count + "");
                                    }
                                });
                                try {
                                    sleep(1000);
                                    check++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**세번째 알람 출력**/
                        if(check >= settingTime+10) {
                            handler.post(new Runnable() {//toast and overlay 보여주기
                                @Override
                                public void run() {
                                    for (int i = 0; i < alerttime; i++) {
                                        //요기가 커스텀 토스트
                                        //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
                                        showPopup2(); // 오버레이 팝업을 보여줌
                                    }
                                    // 보호자에게 문자를 보낸다
                                    //sendSMS();

                                    // 노티피케이션 알람을 보낸다.
                                    //sendNotification();

                                }
                            });
                        }


                    }
                }
            }
        }
    }

    /**
     * 팝업 보여주기
     */
    private void showPopup() {
        // 오버레이 서비스 시작
        startService(new Intent(getApplicationContext(), OverlayService.class));
    }

    private void showPopup1() {
        // 오버레이 서비스 시작
        startService(new Intent(getApplicationContext(), OverlayService1.class));

        Log.d("Count", "OVERLAY1");

    }

    private void showPopup2() {
        // 오버레이 서비스 시작
        startService(new Intent(getApplicationContext(), OverlayService2.class));
    }
    private void showPopup3() {
        // 오버레이 서비스 시작
        //startService(new Intent(getApplicationContext(), OverlayService3.class));
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

        String sms = "[안심전화 서비스 자동발신]\n사용자께서 모르는 전화와 통화를 하고 있습니다\n피싱 위험이 감지되니 안심 전화를 걸어 주세요";

        if (!phoneNum1.equals("")) { // 번호가 존재하면 문자 전송
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum1, null, sms, null, null);
            }catch(Exception e){
                //Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (!phoneNum2.equals("")) {
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum2, null, sms, null, null);
                // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (!phoneNum3.equals("")) {
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum3, null, sms, null, null);
                // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (!phoneNum4.equals("")) {
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum4, null, sms, null, null);
                // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                // Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (!phoneNum5.equals("")) {
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum5, null, sms, null, null);
                // Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
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

    /***
     * 상태 표시줄 알림 보내기
     * */
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.alert2);
        builder.setContentTitle("피싱 위험 감지");
        builder.setContentText("보이스 피싱이 우려됩니다. 주의해주세요.");

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

        long[] vibrate = {0, 7000};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }


}
