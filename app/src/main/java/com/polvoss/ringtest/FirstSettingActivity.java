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


public class FirstSettingActivity extends AppCompatActivity {

    //    private TabLayout tabLayout;
//    private ViewPager viewPager;
    int maxX;
    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체
    TextView setNumber;

    /*TextView inputPhoneNum1;
    TextView inputPhoneNum2;
    TextView inputPhoneNum3;
    TextView inputPhoneName1;
    TextView inputPhoneName2;
    TextView inputPhoneName3;*/

    LinearLayout voiceSettingLayout;
    /* LinearLayout numberSettingLayout1;
    LinearLayout numberSettingLayout2;
    LinearLayout numberSettingLayout3;
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;*/
    //Button saveBtn;
    Switch voicePower;
    Switch smishingPower;

    ImageView closeBtn;
    private boolean mIsBound;

    LinearLayout contactLayout1;
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

    LinearLayout protectLayout;
    TextView finishBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);

        sf = getSharedPreferences("settingFile", MODE_PRIVATE);
        editor = sf.edit();

        boolean is_first = sf.getBoolean("is_first", true);

//        if(!is_first){
//            Intent intent = new Intent(getApplicationContext(), DesignActivity.class);
//            startActivity(intent);
//            finish();
//        }

        closeBtn = findViewById(R.id.closeBtn);

        SeekBar seekBarTime = findViewById(R.id.seekBarTime);
        final TextView textViewTime = findViewById(R.id.textViewTime);

        setNumber = findViewById(R.id.setNumber);

        voiceSettingLayout = findViewById(R.id.voice_Setting_Layout);
        /*numberSettingLayout1 = findViewById(R.id.set_Num_Layout1);
        numberSettingLayout2 = findViewById(R.id.set_Num_Layout2);
        numberSettingLayout3 = findViewById(R.id.set_Num_Layout3);*/

        /*inputPhoneName1 = findViewById(R.id.setting_phoneName1);
        inputPhoneName2 = findViewById(R.id.setting_phoneName2);
        inputPhoneName3 = findViewById(R.id.setting_phoneName3);
        inputPhoneNum1 = findViewById(R.id.setting_phoneNum1);
        inputPhoneNum2 = findViewById(R.id.setting_phoneNum2);
        inputPhoneNum3 = findViewById(R.id.setting_phoneNum3);
        deleteBtn1 = findViewById(R.id.deleteBtn1);
        deleteBtn2 = findViewById(R.id.deleteBtn2);
        deleteBtn3 = findViewById(R.id.deleteBtn3);*/
        //saveBtn = findViewById(R.id.saveBtn);

        voicePower = findViewById(R.id.voice_Power);
        smishingPower = findViewById(R.id.smishing_Power);
        //timePickButton = findViewById(R.id.loadTime);

        contactLayout1 = findViewById(R.id.setContactLayout1);
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

        protectLayout = findViewById(R.id.protectorLayout);

        finishBtn = findViewById(R.id.btn_finish);


        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        String phoneName1 = sf.getString("phoneName1", "");
        String phoneName2 = sf.getString("phoneName2", "");
        String phoneName3 = sf.getString("phoneName3", "");
        String phoneNum1 = sf.getString("phoneNum1", "");
        String phoneNum2 = sf.getString("phoneNum2", "");
        String phoneNum3 = sf.getString("phoneNum3", "");

        /*inputPhoneName1.setText(phoneName1);
        inputPhoneName2.setText(phoneName2);
        inputPhoneName3.setText(phoneName3);
        inputPhoneNum1.setText(phoneNum1);
        inputPhoneNum2.setText(phoneNum2);
        inputPhoneNum3.setText(phoneNum3);

        numberSettingLayout1.setVisibility(View.VISIBLE);


        if (!inputPhoneNum1.getText().equals(""))  // 1번이 들어있으면
        {
            if (!inputPhoneNum2.getText().equals("")) // 2번이 들어있으면
            {
                numberSettingLayout3.setVisibility(View.VISIBLE);
            }
            numberSettingLayout2.setVisibility(View.VISIBLE);
        }*/

        /************************************
         * 초기 세팅 보이스피싱, 스미싱 파워 On
         ***********************************/
        editor.putBoolean("voice_fishing", true);
        editor.putBoolean("smishing", true);
        editor.commit();


        /**
         보호자 연락처 추가
         */

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
        });

        /******************************************
         * 확인 버튼 눌렀을때
         *****************************************/

        finishBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (sf.getBoolean("contact", false)) {    //연락처 1이 저장이 되어있을때
                     editor.putBoolean("is_first", false);
                     editor.commit();
                     Intent intent = new Intent(getApplicationContext(), DesignActivity.class);
                     startActivity(intent);
                     finish();
                 } else {   //연락처 1이 저장이 안되어있을때
                     Toast.makeText(FirstSettingActivity.this, "연락처를 설정해 주세요", Toast.LENGTH_SHORT).show();
                 }
             }
        });

        /*******************************************
         * 보이스 피싱 파워
         ******************************************/
        boolean voiceFishing = sf.getBoolean("voice_fishing", false);
        if (voiceFishing) {
            voiceSettingLayout.setVisibility(View.VISIBLE);
            voicePower.setChecked(true);
        } else {
            voiceSettingLayout.setVisibility(View.GONE);
            voicePower.setChecked(false);
        }


        /*******************************************
         * 스미싱 버튼 확인부분
         ******************************************/
        boolean smishing = sf.getBoolean("smishing", false);
        if (smishing) {
            smishingPower.setChecked(true);
        } else {
            smishingPower.setChecked(false);
        }

        /*******************************************
         * 시간 설정, text가 범위 초과하는 현상 발생
         *******************************************/
        long min = sf.getLong("min", 10);

        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(4);
        numberPicker.setMinValue(2);
        numberPicker.setValue((int) min / 5);

        String[] values = new String[3];
        for (int i = 0; i < 3; i++) {
            values[i] = String.valueOf((i+2) * 5);
        }
        numberPicker.setDisplayedValues(values);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Toast.makeText(SettingActivity.this, numberPicker.getValue() + "", Toast.LENGTH_SHORT).show();
                long min = numberPicker.getValue() * 5;
                Log.d("SettingActivity", ""+min);
                editor.putLong("min", min);
                editor.commit();
            }
        });

       /* textViewTime.setText("Time : " + seekBarTime.getProgress());
        Point maxSizePoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
        maxX = maxSizePoint.x;

        textViewTime.setText("Time : " + min + "분");

        seekBarTime.setProgress((int) min);
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                int val = (progressValue * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewTime.setText("Time : " + progressValue + "분");
                int textViewX = val - (textViewTime.getWidth() / 2);
                int finalX = textViewTime.getWidth() + textViewX > maxX ? (maxX - textViewTime.getWidth() - 16) : textViewX + 16 *//*your margin*//*;
                textViewTime.setX(finalX < 0 ? 16*//*your margin*//* : finalX);
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
*/

        this.TextClickListener();
        //this.SetNumberClickListener();
        //this.ButtonClickListener();
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
         <<<<<<< Updated upstream
         @Override public void onTabSelected(TabLayout.Tab tab) {
         viewPager.setCurrentItem(tab.getPosition());
         }

         @Override public void onTabUnselected(TabLayout.Tab tab) {

         }

         @Override public void onTabReselected(TabLayout.Tab tab) {
         =======
         @Override public void onTabSelected(TabLayout.Tab tab) {
         viewPager.setCurrentItem(tab.getPosition());
         }

         @Override public void onTabUnselected(TabLayout.Tab tab) {

         }

         @Override public void onTabReselected(TabLayout.Tab tab) {
         >>>>>>> Stashed changes

         }
         });
         ************/

    }

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
    }
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
                }
            }
        };

        closeBtn.setOnClickListener(Listener);
        setNumber.setOnClickListener(Listener);
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
        textViews[i].setText("");
    }

    /*******************************************
     * 보호자 전화번호 설정 클릭 이벤트
     *******************************************/
