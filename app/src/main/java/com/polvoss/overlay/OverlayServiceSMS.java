package com.polvoss.overlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.polvoss.R;

/***
 *  통화 화면 위에 알람을 띄움
 *
 */

public class OverlayServiceSMS extends Service {
    WindowManager wm;
    View mView;
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        //sendNotification();

        Log.d("OverlayService", "오버레이 시작");
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 오레오 이상부터는 Type_Application_Overlay를 써야함
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL; // 레이아웃 위치 조정
        //mView = inflate.inflate(R.layout.activity_overlay, null);
        mView = inflate.inflate(R.layout.activity_overlay_sms, null);


        /*final TextView textView = (TextView) mView.findViewById(R.id.overlay_textview);
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.overlay_bt);
        */
        final ImageButton bt =  mView.findViewById(R.id.imagebuttonsms);
        final TextView textView = mView.findViewById(R.id.accepttextsms);
        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //확인 누르기 전까지 무한진동
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(new long[]{100,1000,100,500,100,500,100,1000},0);
        } else{
            vibrator.vibrate(new long[]{100,1000,100,500,100,500,100,1000},0);
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bt.setImageResource(R.mipmap.ic_launcher_round);
                vibrator.cancel();
/*
                bt.setImageResource(R.mipmap.check_icon);
                textView.setText("확인 완료");
*/

                stopSelf();

            }
        });
        wm.addView(mView, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("OverlayService", "오버레이 종료");

        //확인 출력 시간
/*
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        if(wm != null) {
            if(mView != null) {
                wm.removeView(mView);
                mView = null;
            }
            wm = null;
        }
    }
}
