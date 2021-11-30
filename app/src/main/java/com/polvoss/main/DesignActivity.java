package com.polvoss.main;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.polvoss.overlay.AcceptOverlay;
import com.polvoss.message.MMSReceiver;
import com.polvoss.voice.PhoneManageService;
import com.polvoss.R;
import com.polvoss.settings.SettingActivity;
import com.polvoss.message.SmsReceiver;

public class DesignActivity extends AppCompatActivity implements AutoPermissionsListener {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    ImageView powerButton;
    ImageView settingButton;
    boolean powerOn;
    boolean smsOn;
    boolean voiceOn;
    boolean kakaoOn;
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    Intent serviceIntent;
    private Context mContext;

    TextView textState;
    TextView textState2;
    TextView textState3;
    TextView textState4;

    TextView voiceState;
    TextView smsState;

    ViewFlipper v_fllipper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design2);

        mContext = this;

        checkPermission(); // 권한 설정 확인

        // id 매핑
        textState = findViewById(R.id.textState);
        textState2 = findViewById(R.id.textState2);
        textState3 = findViewById(R.id.textState3);
        textState4 = findViewById(R.id.textState4);
        //voiceState = findViewById(R.id.textVoiceFishingState);
        //smsState = findViewById(R.id.textSmishingState);
        settingButton = findViewById(R.id.settingButton);
        powerButton = findViewById(R.id.powerBtn);

        int images[] = {
                R.mipmap.slogan1_1,
                R.mipmap.slogan1_2,
                R.mipmap.slogan2_1,
                R.mipmap.slogan2_2,
                R.mipmap.slogan3_1,
                R.mipmap.slogan3_2

        };

        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }


        // DB에서 파워 버튼 설정 값 확인
        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        editor = sf.edit();
        powerOn = sf.getBoolean("power", false);
        if (powerOn) {
            powerButton.setImageResource(R.mipmap.lock5);
            changeUI();
            changeReceiver();
//            voiceState.setText("활성화");
//            smsState.setText("활성화");
        } else {
            powerButton.setImageResource(R.mipmap.unlock5);
            changeUI();
            changeReceiver();
//            voiceState.setText("비 활성화");
//            smsState.setText("비 활성화");
        }

        // 파워 버튼 클릭 이벤트
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                changeUI();
                // DB에서 보호자 번호가 있는지 확인
                String phoneNo1 = sf.getString("contactPhone1", "");
                String phoneNo2 = sf.getString("contactPhone2", "");
                String phoneNo3 = sf.getString("contactPhone3", "");
                smsOn = sf.getBoolean("smishing", false);
                voiceOn = sf.getBoolean("voice_fishing", false);
                kakaoOn = sf.getBoolean("kakaoCheck", false);
                boolean kakaoOn = sf.getBoolean("kakaoCheck", false);

                // 파워 버튼 상태 변경
                powerOn = !powerOn;
                editor.putBoolean("power", powerOn);
                editor.commit();

                Log.d("DesignActivity ", phoneNo1);

                if(powerOn){        //파워가 켜졌을때 (voice가 켜져있으면 연락처도 확인해봐야 한다.)
                    if(voiceOn && smsOn && kakaoOn){        //셋 다 켜져있을때
                        if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
                            // 파워 버튼 상태 변경
                            powerOn = !powerOn;
                            editor.putBoolean("power", powerOn);
                            editor.commit();

                            Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DesignActivity.this, SettingActivity.class));
                            return;
                        } else{     //설정된 휴대폰 번호가 있으면 서비스 실행
                            Toast.makeText(DesignActivity.this, "서비스 시작(보이스, 스미싱, 카카오)", Toast.LENGTH_SHORT).show();
                            changeUI();
                            changeReceiver();
                            serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                            serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                            startService(serviceIntent);
                        }
                    }
                    else if(voiceOn && smsOn && !kakaoOn){    //카카오만 꺼져있을때
                        if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
                            // 파워 버튼 상태 변경
                            powerOn = !powerOn;
                            editor.putBoolean("power", powerOn);
                            editor.commit();

                            Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DesignActivity.this, SettingActivity.class));
                            return;
                        } else{     //설정된 휴대폰 번호가 있으면 서비스 실행
                            Toast.makeText(DesignActivity.this, "서비스 시작(보이스, 스미싱)", Toast.LENGTH_SHORT).show();
                            changeUI();
                            changeReceiver();
                            serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                            serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                            startService(serviceIntent);
                        }
                    }
                    else if(voiceOn && !smsOn && kakaoOn){    //스미싱만 꺼져있을때
                        if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
                            // 파워 버튼 상태 변경
                            powerOn = !powerOn;
                            editor.putBoolean("power", powerOn);
                            editor.commit();

                            Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DesignActivity.this, SettingActivity.class));
                            return;
                        } else{     //설정된 휴대폰 번호가 있으면 서비스 실행
                            Toast.makeText(DesignActivity.this, "서비스 시작(보이스, 카카오)", Toast.LENGTH_SHORT).show();
                            changeUI();
                            changeReceiver();
                            serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                            serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                            startService(serviceIntent);
                        }
                    }
                    else if(!voiceOn && smsOn && kakaoOn){    //보이스만 꺼져있을때
                        Toast.makeText(DesignActivity.this, "서비스 시작(스미싱, 카카오)", Toast.LENGTH_SHORT).show();
                        changeUI();
                        changeReceiver();
                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                        startService(serviceIntent);
                    }
                    else if(voiceOn && !smsOn && !kakaoOn){   //카카오, 스미싱 꺼져있을때
                        if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
                            // 파워 버튼 상태 변경
                            powerOn = !powerOn;
                            editor.putBoolean("power", powerOn);
                            editor.commit();

                            Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DesignActivity.this, SettingActivity.class));
                            return;
                        } else{     //설정된 휴대폰 번호가 있으면 서비스 실행
                            Toast.makeText(DesignActivity.this, "서비스 시작(보이스)", Toast.LENGTH_SHORT).show();
                            changeUI();
                            changeReceiver();
                            serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                            serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                            startService(serviceIntent);
                        }
                    }
                    else if(!voiceOn && smsOn && !kakaoOn){   //카카오, 보이스 꺼져있을때
                        Toast.makeText(DesignActivity.this, "서비스 시작(스미싱)", Toast.LENGTH_SHORT).show();
                        changeUI();
                        changeReceiver();
                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                        startService(serviceIntent);
                    }
                    else if(!voiceOn && !smsOn && kakaoOn){   //보이스, 스미싱 꺼져있을때
                        Toast.makeText(DesignActivity.this, "서비스 시작(카카오)", Toast.LENGTH_SHORT).show();
                        changeUI();
                        changeReceiver();
                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
                        startService(serviceIntent);
                    }
                    else{     //다 꺼져있을때
                        powerOn = !powerOn;
                        editor.putBoolean("power", powerOn);
                        editor.commit();

                        Toast.makeText(DesignActivity.this, "기능을 실행시켜 주세요.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DesignActivity.this, SettingActivity.class));
                        return;
                    }
                } else{             //파워 꺼졌을때
                    changeUI();
                    changeReceiver();
                    if(serviceIntent == null){
                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
                    }
                    if (serviceIntent != null) {
                        Toast.makeText(DesignActivity.this, "서비스 종료", Toast.LENGTH_SHORT).show();
                        serviceIntent.putExtra("stop", true);
                        stopService(serviceIntent);
                        serviceIntent = null;
                    }
                }