//    public void SetNumberClickListener() {
//        View.OnClickListener Listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//
//                switch (view.getId()) {
//
//                    // 번호 설정
//
//                    case R.id.setting_phoneNum1:
//                    case R.id.setting_phoneName1:
//
//                        editor.putInt("textViewNum", 1);
//                        editor.commit();
//                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                        startActivityForResult(intent, 1);
//                        break;
//
//                    case R.id.setting_phoneName2:
//                    case R.id.setting_phoneNum2:
//                        editor.putInt("textViewNum", 2);
//                        editor.commit();
//                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                        startActivityForResult(intent, 1);
//                        break;
//
//                    case R.id.setting_phoneName3:
//                    case R.id.setting_phoneNum3:
//                        editor.putInt("textViewNum", 3);
//                        editor.commit();
//                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                        startActivityForResult(intent, 1);
//                        break;
//                }
//            }
//        };
//        /*inputPhoneName1.setOnClickListener(Listener);
//        inputPhoneName2.setOnClickListener(Listener);
//        inputPhoneName3.setOnClickListener(Listener);
//        inputPhoneNum1.setOnClickListener(Listener);
//        inputPhoneNum2.setOnClickListener(Listener);
//        inputPhoneNum3.setOnClickListener(Listener);*/
//    }

    /*******************************************
     * 전화번호 저장 및 삭제 클릭 이벤트
     *******************************************/
