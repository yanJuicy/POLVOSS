package com.polvoss.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.polvoss.R;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    final String TAG = "MainActivity"; // TAG

    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체

    /*RadioGroup radioGroup;
    RadioButton r_btn1, r_btn2, r_btn3, r_btn4;
    Button timeButton;
    Button cancelService;*/

    TextView inputPhoneNum1;         // 보호자 번호
    TextView inputPhoneNum2;         // 보호자 번호
    TextView inputPhoneNum3;         // 보호자 번호
    Button saveBtn;              // 저장 버튼
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;


    //int timeCheckId;    // 설정 시간 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck(); // 권한 체크

        // id 매핑
        SeekBar seekBarTime = findViewById(R.id.main_seekBar);
        final TextView textViewTime = findViewById(R.id.main_textViewTime);
        inputPhoneNum1 = findViewById(R.id.main_phoneNum1);
        inputPhoneNum2 = findViewById(R.id.main_phoneNum2);
        inputPhoneNum3 = findViewById(R.id.main_phoneNum3);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn1 = findViewById(R.id.main_deleteBtn1);
        deleteBtn2 = findViewById(R.id.main_deleteBtn2);
        deleteBtn3 = findViewById(R.id.main_deleteBtn3);

        /*r_btn1 = findViewById(R.id.rg_btn1);
        r_btn2 = findViewById(R.id.rg_btn2);
        r_btn3 = findViewById(R.id.rg_btn3);
        r_btn4 = findViewById(R.id.rg_btn4);
        radioGroup = findViewById(R.id.radioGroup);*/
        //timeButton = findViewById(R.id.buttonSetTime);
        //cancelService = findViewById(R.id.buttonCancelService);



        // DB에서 저장된 설정 시간이 있는 지 확인
        sf = getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체
        //timeCheckId = sf.getInt("timeCheckId", 1);      // DB에 설정 시간이 존재하면 불러옴
        long min = sf.getLong("min", 0);

        // 시크바에 DB에 저장되 있는 값 설정
        seekBarTime.setProgress((int) min);
        textViewTime.setText("Time : " + min + "분");
        Point maxSizePoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
        final int maxX = maxSizePoint.x;

        // 시크바 이동 이벤트
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) { // 시크바 이동 중일 때
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
            public void onStopTrackingTouch(SeekBar seekBar) { // 시크바 이동이 멈췄을 때 DB에 저장
                Log.d("VoiceSetting", "DB 저장 " + seekBar.getProgress());
                long min = seekBar.getProgress();
                editor.putLong("min", min);      // 로컬 DB에 설정 시간 저장
                editor.commit();
            }
        });

        //내 연락처에서 번호 블러오기
        TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                switch (view.getId()) {
                    case R.id.main_phoneNum1 :
                        editor.putInt("textViewNum", 1);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;

                    case R.id.main_phoneNum2 :
                        editor.putInt("textViewNum", 2);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;

                    case R.id.main_phoneNum3 :
                        editor.putInt("textViewNum", 3);
                        editor.commit();
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,1);
                        break ;
                }
            }
        };

        inputPhoneNum1.setOnClickListener(onClickListener);
        inputPhoneNum2.setOnClickListener(onClickListener);
        inputPhoneNum3.setOnClickListener(onClickListener);


        // 저장 버튼 클릭 이벤트
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("phoneNum1", inputPhoneNum1.getText().toString());     // DB에 보호자 번호 저장
                editor.putString("phoneNum2", inputPhoneNum2.getText().toString());     // DB에 보호자 번호 저장
                editor.putString("phoneNum3", inputPhoneNum3.getText().toString());     // DB에 보호자 번호 저장
                editor.commit();
                Toast.makeText(MainActivity.this, "설정 완료", Toast.LENGTH_SHORT).show();
                finish();

                /*// 서비스 시작
                intent = new Intent(MainActivity.this, PhoneManageService.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("timeCheckId", timeCheckId);
                intent.setAction("startForeground");//포그라운드 액션지정
                startService(intent);
*/
                /*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(intent);
                }
                else {    startService(intent);
                }*/
            }
        });


        // 번호 삭제 버튼
        ImageButton.OnClickListener onClickListener2 = new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.main_deleteBtn1 :
                        inputPhoneNum1.setText("");
                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString()); // DB에 공백 저장
                        editor.commit();
                        break ;

                    case R.id.main_deleteBtn2 :
                        inputPhoneNum2.setText("");
                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                        editor.commit();
                        break ;

                    case R.id.main_deleteBtn3 :
                        inputPhoneNum3.setText("");
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());     // DB에 보호자 번호 저장
                        editor.commit();
                        break ;
                }
            }
        };

        deleteBtn1.setOnClickListener(onClickListener2);
        deleteBtn2.setOnClickListener(onClickListener2);
        deleteBtn3.setOnClickListener(onClickListener2);



        /*timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("timeCheckId", timeCheckId);      // 로컬 DB에 설정 시간 번호 저장
                editor.commit();
                Toast.makeText(MainActivity.this, "시간 선택 완료", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_btn1:
                        timeCheckId = 1;
                        break;
                    case R.id.rg_btn2:
                        timeCheckId = 2;
                        break;
                    case R.id.rg_btn3:
                        timeCheckId = 3;
                        break;
                    case R.id.rg_btn4:
                        timeCheckId = 4;
                        break;
                }
            }
        });*/

        /*// 서비스 종료
        cancelService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null) {
                    Toast.makeText(MainActivity.this, "서비스 종료료", Toast.LENGTH_SHORT).show();
                    intent.putExtra("stop", true);
                    stopService(intent);
                    intent = null;
                }
            }
        });*/
    }

    //연락처 불러오기 버튼 클릭 시 동작
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int textViewNum = sf.getInt("textViewNum", 0);

        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);  //0은 이름을 얻어옵니다.
            String num = cursor.getString(1);   //1은 번호를 받아옵니다.

            switch(textViewNum) {
                case 1:
                    inputPhoneNum1.setText(num);
                    break;
                case 2:
                    inputPhoneNum2.setText(num);
                    break;
                case 3:
                    inputPhoneNum3.setText(num);
                    break;
            }
            cursor.close();
        }
    }

    private void permissionCheck() {
        AutoPermissions.Companion.loadAllPermissions(this,101); // 권한 설정 오픈소스
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