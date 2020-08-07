package com.example.ringtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.lang.annotation.Retention;

public class SettingActivity extends AppCompatActivity {
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    int maxX;
    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체
    TextView setNumber;
    TextView version;
    TextView osLicense;
    TextView inputPhoneNum1;
    TextView inputPhoneNum2;
    TextView inputPhoneNum3;
    LinearLayout voiceSettingLayout;
    LinearLayout numberSettingLayout1;
    LinearLayout numberSettingLayout2;
    LinearLayout numberSettingLayout3;
    LinearLayout smishingLayout;
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;
    //Button saveBtn;
    Switch voicePower;
    Switch smishingPower;
    Switch smsPower;
    Switch mmsPower;

    ImageView closeBtn;
    private boolean mIsBound;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        closeBtn= findViewById(R.id.closeBtn);

        SeekBar seekBarTime = findViewById(R.id.seekBarTime);
        final TextView textViewTime = findViewById(R.id.textViewTime);

        setNumber = findViewById(R.id.setNumber);
        version = findViewById(R.id.version_Info);
        osLicense = findViewById(R.id.OS_License);

        voiceSettingLayout = findViewById(R.id.voice_Setting_Layout);
        numberSettingLayout1 = findViewById(R.id.set_Num_Layout1);
        numberSettingLayout2 = findViewById(R.id.set_Num_Layout2);
        numberSettingLayout3 = findViewById(R.id.set_Num_Layout3);

        smishingLayout = findViewById(R.id.smishing_layout);

        inputPhoneNum1 = findViewById(R.id.setting_phoneNum1);
        inputPhoneNum2 = findViewById(R.id.setting_phoneNum2);
        inputPhoneNum3 = findViewById(R.id.setting_phoneNum3);
        deleteBtn1 = findViewById(R.id.deleteBtn1);
        deleteBtn2 = findViewById(R.id.deleteBtn2);
        deleteBtn3 = findViewById(R.id.deleteBtn3);
        //saveBtn = findViewById(R.id.saveBtn);

        voicePower = findViewById(R.id.voice_Power);
        smishingPower = findViewById(R.id.smishing_Power);
        smsPower = findViewById(R.id.sms_Power);
        mmsPower = findViewById(R.id.mms_Power);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        String phoneNum1 = sf.getString("phoneNum1", "");
        String phoneNum2 = sf.getString("phoneNum2", "");
        String phoneNum3 = sf.getString("phoneNum3", "");

        inputPhoneNum1.setText(phoneNum1);
        inputPhoneNum2.setText(phoneNum2);
        inputPhoneNum3.setText(phoneNum3);

        numberSettingLayout1.setVisibility(View.VISIBLE);


        if(!inputPhoneNum1.getText().equals(""))  // 1번이 들어있으면
        {
            if(!inputPhoneNum2.getText().equals("")) // 2번이 들어있으면
            {
                numberSettingLayout3.setVisibility(View.VISIBLE);
            }
            numberSettingLayout2.setVisibility(View.VISIBLE);
        }

        /*******************************************
         * 보이스 피싱 파워
         ******************************************/
        boolean voiceFishing = sf.getBoolean("voice_fishing", false);
        if(voiceFishing){
            voiceSettingLayout.setVisibility(View.VISIBLE);
            voicePower.setChecked(true);
        } else{
            voiceSettingLayout.setVisibility(View.GONE);
            voicePower.setChecked(false);
        }



        /*******************************************
         * 스미싱 버튼 확인부분
         ******************************************/
        boolean smishing = sf.getBoolean("smishing", false);
        if(smishing){
            smishingLayout.setVisibility(View.VISIBLE);
            smishingPower.setChecked(true);
        } else{
            smishingLayout.setVisibility(View.GONE);
            smishingPower.setChecked(false);
        }

        boolean sms = sf.getBoolean("sms", false);
        if(sms){
            smsPower.setChecked(true);
        } else{
            smsPower.setChecked(false);
        }

        boolean mms = sf.getBoolean("mms", false);
        if(mms){
            mmsPower.setChecked(true);
        } else{
            mmsPower.setChecked(false);
        }

        /*******************************************
         * 시간 설정, text가 범위 초과하는 현상 발생
         *******************************************/

        textViewTime.setText("Time : " + seekBarTime.getProgress());
        Point maxSizePoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
        maxX = maxSizePoint.x;

