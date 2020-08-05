package com.example.ringtest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class DesignActivity extends AppCompatActivity implements AutoPermissionsListener {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    ImageView powerButton;
    ImageView settingButton;
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

        checkPermission(); // 권한 설정 확인

        // id 매핑
        textState = findViewById(R.id.textState);
        voiceState = findViewById(R.id.textVoiceFishingState);
        smsState = findViewById(R.id.textSmishingState);
        powerButton = findViewById(R.id.powerBtn);
        settingButton = findViewById(R.id.settingButton);

        // DB에서 파워 버튼 설정 값 확인
        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        editor = sf.edit();
        powerOn = sf.getBoolean("power", false);
        if (powerOn) {
            powerButton.setImageResource(R.mipmap.lock);
        } else {
            powerButton.setImageResource(R.mipmap.unlock);
        }

        // 파워 버튼 클릭 이벤트
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에서 보호자 번호가 있는지 확인
                String phoneNo1 = sf.getString("phoneNum1", "");
                String phoneNo2 = sf.getString("phoneNum2", "");
                String phoneNo3 = sf.getString("phoneNum3", "");

                if (phoneNo1.equals("") && phoneNo2.equals("") && phoneNo3.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
                    startActivity(new Intent(DesignActivity.this, MainActivity.class));
                    return;
                }
                // 파워 버튼 상태 변경
                powerOn = !powerOn;
                editor.putBoolean("power", powerOn);
                editor.commit();

                if (powerOn) { // 서비스 시작
                    changeUI();
                    serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                    serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                    startService(serviceIntent);
                } else { // 서비스 종료
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
                Toast.makeText(DesignActivity.this, "설정창 누름", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DesignActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeUI() { // 파워 버튼 상태값에 따른 UI 변경
        if (powerOn) {
            powerButton.setImageResource(R.mipmap.lock);
            Toast.makeText(DesignActivity.this, "서비스 시작", Toast.LENGTH_SHORT).show();
            textState.setText("안전하게 보호중입니다.");
            voiceState.setText("활성화");
            smsState.setText("활성화");
        } else {
            powerButton.setImageResource(R.mipmap.unlock);
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

    public void checkPermission() {
        AutoPermissions.Companion.loadAllPermissions(this,101); // 권한 설정 오픈소스

        // 다른 앱 위에 그리기 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리

            } else {
                //startService(new Intent(MainActivity.this, MyService.class));
            }
        }
    }
}
