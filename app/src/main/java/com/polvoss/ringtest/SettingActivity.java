package com.polvoss.ringtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;


public class SettingActivity extends AppCompatActivity {

    final int KAKAO_CHECK = 101;

    //    private TabLayout tabLayout;
//    private ViewPager viewPager;
    int maxX;
    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체
    //TextView setNumber;
    TextView version;
    TextView osLicense;
    /*TextView inputPhoneNum1;
    TextView inputPhoneNum2;
    TextView inputPhoneNum3;
    TextView inputPhoneName1;
    TextView inputPhoneName2;
    TextView inputPhoneName3;*/

    //LinearLayout voiceSettingLayout;
    /* LinearLayout numberSettingLayout1;
    LinearLayout numberSettingLayout2;
    LinearLayout numberSettingLayout3;
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;*/
    //Button saveBtn;
    //Switch voicePower;
    Switch smishingPower;

    Switch kakaoPower;

    ImageView closeBtn;
    private boolean mIsBound;

    boolean isPermissionAllowd;

    /*LinearLayout contactLayout1;
    ImageButton contactButton1;
    TextView contactName1;
    TextView contactPhone1;

    LinearLayout contactLayout2;
    ImageButton contactButton2;
    TextView contactName2;
    TextView contactPhone2;

    LinearLayout contactLayout3;
    ImageButton contactButton3;
    TextView contactName3;
    TextView contactPhone3;

    LinearLayout contactLayout4;
    ImageButton contactButton4;
    TextView contactName4;
    TextView contactPhone4;

    LinearLayout contactLayout5;
    ImageButton contactButton5;
    TextView contactName5;
    TextView contactPhone5;

    LinearLayout protectLayout;*/

    LinearLayout etcLayout;
    TextView setButton;
    LinearLayout testLayout;
    TextView testButton;
    boolean is_first;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        closeBtn = findViewById(R.id.closeBtn);

        SeekBar seekBarTime = findViewById(R.id.seekBarTime);
        final TextView textViewTime = findViewById(R.id.textViewTime);

        // setNumber = findViewById(R.id.setNumber);
        version = findViewById(R.id.version_Info);
        //osLicense = findViewById(R.id.OS_License);

        //voiceSettingLayout = findViewById(R.id.voice_Setting_Layout);

        //voicePower = findViewById(R.id.voice_Power);
        smishingPower = findViewById(R.id.smishing_Power);
        kakaoPower = findViewById(R.id.kakaotalk_Power);

        /*contactLayout1 = findViewById(R.id.setContactLayout1);
        contactButton1 = findViewById(R.id.contactButton1);
        contactName1 = findViewById(R.id.contactName1);
        contactPhone1 = findViewById(R.id.contatctPhone1);

        contactLayout2 = findViewById(R.id.setContactLayout2);
        contactButton2 = findViewById(R.id.contactButton2);
        contactName2 = findViewById(R.id.contactName2);
        contactPhone2 = findViewById(R.id.contatctPhone2);

        contactLayout3 = findViewById(R.id.setContactLayout3);
        contactButton3 = findViewById(R.id.contactButton3);
        contactName3 = findViewById(R.id.contactName3);
        contactPhone3 = findViewById(R.id.contatctPhone3);

        contactLayout4 = findViewById(R.id.setContactLayout4);
        contactButton4 = findViewById(R.id.contactButton4);
        contactName4 = findViewById(R.id.contactName4);
        contactPhone4 = findViewById(R.id.contatctPhone4);

        contactLayout5 = findViewById(R.id.setContactLayout5);
        contactButton5 = findViewById(R.id.contactButton5);
        contactName5 = findViewById(R.id.contactName5);
        contactPhone5 = findViewById(R.id.contatctPhone5);

        protectLayout = findViewById(R.id.protectorLayout);*/

        etcLayout = findViewById(R.id.etc_layout);
        setButton = findViewById(R.id.btn_set);

        testLayout = findViewById(R.id.test_layout);
        testButton = findViewById(R.id.btn_test);


        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        /**
         * 앱 초기 실행시 설정 창
         */

        is_first = sf.getBoolean("is_first", true);