//    public void ButtonClickListener() {
//        View.OnClickListener Listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switch (view.getId()) {
//
//                    // 저장 버튼
////                    case R.id.saveBtn:
////                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
////                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
////                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
////                        editor.commit();
////                        Toast.makeText(SettingActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
////
////                        break;
//
//                    // 번호 삭제
//                    case R.id.deleteBtn1:
//                        inputPhoneName1.setText("");
//                        inputPhoneNum1.setText("");
//                        if (!inputPhoneNum3.getText().equals("")) // 1, 2, 3 모두 있는 상태
//                        {
//                            inputPhoneName1.setText(inputPhoneName2.getText());
//                            inputPhoneNum1.setText(inputPhoneNum2.getText());
//                            inputPhoneName2.setText(inputPhoneName3.getText());
//                            inputPhoneNum2.setText(inputPhoneNum3.getText());
//                            inputPhoneName3.setText("");
//                            inputPhoneNum3.setText("");
//                        } else if (!inputPhoneNum2.getText().equals("")) // 1, 2가 있는 상태
//                        {
//                            inputPhoneName1.setText(inputPhoneName2.getText());
//                            inputPhoneNum1.setText(inputPhoneNum2.getText());
//                            inputPhoneName2.setText("");
//                            inputPhoneNum2.setText("");
//                            numberSettingLayout3.setVisibility(View.GONE);
//                        } else { // 1만 있는 상태
//                            numberSettingLayout2.setVisibility(View.GONE);
//                        }
//
//                        editor.putString("phoneName1", inputPhoneName1.getText().toString());
//                        editor.putString("phoneName2", inputPhoneName2.getText().toString());
//                        editor.putString("phoneName3", inputPhoneName3.getText().toString());
//                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
//                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
//                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
//                        editor.commit();
//                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.deleteBtn2:
//                        inputPhoneName2.setText("");
//                        inputPhoneNum2.setText("");
//                        if (!inputPhoneNum3.getText().equals("")) // 1, 2, 3 모두 있는 상태
//                        {
//                            inputPhoneName2.setText(inputPhoneName3.getText());
//                            inputPhoneNum2.setText(inputPhoneNum3.getText());
//                            inputPhoneName3.setText("");
//                            inputPhoneNum3.setText("");
//                        } else {
//                            numberSettingLayout3.setVisibility(View.GONE);
//                        }
//
//                        editor.putString("phoneName2", inputPhoneName2.getText().toString());
//                        editor.putString("phoneName3", inputPhoneName3.getText().toString());
//                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
//                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
//                        editor.commit();
//                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.deleteBtn3:
//                        inputPhoneName3.setText("");
//                        inputPhoneNum3.setText("");
//                        editor.putString("phoneName3", inputPhoneName3.getText().toString());
//                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
//                        editor.commit();
//                        Toast.makeText(SettingActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        };
//        //saveBtn.setOnClickListener(Listener);
//        deleteBtn1.setOnClickListener(Listener);
//        deleteBtn2.setOnClickListener(Listener);
//        deleteBtn3.setOnClickListener(Listener);
//
//
//    }
    public void SwitchCheckedListener() {
        // 보이스피싱 파워 체크
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {

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
                        break;

                    // 스미싱 파워 체크
                    case R.id.smishing_Power:
                        // default 값으로 우선 전부 기능 꺼져있게 만듬
                        if (isChecked) {
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
        voicePower.setOnCheckedChangeListener(onCheckedChangeListener);
        smishingPower.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    //연락처 불러오기 버튼 클릭 시 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int textViewNum = sf.getInt("textViewNum", 0);
        String[] names = new String[3];
        names[0] = sf.getString("contactName1", "");
        names[1] = sf.getString("contactName2", "");
        names[2] = sf.getString("contactName3", "");

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


        }

//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Cursor cursor = getContentResolver().query(data.getData(),
//                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
//            cursor.moveToFirst();
//            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
//            String num = cursor.getString(1);   //1은 번호를 받아옵니다.
//
//            switch (textViewNum) {
//                case 1:
//                    inputPhoneName1.setText(name);
//                    inputPhoneNum1.setText(num);
//                    editor.putString("phoneName1", inputPhoneName1.getText().toString());
//                    editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
//                    editor.commit();
//                    if (!inputPhoneNum1.getText().equals("")) {
//                        numberSettingLayout2.setVisibility(View.VISIBLE);
//                    }
//                    break;
//
//                case 2:
//                    inputPhoneName2.setText(name);
//                    inputPhoneNum2.setText(num);
//                    editor.putString("phoneName2", inputPhoneName2.getText().toString());
//                    editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
//                    editor.commit();
//                    if (!inputPhoneNum2.getText().equals("")) {
//                        numberSettingLayout3.setVisibility(View.VISIBLE);
//                    }
//                    break;
//
//                case 3:
//                    inputPhoneName3.setText(name);
//                    inputPhoneNum3.setText(num);
//                    editor.putString("phoneName3", inputPhoneName3.getText().toString());
//                    editor.putString("phoneNum3", inputPhoneNum3.getText().toString());
//                    editor.commit();
//                    break;
//            }
//
//            cursor.close();
//        }


    }
}