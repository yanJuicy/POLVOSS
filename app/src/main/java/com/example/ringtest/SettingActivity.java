package com.example.ringtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
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
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;
    Button saveBtn;
    Switch voicePower;
    Switch smishingPower;
    Switch smsPower;
    Switch mmsPower;

    ImageView closeBtn;

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
        inputPhoneNum1 = findViewById(R.id.setting_phoneNum1);
        inputPhoneNum2 = findViewById(R.id.setting_phoneNum2);
        inputPhoneNum3 = findViewById(R.id.setting_phoneNum3);
        deleteBtn1 = findViewById(R.id.deleteBtn1);
        deleteBtn2 = findViewById(R.id.deleteBtn2);
        deleteBtn3 = findViewById(R.id.deleteBtn3);
        saveBtn = findViewById(R.id.saveBtn);

        voicePower = findViewById(R.id.voice_Power);
        smishingPower = findViewById(R.id.smishing_Power);
        smsPower = findViewById(R.id.smsPower);
        mmsPower = findViewById(R.id.mmsPower);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        String phoneNum1 = sf.getString("phoneNum1", "");
        String phoneNum2 = sf.getString("phoneNum2", "");
        String phoneNum3 = sf.getString("phoneNum3", "");

        inputPhoneNum1.setText(phoneNum1);
        inputPhoneNum2.setText(phoneNum2);
        inputPhoneNum3.setText(phoneNum3);

        if(inputPhoneNum1.equals(""))
        {
            inputPhoneNum2.setVisibility(View.GONE);
            inputPhoneNum3.setVisibility(View.GONE);
        }

        else
        {
            if(inputPhoneNum2.equals(""))
            {
                inputPhoneNum3.setVisibility(View.GONE);
            }
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
                        Toast.makeText(SettingActivity.this, "버전 정보 클릭", Toast.LENGTH_SHORT).show();
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

                        if(inputPhoneNum1.equals(""))
                        {
                            inputPhoneNum2.setVisibility(View.GONE);
                        }
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn2 :
                        inputPhoneNum2.setText("");

                        if(inputPhoneNum2.equals(""))
                        {
                            inputPhoneNum3.setVisibility(View.GONE);
                        }
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn3 :
                        inputPhoneNum3.setText("");
                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;
                }
            }
        };
        saveBtn.setOnClickListener(Listener);
        deleteBtn1.setOnClickListener(Listener);
        deleteBtn2.setOnClickListener(Listener);
        deleteBtn3.setOnClickListener(Listener);
    }

    public void SwitchCheckedListener()
    {
        // 보이스피싱 파워 체크
        voicePower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    voiceSettingLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    voiceSettingLayout.setVisibility(View.GONE);
                }

            }
        });
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

                    if(!inputPhoneNum1.equals(""))
                    {
                        inputPhoneNum2.setVisibility(View.VISIBLE);
                    }
                    break;

                case 2:
                    inputPhoneNum2.setText(num);
                    if(!inputPhoneNum2.equals(""))
                    {
                        inputPhoneNum3.setVisibility(View.VISIBLE);
                    }
                    break;

                case 3:
                    inputPhoneNum3.setText(num);
                    break;
            }

            cursor.close();
        }

    }
}