        if (is_first) {
            etcLayout.setVisibility(View.GONE);
            testLayout.setVisibility(View.GONE);
        } else {
            etcLayout.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.GONE);
            testLayout.setVisibility(View.VISIBLE);
        }

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSettingFinish()) {
                    editor.putBoolean("is_first", false);
                    editor.commit();
                } else {
                    Toast.makeText(SettingActivity.this, "보이스피싱 기능을 사용하려면\n먼저 보호자 연락처를 설정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(SettingActivity.this, DesignActivity.class));
                finish();
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(SettingActivity.this, TestService.class));
                Toast.makeText(SettingActivity.this, "테스트 시작", Toast.LENGTH_SHORT).show();

            }
        });


/*
        *//*****
         * 보호자 연락처 설정
         *****//*

        String[] names = new String[5];
        String[] phones = new String[5];

        for (int i = 0; i < 5; i++) {
            names[i] = sf.getString("contactName" + (i + 1), "");
            phones[i] = sf.getString("contactPhone" + (i + 1), "");
        }

        if (!names[0].equals("")) {
            contactName1.setText(names[0]);
            contactPhone1.setText(phones[0]);
            contactButton1.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);
            editor.putBoolean("contact", true);
            contactLayout2.setVisibility(View.VISIBLE);
        }
        if (!names[1].equals("")) {
            contactName2.setText(names[1]);
            contactPhone2.setText(phones[1]);
            contactButton2.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);
            contactLayout3.setVisibility(View.VISIBLE);
            editor.putBoolean("contact2", true);
        }
        if (!names[2].equals("")) {
            contactName3.setText(names[2]);
            contactPhone3.setText(phones[2]);
            contactButton3.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);
            contactLayout4.setVisibility(View.VISIBLE);
            editor.putBoolean("contact3", true);
        }
        if (!names[3].equals("")) {
            contactName4.setText(names[3]);
            contactPhone4.setText(phones[3]);
            contactButton4.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);
            contactLayout5.setVisibility(View.VISIBLE);
            editor.putBoolean("contact4", true);
        }
        if (!names[4].equals("")) {
            contactName5.setText(names[4]);
            contactPhone5.setText(phones[4]);
            contactButton5.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);
            editor.putBoolean("contact5", true);
        }

        contactButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getBoolean("contact", false)) {
                    if (!contactName5.getText().equals("") && !contactName4.getText().equals("")) {
                        delContactName(0, contactName1, contactName2, contactName3, contactName4, contactName5);
                        delContactPhone(0, contactPhone1, contactPhone2, contactPhone3, contactPhone4, contactPhone5);
                        contactButton5.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact5", false);
                    } else if (!contactName4.getText().equals("") && !contactName3.getText().equals("")) {
                        delContactName(0, contactName1, contactName2, contactName3, contactName4);
                        delContactPhone(0, contactPhone1, contactPhone2, contactPhone3, contactPhone4);
                        contactButton4.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact4", false);
                    } else if (!contactName3.getText().equals("") && !contactName2.getText().equals("")) {
                        delContactName(0, contactName1, contactName2, contactName3);
                        delContactPhone(0, contactPhone1, contactPhone2, contactPhone3);
                        contactButton3.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact3", false);
                    } else if (!contactName2.getText().equals("")) {
                        delContactName(0, contactName1, contactName2);
                        delContactPhone(0, contactPhone1, contactPhone2);
                        editor.putBoolean("contact2", false);
                        contactButton2.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);

                    } else {
                        delContactName(0, contactName1);
                        delContactPhone(0, contactPhone1);
                        contactButton1.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact", false);
                    }

                    if (contactName2.getText().equals(""))
                        contactLayout3.setVisibility(View.GONE);

                    editor.commit();
                    afterContactLayoutDelete();


                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 2);
                    editor.putInt("textViewNum", 1);
                    editor.commit();
                }
            }
        });

        contactButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getBoolean("contact2", false)) {
                    if (!contactName5.getText().equals("")) {
                        delContactName(1, contactName2, contactName3, contactName4, contactName5);
                        delContactPhone(1, contactPhone2, contactPhone3, contactPhone4, contactPhone5);
                        contactButton5.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact5", false);

                    } else if (!contactName4.getText().equals("")) {
                        delContactName(1, contactName2, contactName3, contactName4);
                        delContactPhone(1, contactPhone2, contactPhone3, contactPhone4);
                        contactButton4.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact4", false);

                    } else if (!contactName3.getText().equals("")) {
                        delContactName(1, contactName2, contactName3);
                        delContactPhone(1, contactPhone2, contactPhone3);
                        contactButton3.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact3", false);

                    } else {
                        delContactName(1, contactName2);
                        delContactPhone(1, contactPhone2);
                        contactButton2.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        editor.putBoolean("contact2", false);
                    }
                    editor.commit();

                    afterContactLayoutDelete();

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 2);
                    editor.putInt("textViewNum", 2);
                    editor.commit();
                }
            }
        });

        contactButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getBoolean("contact3", false)) {

                    if (!contactName5.getText().equals("")) {
                        delContactName(2, contactName3, contactName4, contactName5);
                        delContactPhone(2, contactPhone3, contactPhone4, contactPhone5);
                        editor.putBoolean("contact5", false);
                        contactButton5.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                    } else if (!contactName4.getText().equals("")) {
                        delContactName(2, contactName3, contactName4);
                        delContactPhone(2, contactPhone3, contactPhone4);
                        editor.putBoolean("contact4", false);
                        contactButton4.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                    } else {
                        delContactName(2, contactName3);
                        delContactPhone(2, contactPhone3);
                        editor.putBoolean("contact3", false);
                        contactButton3.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                    }

                    editor.commit();
                    afterContactLayoutDelete();

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 2);
                    editor.putInt("textViewNum", 3);
                    editor.commit();
                }
            }
        });

        contactButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getBoolean("contact4", false)) {

                    if (!contactName5.getText().equals("")) {
                        delContactName(3, contactName4, contactName5);
                        delContactPhone(3, contactPhone4, contactPhone5);
                        editor.putBoolean("contact5", false);
                        contactButton5.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                    } else {
                        delContactName(3, contactName4);
                        delContactPhone(3, contactPhone4);
                        editor.putBoolean("contact4", false);
                        contactButton4.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                        //contactLayout5.setVisibility(View.GONE);
                    }

                    editor.commit();
                    afterContactLayoutDelete();

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 2);
                    editor.putInt("textViewNum", 4);
                    editor.commit();
                }
            }
        });

        contactButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getBoolean("contact5", false)) {
                    delContactName(4, contactName5);
                    delContactPhone(4, contactPhone5);
                    editor.putBoolean("contact5", false);
                    contactButton5.setImageResource(R.drawable.ic_baseline_add_circle_outline_35);
                    editor.commit();
                    afterContactLayoutDelete();

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 2);
                    editor.putInt("textViewNum", 5);
                    editor.commit();
                }
            }
        });*/

        /*
         *****************************************
         * 보이스 피싱 파워
         *****************************************
        boolean voiceFishing = sf.getBoolean("voice_fishing", false);
        if (voiceFishing) {
            voiceSettingLayout.setVisibility(View.VISIBLE);
            voicePower.setChecked(true);
        } else {
            voiceSettingLayout.setVisibility(View.GONE);
            voicePower.setChecked(false);
        }
        */

        /*******************************************
         * 스미싱 버튼 확인부분
         ******************************************/
        boolean smishing = sf.getBoolean("smishing", false);
        if (smishing) {
            smishingPower.setChecked(true);
        } else {
            smishingPower.setChecked(false);
        }


        /***************************************
        * 카카오 버튼 확인부분
        *******************************************/
        isPermissionAllowd = isNotPermissionAllowed();

        final boolean kakaoCheck = sf.getBoolean("kakaoCheck", false);
        //Toast.makeText(SettingActivity.this, kakaoCheck + " " + isPermissionAllowd, Toast.LENGTH_SHORT).show();

        if (isPermissionAllowd && kakaoCheck) {
            kakaoPower.setChecked(true);
        } else {
            kakaoPower.setChecked(false);
        }
        final Intent listenerIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");

        kakaoPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(SettingActivity.this, "true", Toast.LENGTH_SHORT).show();
                    if (!isPermissionAllowd)
                        startActivityForResult(listenerIntent, KAKAO_CHECK);
                    else {
                        editor.putBoolean("kakaoCheck", true);
                        editor.commit();
                    }
                } else {
                    //Toast.makeText(SettingActivity.this, "false", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("kakaoCheck", false);
                    editor.commit();
                }
            }
        });

        /*******************************************
         * 시간 설정, NumberPicker로 변경
         * -> 5분, 10분, 15분으로 변경
         *******************************************/
        long min = sf.getLong("min", 10);
        int maxValue = 20;
        int minValue = 10;
        int step = 5;

        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(maxValue/step);
        numberPicker.setMinValue(minValue/step);
        numberPicker.setValue((int) min / step);

        int size = (maxValue/step) - (minValue/step) + 1;
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = String.valueOf((i+(minValue/step)) * step);
        }
        numberPicker.setDisplayedValues(values);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Toast.makeText(SettingActivity.this, numberPicker.getValue() + "", Toast.LENGTH_SHORT).show();
                long min = numberPicker.getValue() * 5;
                Log.d("SettingActivity", ""+min+" 분 설정");
                editor.putLong("min", min);
                editor.commit();
            }
        });

        this.TextClickListener();
        this.SwitchCheckedListener();



    }

    private boolean checkSettingFinish() {
        boolean isVoiceCheck = sf.getBoolean("voice_fishing", false);
        String protector = sf.getString("contactName1", "");
        if (isVoiceCheck && protector.equals("")) {
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("SettingActivity ",  checkSettingFinish() + " " + is_first);

        if (checkSettingFinish() || is_first)
            super.onBackPressed();
        else
            Toast.makeText(this, "보이스피싱 기능을 사용하려면\n 먼저 보호자 연락처를 설정해주세요.", Toast.LENGTH_SHORT).show();
    }

    private boolean isNotPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        for(String packageName : notiListenerSet) {
            if(packageName == null) {
                continue;
            }
            if(packageName.equals(myPackageName)) {
                return true;
            }
        }

        return false;
    }
