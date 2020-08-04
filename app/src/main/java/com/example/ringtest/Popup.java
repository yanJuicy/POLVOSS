package com.example.ringtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
/**
    사용 안하는 클래스, 삭제 예정
    전화 중 팝업 보여주기, OverlayService 이용
 */
public class Popup extends AppCompatActivity {

    TextView btn;
    static boolean isVibrate = true;
    Thread count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        final Vibrator vibrator;  // 진동 관리 변수
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        isVibrate = true;

        btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVibrate = false;
                finish();
            }
        });

        count = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    vibrator.vibrate(100);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while(isVibrate);
            }
        });
        count.start();


    }
}