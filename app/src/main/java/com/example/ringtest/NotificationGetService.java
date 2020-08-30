package com.example.ringtest;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationGetService extends NotificationListenerService {

    SharedPreferences sf;

    public NotificationGetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationListener", " onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("NotificationListener", " onStart");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotificationListener", " onBind");
        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("NotificationListener", " onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("NotificationListener", " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("NotificationListener", " onConnected");

    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d("NotificationListener", " onDisConnected");

    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
        Log.d("NotificationListener", " hint" + hints);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        boolean powerState = sf.getBoolean("power", false);
        if (!powerState) return;

        String kakao = "com.kakao.talk";

        Log.d("NotificationListener", " packageName" + sbn.getPackageName());
        Log.d("NotificationListener", " postTime" + sbn.getPostTime());

        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        //int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
        //Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

        Log.d("NotificationListener", " title" + title);
        Log.d("NotificationListener", " text" + text);
        Log.d("NotificationListener", " subtext" + subText);

        if (sbn.getPackageName().equals(kakao)) {
            if (text != null && URLCheck(text.toString())) {
                // 오버레이 서비스 시작
                Intent intent = new Intent(NotificationGetService.this, OverlayServiceSMS.class);
                startService(intent);
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private boolean URLCheck(String contents) {
        //내용에서 .을 찾는다
        //.의 인덱스를 배열에 넣는다 순서대로 ( 브루드 포스트 알고리즘 사용 )
        //.의 인데스의 -1 +1번째 문자에 영단어가 들어가는지 판단한다
        // url인지 아닌지 구별한다
        // + 더 정확하게 url 인지 아닌지 판단할 수 있는 기준을 더한다.

        Log.d("NotificationListener", "내용 : " + contents);
        String regex = "[(http(s)?):\\/\\/(www\\)?a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣@:%_" +
                "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ①②③④⑤⑥⑦⑧⑨㉠㉡㉢㉣㉤㉥㉦㉧㉨㉩㉪㉫㉬㉭㉮㉯㉰㉱㉲㉳㉴㉵㉶㉷㉸㉹㉺㉻" +
                "\\+~#=]{2,256}\\.[a-zA-Z0-9가-힣]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(contents);

        if (m.find()) {
            Log.d("NotificationListener", " URL 메세지수신");
            return true;
        }

        return false;
    }

}
