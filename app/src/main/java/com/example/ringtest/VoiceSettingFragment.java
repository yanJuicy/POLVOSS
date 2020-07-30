package com.example.ringtest;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class VoiceSettingFragment extends Fragment {

    int maxX;

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


        /*******************************************
         * 시간 설정, text가 범위 초과하는 현상 발생
         *******************************************/

        textViewTime.setText("Time : " + seekBarTime.getProgress());
        Point maxSizePoint = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
        maxX = maxSizePoint.x;
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                int val = (progressValue * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewTime.setText("Time : " + progressValue);
                int textViewX = val - (textViewTime.getWidth() / 2);
                int finalX = textViewTime.getWidth() + textViewX > maxX ? (maxX - textViewTime.getWidth() - 16) : textViewX + 16 /*your margin*/;
                textViewTime.setX(finalX < 0 ? 16/*your margin*/ : finalX);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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
}
