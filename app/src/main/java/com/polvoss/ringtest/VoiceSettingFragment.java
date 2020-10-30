package com.polvoss.ringtest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class VoiceSettingFragment extends Fragment {

    int maxX;
    SharedPreferences sf;               // 로컬 DB
    SharedPreferences.Editor editor;    // DB 편집 객체
    TextView setNumber;
    TextView version;
    TextView osLicense;
    TextView inputPhoneNum1;
    TextView inputPhoneNum2;
    TextView inputPhoneNum3;
    LinearLayout layout;
    ImageButton deleteBtn1;
    ImageButton deleteBtn2;
    ImageButton deleteBtn3;
    Button saveBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                            @Nullable ViewGroup Container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_voice, null);

        SeekBar seekBarTime = view.findViewById(R.id.seekBarTime);
        final TextView textViewTime = view.findViewById(R.id.textViewTime);

        setNumber = view.findViewById(R.id.setNumber);
        version = view.findViewById(R.id.version_Info);
        osLicense = view.findViewById(R.id.OS_License);

        layout = view.findViewById(R.id.set_Num_Layout);
        inputPhoneNum1 = view.findViewById(R.id.setting_phoneNum1);
        inputPhoneNum2 = view.findViewById(R.id.setting_phoneNum2);
        inputPhoneNum3 = view.findViewById(R.id.setting_phoneNum3);
        deleteBtn1 = view.findViewById(R.id.deleteBtn1);
        deleteBtn2 = view.findViewById(R.id.deleteBtn2);
        deleteBtn3 = view.findViewById(R.id.deleteBtn3);
        saveBtn = view.findViewById(R.id.saveBtn);

        sf = getContext().getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        String phoneNum1 = sf.getString("phoneNum1", "");
        String phoneNum2 = sf.getString("phoneNum2", "");
        String phoneNum3 = sf.getString("phoneNum3", "");

        inputPhoneNum1.setText(phoneNum1);
        inputPhoneNum2.setText(phoneNum2);
        inputPhoneNum3.setText(phoneNum3);

        /*******************************************
         * 시간 설정, text가 범위 초과하는 현상 발생
         *******************************************/

        textViewTime.setText("Time : " + seekBarTime.getProgress());
        Point maxSizePoint = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
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

        return view;
    }

    /*******************************************
     * 설정창 텍스트 클릭 이벤트
     *******************************************/
    public void TextClickListener()
    {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    // 전화번호 입력 창 레이아웃
                    case R.id.setNumber:
                        Log.e("로그", "0");
                        Toast.makeText(getActivity(), "번호 설정 클릭", Toast.LENGTH_SHORT).show();

                        if(layout.getVisibility()==View.VISIBLE) // 클릭시 레이아웃 Visiability 설정
                        {
                            layout.setVisibility(View.GONE);
                        }

                        else
                        {
                            layout.setVisibility(View.VISIBLE);
                        }
                        break;

                    // 버전 정보
                    case R.id.version_Info:
                        Toast.makeText(getActivity(), "버전 정보 클릭", Toast.LENGTH_SHORT).show();
                        break;

                    // OS 라이센스 정보
                    case R.id.OS_License:
                        Toast.makeText(getActivity(), "OS 클릭", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

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
                        Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

                        break;

                    // 번호 삭제
                    case R.id.deleteBtn1 :
                        inputPhoneNum1.setText("");
                        editor.putString("phoneNum1", inputPhoneNum1.getText().toString()); // DB에 공백 저장
                        editor.commit();
                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn2 :
                        inputPhoneNum2.setText("");
                        editor.putString("phoneNum2", inputPhoneNum2.getText().toString());
                        editor.commit();
                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;

                    case R.id.deleteBtn3 :
                        inputPhoneNum3.setText("");
                        editor.putString("phoneNum3", inputPhoneNum3.getText().toString());     // DB에 보호자 번호 저장
                        editor.commit();
                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        break ;
                }
            }
        };
        saveBtn.setOnClickListener(Listener);
        deleteBtn1.setOnClickListener(Listener);
        deleteBtn2.setOnClickListener(Listener);
        deleteBtn3.setOnClickListener(Listener);
    }

    //연락처 불러오기 버튼 클릭 시 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int textViewNum = sf.getInt("textViewNum", 0);

        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
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
}