//                if (powerOn && smsOn && voiceOn) { // 서비스 시작 (스미싱, 보이스피싱 둘 다 켜져있을때)
//                    if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
//                        // 파워 버튼 상태 변경
//                        powerOn = !powerOn;
//                        editor.putBoolean("power", powerOn);
//                        editor.commit();
//
//                        Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(DesignActivity.this, SettingActivity.class));
//                        return;
//                    } else{
//                        Toast.makeText(DesignActivity.this, "서비스 시작(보이스, 스미싱)", Toast.LENGTH_SHORT).show();
//                        changeUI();
//                        changeReceiver();
//                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
//                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
//                        startService(serviceIntent);
//                    }
//                } else if(powerOn && smsOn && !voiceOn){ // 서비스 시작 (스미싱만 켜져있을때)
//                    Toast.makeText(DesignActivity.this, "서비스 시작(스미싱)", Toast.LENGTH_SHORT).show();
//                    changeUI();
//                    changeReceiver();
//                    serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
//                    serviceIntent.setAction("startForeground"); //포그라운드 액션지정
//                    startService(serviceIntent);
//                } else if(powerOn && !smsOn && voiceOn){ // 서비스 시작 (보이스만 켜져있을때)
//                    if (phoneNo1.equals("")) { // 설정된 휴대폰 번호가 없으면 MainActivity로 이동
//                        // 파워 버튼 상태 변경
//                        powerOn = !powerOn;
//                        editor.putBoolean("power", powerOn);
//                        editor.commit();
//
//                        Toast.makeText(DesignActivity.this, "보호자 연락처를 설정해 주세요.", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(DesignActivity.this, SettingActivity.class));
//                        return;
//                    } else{
//                        Toast.makeText(DesignActivity.this, "서비스 시작(보이스)", Toast.LENGTH_SHORT).show();
//                        changeUI();
//                        changeReceiver();
//                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
//                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
//                        startService(serviceIntent);
//                    }
//                } else if(powerOn && !smsOn && !voiceOn){ // 버튼 둘다 꺼져있는데 실행눌렀을때
//                    if(kakaoOn){
//                        Toast.makeText(DesignActivity.this, "서비스 시작(카카오)", Toast.LENGTH_SHORT).show();
//                        changeUI();
//                        changeReceiver();
//                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
//                        serviceIntent.setAction("startForeground"); //포그라운드 액션지정
//                        startService(serviceIntent);
//                    } else{
//                        // 파워 버튼 상태 변경
//                        powerOn = !powerOn;
//                        editor.putBoolean("power", powerOn);
//                        editor.commit();
//
//                        Toast.makeText(DesignActivity.this, "보이스피싱 또는 스미싱 기능을 켜주세요", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(DesignActivity.this, SettingActivity.class));
//                        return;
//                    }
//
//                } else { // 서비스 종료
//                    changeUI();
//                    changeReceiver();
//                    if(serviceIntent == null){
//                        serviceIntent = new Intent(DesignActivity.this, PhoneManageService.class);
//                    }
//                    if (serviceIntent != null) {
//                        Toast.makeText(DesignActivity.this, "서비스 종료", Toast.LENGTH_SHORT).show();
//                        serviceIntent.putExtra("stop", true);
//                        stopService(serviceIntent);
//                        serviceIntent = null;
//                    }
//
//                }
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_running = sf.getBoolean("power", false);
                if(is_running){
                    /*
                    Toast.makeText(DesignActivity.this, "서비스 실행중", Toast.LENGTH_SHORT).show();
                    */
                    showPopup();
                }
                else{
                    //Toast.makeText(DesignActivity.this, "설정창 누름", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DesignActivity.this, SettingActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }


    private void changeUI() { // 파워 버튼 상태값에 따른 UI 변경
        if (powerOn) {
            powerButton.setImageResource(R.mipmap.lock5);
//            Toast.makeText(DesignActivity.this, "서비스 시작", Toast.LENGTH_SHORT).show();
            textState.setText("경찰로고를 터치하면 ");
            textState2.setText("서비스(보호)를 ");
            textState3.setText("종료");
            textState3.setTextColor(Color.parseColor("#FFBF00"));
            textState4.setText("합니다.");
//            voiceState.setText("활성화");
//            smsState.setText("활성화");
        } else {
            powerButton.setImageResource(R.mipmap.unlock5);
            textState.setText("경찰로고를 터치하면 ");
            textState2.setText("서비스(보호)를 ");
            textState2.setText("서비스(보호)를 ");
            textState3.setText("실행");
            textState3.setTextColor(Color.parseColor("#124798"));
            textState4.setText("합니다.");

//            voiceState.setText("비 활성화");
//            smsState.setText("비 활성화");
        }
    }

    private void changeReceiver(){
        smsOn = sf.getBoolean("smishing", false);

        ComponentName smsComponent = new ComponentName(mContext, SmsReceiver.class);
        PackageManager smsPackage = mContext.getPackageManager();

        ComponentName mmsComponent = new ComponentName(mContext, MMSReceiver.class);
        PackageManager mmsPackage = mContext.getPackageManager();

        if (powerOn){
            // SMS Broadcast Receiver 켜기
            if(smsOn){
                Log.d("receiverLog", "Receiver On");
                smsPackage.setComponentEnabledSetting(
                        smsComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
                mmsPackage.setComponentEnabledSetting(
                        mmsComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
                // SMS Broadcast Receiver 끄기
            } else{
                Log.d("receiverLog", "Receiver Off");
                smsPackage.setComponentEnabledSetting(
                        smsComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                );
                mmsPackage.setComponentEnabledSetting(
                        mmsComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                );
            }
            // SMS, MMS Broadcast Receiver 끄기
        } else{
            Log.d("receiverLog", "Receiver Off");
            smsPackage.setComponentEnabledSetting(
                    smsComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
            );

            mmsPackage.setComponentEnabledSetting(
                    mmsComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
            );
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

    private void showPopup() {
        // 오버레이 서비스 시작
        Intent intent = new Intent(DesignActivity.this, AcceptOverlay.class);
        startService(intent);
        // Toast.makeText(DesignActivity.this, "서비스 실행중", Toast.LENGTH_SHORT).show();
    }
}
