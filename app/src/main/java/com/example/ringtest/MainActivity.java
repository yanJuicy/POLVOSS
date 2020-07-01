package com.example.ringtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    PhoneStateListener phoneStateListener;

    TelephonyManager telephonyManager;

    SharedPreferences sf;
    SharedPreferences.Editor editor;
    RadioGroup radioGroup;
    RadioButton r_btn1, r_btn2, r_btn3, r_btn4;
    Button timeButton;
    long time;
    int timeCheckId;
    String phoneNum;
    EditText inputPhoneNum;
    Button setPhoneNumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();

        r_btn1 = findViewById(R.id.rg_btn1);
        r_btn2 = findViewById(R.id.rg_btn2);
        r_btn3 = findViewById(R.id.rg_btn3);
        r_btn4 = findViewById(R.id.rg_btn4);
        radioGroup = findViewById(R.id.radioGroup);
        timeButton = findViewById(R.id.buttonSetTime);
        inputPhoneNum = findViewById(R.id.phoneNum);
        setPhoneNumButton = findViewById(R.id.buttonSetPhoneNum);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        int checkId = sf.getInt("timeCheckId", 1);
        editor = sf.edit();

        switch(checkId) {
            case 1:
                r_btn1.setChecked(true);
                break;
            case 2:
                r_btn2.setChecked(true);
                break;
            case 3:
                r_btn3.setChecked(true);
                break;
            case 4:
                r_btn4.setChecked(true);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_btn1:
                        time = 5 * 60;
                        timeCheckId = 1;
                        break;
                    case R.id.rg_btn2:
                        time = 10 * 60;
                        timeCheckId = 2;
                        break;
                    case R.id.rg_btn3:
                        time = 20 * 60;
                        timeCheckId = 3;
                        break;
                    case R.id.rg_btn4:
                        time = 10;
                        timeCheckId = 4;
                        break;
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("timeCheckId", timeCheckId);
                editor.commit();
                Toast.makeText(MainActivity.this, "시간 선택 완료", Toast.LENGTH_SHORT).show();
            }
        });

        // 서비스 시작 구현 부분 여기서 부터 진행하자
        setPhoneNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = inputPhoneNum.getText().toString();
                editor.putString("phoneNum", phoneNum);
                editor.commit();
                Toast.makeText(MainActivity.this, "설정 완료 서비스 시작", Toast.LENGTH_SHORT).show();

                //서비스 시작
                Intent intent = new Intent(MainActivity.this, PhoneManageService.class);
                startService(intent);
            }
        });


        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener  = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // 평소 상태
                    Toast.makeText(MainActivity.this, "일반 상태", Toast.LENGTH_SHORT).show();
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // 전화벨 울림
                    Toast.makeText(MainActivity.this, "전화벨 울림", Toast.LENGTH_SHORT).show();

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // 전화 받음
                    Toast.makeText(MainActivity.this, "전화 받음", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    startService(intent);
                }
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }
        };;

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void permissionCheck() {
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SEND_SMS}, 1);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SEND_SMS}, 1);
            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
    }


}