        long min = sf.getLong("min", 0);
        textViewTime.setText("Time : " + min + "분");

        seekBarTime.setProgress((int) min);
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                int val = (progressValue * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewTime.setText("Time : " + progressValue + "분");
                int textViewX = val - (textViewTime.getWidth() / 2);
                int finalX = textViewTime.getWidth() + textViewX > maxX ? (maxX - textViewTime.getWidth() - 16) : textViewX + 16 /*your margin*/;
                textViewTime.setX(finalX < 0 ? 16/*your margin*/ : finalX);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("VoiceSetting", "DB 저장 " + seekBar.getProgress());
                long min = seekBar.getProgress();
                editor.putLong("min", min);      // 로컬 DB에 설정 시간 저장
                editor.commit();
            }
        });



        this.TextClickListener();
        this.SetNumberClickListener();
        this.ButtonClickListener();
        this.SwitchCheckedListener();

        /***********
         * Fragment 연결
         *
        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Voice"));
        tabLayout.addTab(tabLayout.newTab().setText("SMS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager)findViewById(R.id.pager);


        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ************/
    }

    /*******************************************
     * 설정창 종료, 텍스트 클릭 이벤트
     *******************************************/
    public void TextClickListener()
    {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.closeBtn:
                        Toast.makeText(SettingActivity.this, "종료 누름", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
//                    // 전화번호 입력 창 레이아웃
//                    case R.id.setNumber:
//                        Log.e("로그", "0");
//                        Toast.makeText(SettingActivity.this, "번호 설정 클릭", Toast.LENGTH_SHORT).show();
//
//                        if(layout.getVisibility()==View.VISIBLE) // 클릭시 레이아웃 Visiability 설정
//                        {
//                            layout.setVisibility(View.GONE);
//                        }
//
//                        else
//                        {
//                            layout.setVisibility(View.VISIBLE);
//                        }
//                        break;

                    // 버전 정보
                    case R.id.version_Info:
                        Toast.makeText(SettingActivity.this, "Version 1.0.0", Toast.LENGTH_SHORT).show();
                        break;

                    // OS 라이센스 정보
                    case R.id.OS_License:
                        Toast.makeText(SettingActivity.this, "OS 클릭", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        closeBtn.setOnClickListener(Listener);
        setNumber.setOnClickListener(Listener);
        version.setOnClickListener(Listener);
        osLicense.setOnClickListener(Listener);
    }

    /*******************************************
     * 보호자 전화번호 설정 클릭 이벤트
     *******************************************/
    public void SetNumberClickListener()
    {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);

                switch (view.getId()) {

                    // 번호 설정
                    case R.id.setting_phoneNum1 :
                        editor.putInt("textViewNum", 1);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;

                    case R.id.setting_phoneNum2 :
                        editor.putInt("textViewNum", 2);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;

                    case R.id.setting_phoneNum3 :
                        editor.putInt("textViewNum", 3);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;
                }
            }
        };
        inputPhoneNum1.setOnClickListener(Listener);
        inputPhoneNum2.setOnClickListener(Listener);
        inputPhoneNum3.setOnClickListener(Listener);
    }

    /*******************************************
     * 전화번호 저장 및 삭제 클릭 이벤트
     *******************************************/
    public void ButtonClickListener()
    {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    // 저장 버튼
                    case R.id.saveBtn:
                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
                        editor.commit();
                        Toast.makeText(SettingActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

                        break;

                    // 번호 삭제
                    case R.id.deleteBtn1 :
                        inputPhoneNum1.setText("");
                        if(!inputPhoneNum3.getText().equals("")) // 1, 2, 3 모두 있는 상태
                        {
                            inputPhoneNum1.setText(inputPhoneNum2.getText());
                            inputPhoneNum2.setText(inputPhoneNum3.getText());
                            inputPhoneNum3.setText("");
                        }
                        else if(!inputPhoneNum2.getText().equals("")) // 1, 2가 있는 상태
                        {
                            inputPhoneNum1.setText(inputPhoneNum2.getText());
                            inputPhoneNum2.setText("");
                            numberSettingLayout3.setVisibility(View.GONE);
                        }
                        else { // 1만 있는 상태
                            numberSettingLayout2.setVisibility(View.GONE);
                        }

                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
                        editor.commit();
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn2 :
                        inputPhoneNum2.setText("");
                        if(!inputPhoneNum3.getText().equals("")) // 1, 2, 3 모두 있는 상태
                        {
                            inputPhoneNum2.setText(inputPhoneNum3.getText());
                            inputPhoneNum3.setText("");
                        }
                        else {
                            numberSettingLayout3.setVisibility(View.GONE);
                        }

                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
                        editor.commit();
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn3 :
                        inputPhoneNum3.setText("");
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
                        editor.commit();
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;
                }
            }
        };
        //saveBtn.setOnClickListener(Listener);
        deleteBtn1.setOnClickListener(Listener);
        deleteBtn2.setOnClickListener(Listener);
        deleteBtn3.setOnClickListener(Listener);
    }

    public void SwitchCheckedListener()
    {
        // 보이스피싱 파워 체크
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {

                    // 보이스피싱 파워 체크
                    case R.id.voice_Power :
                        if(isChecked)
                        {
                            editor.putBoolean("voice_fishing", true);
                            editor.commit();

                            voiceSettingLayout.setVisibility(View.VISIBLE);

                            Toast.makeText(SettingActivity.this, "VoiceFishing On", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            editor.putBoolean("voice_fishing", false);
                            editor.commit();

                            voiceSettingLayout.setVisibility(View.GONE);


                            Toast.makeText(SettingActivity.this, "VoiceFishing Off", Toast.LENGTH_SHORT).show();

                        }
                        break;

                    // 스미싱 파워 체크
                    case R.id.smishing_Power:
                        // default 값으로 우선 전부 기능 꺼져있게 만듬
                        if(isChecked){
                            editor.putBoolean("sms", false);
                            editor.putBoolean("mms", false);
                            editor.putBoolean("smishing", true);
                            editor.commit();

                            smsPower.setChecked(false);
                            mmsPower.setChecked(false);

                            smishingLayout.setVisibility(View.VISIBLE);

                            Toast.makeText(SettingActivity.this, "smishing On", Toast.LENGTH_SHORT).show();
                        } else{
                            editor.putBoolean("sms", false);
                            editor.putBoolean("mms", false);
                            editor.putBoolean("smishing", false);
                            editor.commit();

                            smsPower.setChecked(false);
                            mmsPower.setChecked(false);

                            smishingLayout.setVisibility(View.GONE);

                            Toast.makeText(SettingActivity.this, "smishing Off", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    // sms 파워 체크
                    case R.id.sms_Power:
                        if(isChecked){
                            editor.putBoolean("sms", true);
                            editor.commit();
                            Toast.makeText(SettingActivity.this, "sms On", Toast.LENGTH_SHORT).show();
                        } else{
                            editor.putBoolean("sms", false);
                            editor.commit();
                            Toast.makeText(SettingActivity.this, "sms Off", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    // mms 파워 체크
                    case R.id.mms_Power:
                        if(isChecked){
                            editor.putBoolean("mms", true);
                            editor.commit();
                            Toast.makeText(SettingActivity.this, "mms On", Toast.LENGTH_SHORT).show();
                        } else{
                            editor.putBoolean("mms", false);
                            editor.commit();
                            Toast.makeText(SettingActivity.this, "mms Off", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        voicePower.setOnCheckedChangeListener(onCheckedChangeListener);
        smishingPower.setOnCheckedChangeListener(onCheckedChangeListener);
        smsPower.setOnCheckedChangeListener(onCheckedChangeListener);
        mmsPower.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    //연락처 불러오기 버튼 클릭 시 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int textViewNum = sf.getInt("textViewNum", 0);

        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String num = cursor.getString(1);   //1은 번호를 받아옵니다.

            switch(textViewNum) {
                case 1:
                    inputPhoneNum1.setText(num);
                    editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
                    editor.commit();
                    if(!inputPhoneNum1.getText().equals(""))
                    {
                        numberSettingLayout2.setVisibility(View.VISIBLE);
                    }
                    break;

                case 2:
                    inputPhoneNum2.setText(num);
                    editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                    editor.commit();
                    if(!inputPhoneNum2.getText().equals(""))
                    {
                        numberSettingLayout3.setVisibility(View.VISIBLE);
                    }
                    break;

                case 3:
                    inputPhoneNum3.setText(num);
                    editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
                    editor.commit();
                    break;
            }

            cursor.close();
        }

    }
}
