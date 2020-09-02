package com.example.ringtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.Thread.sleep;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //알람
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    Vibrator vibrator;  // 진동 관리 변수
    private Context context;
    private Handler handler = new Handler();
    private int alerttime = 4; //toast 알림 출력 시간(n * 3.5 초 )
    private String alertText = "위험 요소가 감지되었습니다.\n1. 금전 요구\n2. 기관 사칭\n3. 협박\n 요소가 있으실 경우 유의 해주세요";

    private int vibratetime = 5000;

    @Override
    public void onReceive(Context context, Intent intent) { OnSmsReceive(context, intent); }

    private void OnSmsReceive(Context context, Intent intent){
        Log.d(TAG, "onReceive() 호출됨."); // sms가 오면 onReceive() 가 호출된다. 여기에 처리하는 코드 작성하면 된다.
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);  // parseSmsMessage() 메서드의 코드들은 SMS문자의 내용을 뽑아내는 정형화된 코드이다.
        int CheckNum = 0;

        if(messages.length>0){
            // 문자메세지에서 송신자와 관련된 내용을 뽑아낸다.
            String sender = messages[0].getOriginatingAddress();
            Log.d(TAG, "sender: "+sender);

            // 문자메세지 내용 추출
            String contents = messages[0].getMessageBody().toString();
            Log.d(TAG, "contents: "+contents);

            // 수신 날짜/시간 데이터 추출
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "received date: "+receivedDate);

            // 해당 내용을 모두 합쳐서 액티비티로 보낸다.
            URLCheck(contents, context);
            //sendToActivity(context, sender, contents, receivedDate);
            //OnSmsNotification(context, sender, contents);
        }

    }

    // 데이터 받아오기
    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[])bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i=0;i<objs.length;i++){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i], format);
            }
            else{
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
            }

        }
        return messages;
    }

    private void OnSmsNotification(Context context, String sender, String contents){
        Log.d(TAG, "Notification() 호출됨");
        // Notification.Builder builder = new Notification.Builder(context); OS버전을 확인해야야한다.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher); // 스마트폰 위에 뜨는 조그마한 창
        builder.setTicker("Message");
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("보낸사람:"+ sender); // 출력할 제목
        builder.setContentText(contents); // 출력할 내용

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

//        String replyLabel = getResources().getString(R.string.reply_label);
//        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
//                .setLabel(replyLabel)
//                .build();

        // 이동할 페이지 설정
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.putExtra("data", "sion");
        builder.setContentIntent(PendingIntent.getActivity(
                context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT)); // 클릭동작 셋팅

        // 알림창 띄우기
        Notification notification = builder.build(); // builder 실행하기
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        //노티피케이션 매니저 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {     // 오레오 버전 이상 노티피케이션 알림 설정
            manager.createNotificationChannel(new NotificationChannel("SMS", "SMS팝업", NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, notification);

    }

    private void URLCheck(String contents, final Context context){
        //내용에서 .을 찾는다
        //.의 인덱스를 배열에 넣는다 순서대로 ( 브루드 포스트 알고리즘 사용 )
        //.의 인데스의 -1 +1번째 문자에 영단어가 들어가는지 판단한다
        // url인지 아닌지 구별한다
        // + 더 정확하게 url 인지 아닌지 판단할 수 있는 기준을 더한다.
        int idx = -1;
        int checkNum = 0;
        int result = -1;
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);   // 진동 객체 초기화, 안드로이드 9까지 통화중 진동 가능 (아마도)

        Log.d(TAG, "내용 : "+ contents);
        String regex ="[(http(s)?):\\/\\/(www\\)?a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣@:%_" +
                "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ①②③④⑤⑥⑦⑧⑨㉠㉡㉢㉣㉤㉥㉦㉧㉨㉩㉪㉫㉬㉭㉮㉯㉰㉱㉲㉳㉴㉵㉶㉷㉸㉹㉺㉻" +
                "\\+~#=]{2,256}\\.[a-zA-Z0-9가-힣]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m=p.matcher(contents);

        if(m.find()){
            //sb.append(m.group(0));
            //System.out.println("==="+m.group(0));
         /*for(int i=0;i<=m.groupCount();i++){s
             System.out.println(i+"==="+m.group(i));
             sb.append(m.group(i));
         }*/
            Log.d(TAG, "메세지수신");
            handler.post(new Runnable() {//toast and overlay 보여주기

                //오버레이 팝업 출력
                @Override
                public void run() {
                    for( int i=0; i<alerttime; i++){
                        //요기가 커스텀 토스트
                        //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
                        showPopup();
                    }
                }
            });
            //노티피케이션 보내기

            sendNotification();
        }
//        Uri uri = Uri.parse(contents);
//        if (uri.getScheme() == null || uri.getScheme().isEmpty()) {
//            // safe text
//            Log.d("URL X", contents);
//
//        } else {
//            // has url
//            Log.d("URL O", contents);
//        }
//        while(true)
//        {
//            checkNum = contents.indexOf(".", idx+1);
//            if(checkNum == -1)
//            {
//                break;
//            }
//            idx = contents.indexOf(".", idx+1);
//            result = check(idx, contents);
//            if(result == 1)
//            {
//                Log.d(TAG, "메세지수신");
//                handler.post(new Runnable() {//toast and overlay 보여주기
//
//                    //오버레이 팝업 출력
//                    @Override
//                    public void run() {
//                        for( int i=0; i<alerttime; i++){
//                            //요기가 커스텀 토스트
//                            //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
//                            showPopup();
//                        }
//                    }
//                });
//
//                //노티피케이션 보내기
//
//                sendNotification();
//                break;
//            }
//
//        }
    }

    /*private void old_showPopup() {
        Intent intent = new Intent(context.getApplicationContext(), Popup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }*/

    private void showPopup() {
        // 오버레이 서비스 시작
        context.startService(new Intent(context.getApplicationContext(), OverlayServiceSMS.class));
    }


    private static int check(int idx, String contents){
        if(idx <= 0)
            return -1;
        else{
            if((contents.charAt(idx-1) >= 65 && contents.charAt(idx-1) <= 122) || (contents.charAt(idx+1) >= 65 && contents.charAt(idx+1) <= 122))
                return 1;
        }
        return -1;
    }

    /***
     * 상태 표시줄 알림 보내기
     * */
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        builder.setSmallIcon(R.mipmap.alert2);
        builder.setContentTitle("스미싱 위험 감지");
        builder.setContentText("SMS(메시지) 스미싱이 의심됩니다. 주의해주세요.");

        Intent intent = new Intent(context, DesignActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        builder.setColor(Color.RED);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

//        long[] vibrate = {0, 7000};
//        builder.setVibrate(vibrate);
//        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(4, builder.build());
    }

}