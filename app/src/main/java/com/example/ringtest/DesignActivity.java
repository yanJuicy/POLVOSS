package com.example.ringtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class DesignActivity extends AppCompatActivity implements AutoPermissionsListener {

    ImageView powerButton;
    ImageView settingButton;
    ImageView imageActivate;
    boolean powerOn;
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    Intent serviceIntent;

    TextView textState;
    TextView voiceState;
    TextView smsState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        AutoPermissions.Companion.loadAllPermissions(this,101); // 권한 설정 오픈소스

        textState = findViewById(R.id.textState);
        voiceState = findViewById(R.id.textVoiceFishingState);
        smsState = findViewById(R.id.textSmishingState);
        imageActivate = findViewById(R.id.imageActivate);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        editor = sf.edit();
        powerButton = findViewById(R.id.powerBtn);
        settingButton = findViewById(R.id.settingButton);

        powerOn = sf.getBoolean("power", false);
        if (powerOn) {
            powerButton.setImageResource(R.drawable.power_on);
        } else {
            powerButton.setImageResource(R.drawable.power_off);
        }

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = sf.getString("phoneNum", "");
                if (phoneNo.equals("")) {
                    startActivity(new Intent(DesignActivity.this, MainActivity.class));
                    return;
                }
                powerOn = !powerOn;
                editor.putBoolean("power", powerOn);
                editor.commit();

                if (powerOn) {
                    // 서비스 시작
                    changeUI();
                    serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                    serviceIntent.setAction("startForeground");//포그라운드 액션지정
                    startService(serviceIntent);
                } else {
                    // 서비스 종료
                    changeUI();
                    if (serviceIntent != null) {
                        Toast.makeText(DesignActivity.this, "서비스 종료", Toast.LENGTH_SHORT).show();
                        serviceIntent.putExtra("stop", true);
                        stopService(serviceIntent);
                        serviceIntent = null;
                    }

                }
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DesignActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeUI() {
        if (powerOn) {
            powerButton.setImageResource(R.drawable.power_on);
            imageActivate.setImageResource(R.drawable.success);
            Toast.makeText(DesignActivity.this, "서비스 시작", Toast.LENGTH_SHORT).show();
            textState.setText("안전하게 보호중입니다.");
            voiceState.setText("활성화");
            smsState.setText("활성화");
        } else {
            powerButton.setImageResource(R.drawable.power_off);
            imageActivate.setImageResource(R.drawable.warning);
            textState.setText("보호중이 아닙니다.");
            voiceState.setText("비활성화");
            smsState.setText("비활성화");
        }

    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}
