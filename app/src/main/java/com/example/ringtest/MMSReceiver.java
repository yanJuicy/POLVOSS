package com.example.ringtest;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import static java.lang.Thread.sleep;

public class MMSReceiver extends BroadcastReceiver
{
    private Context _context;
    private int alerttime = 4; //toast 알림 출력 시간(n * 3.5 초 )
    Vibrator vibrator;  // 진동 관리 변수
    private int vibratetime = 5000;


    @Override
    public void onReceive(Context $context, final Intent $intent)
    {
        _context = $context;

        Runnable runn = new Runnable()
        {
            @Override
            public void run()
            {
                parseMMS();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runn, 6000); // 시간이 너무 짧으면 못 가져오는게 있더라
        Log.d("MMS_RECEIVE", "Receive complete");
        handler.post(new Runnable() {//toast and overlay 보여주기

            @Override
            public void run() {
                for( int i=0; i<alerttime; i++){
                    //요기가 커스텀 토스트
                    //customToast.makeText(PhoneManageService.this, alertText, Toast.LENGTH_LONG).show();
                    showPopup();
                }
            }
        });
        //진동
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibratetime, 70));
          /*  try {
                sleep(vibratetime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
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
}
