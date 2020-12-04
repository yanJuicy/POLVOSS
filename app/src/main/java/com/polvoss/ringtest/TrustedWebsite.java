package com.polvoss.ringtest;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrustedWebsite extends NotificationGetService {

    //싱글톤 context를 통해서 다른 매서드에서 클래스 함수를 호출
    public static Context mContext;
    public void onCreate(Bundle savedInstance){
        mContext =this;
    }
    public boolean WebsiteCheck(String contents) {

        //내용에서 .을 찾는다
        //.의 인덱스를 배열에 넣는다 순서대로 ( 브루드 포스트 알고리즘 사용 )
        //.의 인데스의 -1 +1번째 문자에 영단어가 들어가는지 판단한다
        // url인지 아닌지 구별한다
        // + 더 정확하게 url 인지 아닌지 판단할 수 있는 기준을 더한다.

        Log.d("NotificationListener", "내용 : " + contents);

        boolean check = contents.contains("java");

        if (check) {
            Log.d("NotificationListener", " URL 메세지수신");
            return true;
        }

        return false;
    }
}
