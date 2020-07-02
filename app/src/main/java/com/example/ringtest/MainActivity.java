package com.example.ringtest;

import androidx.annotation.NonNull;
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
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    final String TAG = "MainActivity"; // TAG

    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체

    RadioGroup radioGroup;
    RadioButton r_btn1, r_btn2, r_btn3, r_btn4;
    Button timeButton;
    EditText inputPhoneNum;
    Button setPhoneNumButton;

    int timeCheckId;    // 설정 시간 번호
    String phoneNum;    // 보호자 연락처 추후 여러개로 추가


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck(); // 권한 체크

        r_btn1 = findViewById(R.id.rg_btn1);
        r_btn2 = findViewById(R.id.rg_btn2);
        r_btn3 = findViewById(R.id.rg_btn3);
        r_btn4 = findViewById(R.id.rg_btn4);
        radioGroup = findViewById(R.id.radioGroup);
        timeButton = findViewById(R.id.buttonSetTime);
        inputPhoneNum = findViewById(R.id.phoneNum);
        setPhoneNumButton = findViewById(R.id.buttonSetPhoneNum);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체
        timeCheckId = sf.getInt("timeCheckId", 1);      // DB에 설정 시간이 존재하면 불러옴

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("timeCheckId", timeCheckId);      // 로컬 DB에 설정 시간 번호 저장
                editor.commit();
                Toast.makeText(MainActivity.this, "시간 선택 완료", Toast.LENGTH_SHORT).show();
            }
        });

        // 서비스 시작 구현 부분 여기서 부터 진행하자
        setPhoneNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = inputPhoneNum.getText().toString();
                editor.putString("phoneNum", phoneNum);     // DB에 보호자 번호 저장
                editor.commit();
                Toast.makeText(MainActivity.this, "설정 완료 서비스 시작", Toast.LENGTH_SHORT).show();

                // 서비스 시작
                Intent intent = new Intent(MainActivity.this, PhoneManageService.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("timeCheckId", timeCheckId);
                startService(intent);
            }
        });
    }

    private void permissionCheck() {
        AutoPermissions.Companion.loadAllPermissions(this,101); // 권한 설정 오픈소스

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // 상태 표시줄 알림을 위한 작업
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
    }


    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        //Toast.makeText(this, "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }
}