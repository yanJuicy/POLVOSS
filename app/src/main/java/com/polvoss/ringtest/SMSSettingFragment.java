//package com.example.ringtest;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//public class SMSSettingFragment extends Fragment {
//
//    Context mContext;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@Nullable LayoutInflater inflater,
//                             @Nullable ViewGroup Container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_setting_sms, null);
//
//        mContext = getContext();
//
//        Switch sms = view.findViewById(R.id.sms_Power);
//        sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    PreferenceManager.setBoolean(mContext, "sms", true);
//                    Toast.makeText(getActivity(), "sms On", Toast.LENGTH_SHORT).show();
//                }else{
//                    PreferenceManager.setBoolean(mContext, "sms", false);
//                    Toast.makeText(getActivity(), "sms off", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        Switch mms = view.findViewById(R.id.mms_Power);
//        mms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    PreferenceManager.setBoolean(mContext, "mms", true);
//                    Toast.makeText(getActivity(), "mms on", Toast.LENGTH_SHORT).show();
//                }else{
//                    PreferenceManager.setBoolean(mContext, "mms", false);
//                    Toast.makeText(getActivity(), "mms off", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        return view;
//    }
//}