/*
    private void afterContactLayoutDelete() {
        String[] names = new String[5];

        int start = 0;
        for (int i = 0; i < 5; i++) {
            names[i] = sf.getString("contactName" + (i + 1), "");
            if (names[i].equals("")) {
                start = i + 1;
                break;
            }
        }

        switch (start) {
            case 1:
                contactLayout2.setVisibility(View.GONE);
                contactLayout3.setVisibility(View.GONE);
                contactLayout4.setVisibility(View.GONE);
                contactLayout5.setVisibility(View.GONE);
                break;
            case 2:
                contactLayout3.setVisibility(View.GONE);
                contactLayout4.setVisibility(View.GONE);
                contactLayout5.setVisibility(View.GONE);
                break;
            case 3:
                contactLayout4.setVisibility(View.GONE);
                contactLayout5.setVisibility(View.GONE);
                break;
            case 4:
                contactLayout5.setVisibility(View.GONE);
                break;
        }
        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }*/
    /*******************************************
     * NumberPicker 불러오기
     **********************************************//*
    private void getNumberPicker() {
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }*/

    /*******************************************
     * 설정창 종료, 텍스트 클릭 이벤트
     *******************************************/
    public void TextClickListener() {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.closeBtn:
                        //Toast.makeText(SettingActivity.this, "종료 누름", Toast.LENGTH_SHORT).show();
                        if (checkSettingFinish() || is_first)
                            finish();
                        else
                            Toast.makeText(SettingActivity.this, "보이스피싱 기능을 사용하려면\n 먼저 보호자 연락처를 설정해주세요.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SettingActivity.this, "Version 3.1.31", Toast.LENGTH_SHORT).show();
                        break;

/*                    // OS 라이센스 정보
                    case R.id.OS_License:
                        Toast.makeText(SettingActivity.this, "OS 클릭", Toast.LENGTH_SHORT).show();
                        break;*/
                }
            }
        };

        closeBtn.setOnClickListener(Listener);
        //setNumber.setOnClickListener(Listener);
        version.setOnClickListener(Listener);
        //osLicense.setOnClickListener(Listener);
    }

    private void delContactName(int idx, TextView... textViews) {
        int i = 0;
        int size = textViews.length;
        for (i = 0; i < size - 1; i++) {
            TextView curName = textViews[i];
            TextView nextName = textViews[i + 1];
            editor.putString("contactName" + (idx + i + 1), nextName.getText().toString());
            curName.setText(nextName.getText().toString());
        }
        editor.putString("contactName" + (idx + i + 1), "");
        textViews[i].setText("");
    }

    private void delContactPhone(int idx, TextView... textViews) {
        int i = 0;
        int size = textViews.length;
        for (i = 0; i < size - 1; i++) {
            TextView curPhone = textViews[i];
            TextView nextPhone = textViews[i + 1];
            editor.putString("contactPhone" + (idx + i + 1), nextPhone.getText().toString());
            curPhone.setText(nextPhone.getText().toString());
        }
        editor.putString("contactPhone" + (idx + i + 1), "");
        textViews[i].setText("");
    }

    public void SwitchCheckedListener() {
        // 보이스피싱 파워 체크
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
/*
                    // 보이스피싱 파워 체크
                    case R.id.voice_Power:
                        if (isChecked) {
                            editor.putBoolean("voice_fishing", true);
                            editor.commit();

                            voiceSettingLayout.setVisibility(View.VISIBLE);

                            //Toast.makeText(SettingActivity.this, "VoiceFishing On", Toast.LENGTH_SHORT).show();
                        } else {
                            editor.putBoolean("voice_fishing", false);
                            editor.commit();

                            voiceSettingLayout.setVisibility(View.GONE);
                        }
                        break;*/

                    // 스미싱 파워 체크
                    case R.id.smishing_Power:
                        // default 값으로 우선 전부 기능 꺼져있게 만듬
                        if (isChecked) {
                            editor.putBoolean("smishing", true);
                            editor.putBoolean("smishing", true);
                            editor.commit();
                            //Toast.makeText(SettingActivity.this, "smishing On", Toast.LENGTH_SHORT).show();
                        } else {
                            editor.putBoolean("smishing", false);
                            editor.commit();

                            //Toast.makeText(SettingActivity.this, "smishing Off", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        // voicePower.setOnCheckedChangeListener(onCheckedChangeListener);
        smishingPower.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    //연락처 불러오기 버튼 클릭 시 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*int textViewNum = sf.getInt("textViewNum", 0);
        String[] names = new String[5];
        for (int i=0; i<5; i++) {
            names[i] = sf.getString("contactName"+(i+1), "");
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String num = cursor.getString(1);   //1은 번호를 받아옵니다.

            for (int i = 0; i < 3; i++) {
                if (name.equals(names[i])) {
                    Toast.makeText(this, "중복되지 않는 연락처를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (textViewNum == 1) {
                contactName1.setText(name);
                contactPhone1.setText(num);
                contactButton1.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);

                editor.putString("contactName1", name);
                editor.putString("contactPhone1", num);     // DB에 보호자 번호 저장
                editor.putBoolean("contact", true);
                editor.commit();

                if (contactLayout2.getVisibility() == View.GONE)
                    contactLayout2.setVisibility(View.VISIBLE);
            } else if (textViewNum == 2) {
                contactName2.setText(name);
                contactPhone2.setText(num);
                contactButton2.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);

                editor.putString("contactName2", name);
                editor.putString("contactPhone2", num);     // DB에 보호자 번호 저장
                editor.putBoolean("contact2", true);
                editor.commit();

                if (contactLayout3.getVisibility() == View.GONE)
                    contactLayout3.setVisibility(View.VISIBLE);
            } else if (textViewNum == 3) {
                contactName3.setText(name);
                contactPhone3.setText(num);
                contactButton3.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);

                editor.putString("contactName3", name);
                editor.putString("contactPhone3", num);     // DB에 보호자 번호 저장
                editor.putBoolean("contact3", true);
                editor.commit();

                if (contactLayout4.getVisibility() == View.GONE)
                    contactLayout4.setVisibility(View.VISIBLE);
            } else if (textViewNum == 4) {
                contactName4.setText(name);
                contactPhone4.setText(num);
                contactButton4.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);

                editor.putString("contactName4", name);
                editor.putString("contactPhone4", num);     // DB에 보호자 번호 저장
                editor.putBoolean("contact4", true);
                editor.commit();

                if (contactLayout5.getVisibility() == View.GONE)
                    contactLayout5.setVisibility(View.VISIBLE);
            } else {
                contactName5.setText(name);
                contactPhone5.setText(num);
                contactButton5.setImageResource(R.drawable.ic_baseline_remove_circle_outline_35);

                editor.putString("contactName5", name);
                editor.putString("contactPhone5", num);     // DB에 보호자 번호 저장
                editor.putBoolean("contact5", true);
                editor.commit();
            }
        }*/

        if (requestCode == KAKAO_CHECK) {
            //Toast.makeText(this, "노티피 결과", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
            isPermissionAllowd = isNotPermissionAllowed();
            if (isPermissionAllowd) {
                //Toast.makeText(this, "카카오 설정", Toast.LENGTH_SHORT).show();
                kakaoPower.setChecked(true);
                editor.putBoolean("kakaoCheck", true);
            } else {
                Toast.makeText(this, "카카오톡 URL 검사를 하기 위해서\n 폴보스에 권한을 추가해주세요.", Toast.LENGTH_LONG).show();
                editor.putBoolean("kakaoCheck", false);
                kakaoPower.setChecked(false);
            }
            editor.commit();
        }


    }
}