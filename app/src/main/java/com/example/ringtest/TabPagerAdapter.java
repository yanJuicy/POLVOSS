//package com.example.ringtest;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//
//public class TabPagerAdapter extends FragmentStatePagerAdapter {
//
//    // Count number of tabs
//    private int tabCount;
//
//    public TabPagerAdapter(FragmentManager fm, int tabCount) {
//        super(fm);
//        this.tabCount = tabCount;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//
//        //Returning the current tabs
//        switch (position) {
//            case 0:
//                VoiceSettingFragment voiceSettingFragment = new VoiceSettingFragment();
//                return voiceSettingFragment;
//
//            case 1:
//                SMSSettingFragment smsSettingFragment = new SMSSettingFragment();
//                return smsSettingFragment;
//
//            default:
//                return null;
//        }
//
//    }
//
//    @Override
//    public int getCount() {
//        return tabCount;
//    }
//}
