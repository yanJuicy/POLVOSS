package com.example.ringtest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.Thread.sleep;

public class MMSReceiver extends BroadcastReceiver
{
    private static final String TAG = "MMSReceiver";
    private Context _context;
    private int alerttime = 4; //toast 알림 출력 시간(n * 3.5 초 )
    Vibrator vibrator;  // 진동 관리 변수
    private int vibratetime = 5000;
    private Handler handler = new Handler();


    @Override
    public void onReceive(Context $context, final Intent $intent)
    {
//        Log.d(TAG, "onReceive() 호출됨."); // sms가 오면 onReceive() 가 호출된다. 여기에 처리하는 코드 작성하면 된다.
//        Bundle bundle = intent.getExtras();
//        SmsMessage[] messages = parseSmsMessage(bundle);  // parseSmsMessage() 메서드의 코드들은 SMS문자의 내용을 뽑아내는 정형화된 코드이다.
//        int CheckNum = 0;
//
//        if(messages.length>0){
//            // 문자메세지에서 송신자와 관련된 내용을 뽑아낸다.
//            String sender = messages[0].getOriginatingAddress();
//            Log.d(TAG, "sender: "+sender);
//
//            // 문자메세지 내용 추출
//            String contents = messages[0].getMessageBody().toString();
//            Log.d(TAG, "contents: "+contents);
//
//            // 수신 날짜/시간 데이터 추출
//            Date receivedDate = new Date(messages[0].getTimestampMillis());
//            Log.d(TAG, "received date: "+receivedDate);
//
//            // 해당 내용을 모두 합쳐서 액티비티로 보낸다.
//            URLCheck(contents, context);
//            //sendToActivity(context, sender, contents, receivedDate);
//            //OnSmsNotification(context, sender, contents);
//        }

        _context = $context;

        parseMMS();

//        Runnable runn = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                parseMMS();
//            }
//        };
//        Handler handler = new Handler();
//        handler.postDelayed(runn, 6000); // 시간이 너무 짧으면 못 가져오는게 있더라
//        Log.d("MMS_RECEIVE", "Receive complete");
//        handler.post(new Runnable() {//toast and overlay 보여주기
//
//            @Override
//            public void run() {
////                for( int i=0; i<alerttime; i++){
////                    //요기가 커스텀 토스트
////                    //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
//////                    showPopup();
////                }
//            }
//        });

        //노티피케이션 추가
        // sendNotification();
    }

    private void parseMMS()
    {
        ContentResolver contentResolver = _context.getContentResolver();
        final String[] projection = new String[] { "_id" };
        Uri uri = Uri.parse("content://mms");
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return;
        }

        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        cursor.close();

        String number = parseNumber(id);
        String msg = parseMessage(id);
        Log.d("MMS", "MMS Parse");

        URLCheck(msg, _context);
    }

    private String parseNumber(String $id)
    {
        String result = null;

        Uri uri = Uri.parse(MessageFormat.format("content://mms/{0}/addr", $id));
        String[] projection = new String[] { "address" };
        String selection = "msg_id = ? and type = 137";// type=137은 발신자
        String[] selectionArgs = new String[] { $id };

        Cursor cursor = _context.getContentResolver().query(uri, projection, selection, selectionArgs, "_id asc limit 1");

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return result;
        }

        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("address"));
        cursor.close();

        return result;
    }

    private String parseMessage(String $id)
    {
        String result = null;

        // 조회에 조건을 넣게되면 가장 마지막 한두개의 mms를 가져오지 않는다.
        Cursor cursor = _context.getContentResolver().query(Uri.parse("content://mms/part"), new String[] { "mid", "_id", "ct", "_data", "text" }, null, null, null);

        if (cursor.getCount() == 0)
        {
            cursor.close();
            return result;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String mid = cursor.getString(cursor.getColumnIndex("mid"));
            if ($id.equals(mid))
            {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));
                if ("text/plain".equals(type))
                {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));

                    if (TextUtils.isEmpty(data))
                        result = cursor.getString(cursor.getColumnIndex("text"));
                    else
                        result = parseMessageWithPartId(partId);
                }
            }
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    private void showPopup() {
        // 오버레이 서비스 시작
        _context.startService(new Intent(_context.getApplicationContext(), OverlayService.class));
    }



    private String parseMessageWithPartId(String $id)
    {
        Uri partURI = Uri.parse("content://mms/part/" + $id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            is = _context.getContentResolver().openInputStream(partURI);
            if (is != null)
            {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (!TextUtils.isEmpty(temp))
                {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {

                }
            }
        }
        return sb.toString();
    }

    /***
     * 상태 표시줄 알림 보내기
     * */
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context, "default");

        builder.setSmallIcon(R.mipmap.alert2);
        builder.setContentTitle("스미싱 위험 감지");
        builder.setContentText("MMS(멀티미디어 메세지) 스미싱이 우려됩니다. 주의해주세요.");

        Intent intent = new Intent(_context, DesignActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(_context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(_context.getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        builder.setColor(Color.RED);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(_context, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0, 7000};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(3, builder.build());
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
        this._context = context;
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);   // 진동 객체 초기화, 안드로이드 9까지 통화중 진동 가능 (아마도)

        Log.d(TAG, "내용 : "+ contents);
        String regex ="[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣@:%_.\\+~#=]{2,256}\\.[a-zA-Z0-9가-힣]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
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
//                Log.d("mms", "메세지수신");
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

//        }
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
}
