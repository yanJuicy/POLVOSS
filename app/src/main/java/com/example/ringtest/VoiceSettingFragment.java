package com.example.ringtest;
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
import android.widget.EditText;
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
    EditText inputPhoneNum;

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

        TextView setNumber = view.findViewById(R.id.setNumber);
        TextView personalInfoPolicy = view.findViewById(R.id.personal_Info_Policy);
        TextView version = view.findViewById(R.id.version_Info);
        TextView osLicense = view.findViewById(R.id.OS_License);

        sf = getContext().getSharedPreferences("settingFile", MODE_PRIVATE); // 로컬 DB 객체
        editor = sf.edit(); // DB 편집 객체

        inputPhoneNum = view.findViewById(R.id.setting_phoneNum);
        String phoneNum = sf.getString("phoneNum", "");
        inputPhoneNum.setText(phoneNum);

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


        /*******************************************
         * 번호 설정, Popup창이 안떠서 보류
         *******************************************/
        setNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("로그", "0");
                Toast.makeText(getActivity(), "번호 설정 클릭", Toast.LENGTH_SHORT).show();
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(getActivity(), SetNumberPopupActivity.class);
                //intent.putExtra("data", "Test Popup");
                //startActivityForResult(intent, 1);
                startActivity(intent);

            }
        });

        Button setContactButton = view.findViewById(R.id.setting_buttonSetContact);
        //내 연락처에서 번호 블러오기
        setContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });


        /*******************************************
         * 개인정보 (미완)
         *******************************************/
        personalInfoPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "개인 정보 클릭", Toast.LENGTH_SHORT).show();
            }
        });


        /*******************************************
         * 버전 정보 (미완)
         *******************************************/
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "버전 정보 클릭", Toast.LENGTH_SHORT).show();
            }
        });


        /*******************************************
         * OS 클릭 (미완)
         *******************************************/
        osLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "OS 클릭", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    //연락처 불러오기 버튼 클릭 시 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String  name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String num = cursor.getString(1);   //1은 번호를 받아옵니다.
            inputPhoneNum.setText(num);
            editor.putString("phoneNum", num);     // DB에 보호자 번호 저장
            editor.commit();
            cursor.close();
        }

    }
}
