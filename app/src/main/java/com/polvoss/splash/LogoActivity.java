package com.polvoss.splash;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.polvoss.R;
import com.polvoss.guide.UserGuide;


public class LogoActivity extends AppCompatActivity implements View.OnClickListener {
    final int PERMISSION = 1;
    private TextToSpeech tts;

    ViewFlipper v_fllipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogo);

        int images[] = {
                R.mipmap.loading1,
                R.mipmap.loading2,
                R.mipmap.loading3,
                R.mipmap.loading4,
                R.mipmap.loading5,
                R.mipmap.loading6,
                R.mipmap.loading7,
                R.mipmap.loading8
        };

        v_fllipper = findViewById(R.id.loading);

        for(int image : images) {
            fllipperImages(image);
        }

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent1 = new Intent(LogoActivity.this, UserGuide.class);
                startActivity(intent1);
                finish();
            }


        }, 1500);


    }
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(600);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.fade_in);
        v_fllipper.setOutAnimation(this,android.R.anim.fade_out);
    }
    @Override
    public void onClick(View view) {

    }
}