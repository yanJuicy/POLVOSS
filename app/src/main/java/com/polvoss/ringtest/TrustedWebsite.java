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

        boolean check = false;

        //언론사 52개소
        String[] URL1 = new String[] {"mydaily.co.kr",
                "dt.co.kr",
                "donga.com",
                "dailian.co.kr",
                "newsis.com",
                "newstapa.org",
                "newdaily.co.kr",
                "nocutnews.co.kr",
                "kmib.co.kr",
                "khan.co.kr",
                "mk.co.kr",
                "mt.co.kr",
                "munhwa.com",
                "mediatoday.co.kr",
                "bloter.net",
                "sedaily.com",
                "seoul.co.kr",
                "segye.com",
                "sports.donga.com",
                "sportsseoul.com",
                "sports.chosun.com",
                "sportalkorea.com",
                "sisain.co.kr",
                "asiae.co.kr",
                "inews24.com",
                "yonhapnewstv.co.kr",
                "ohmynews.com",
                "edaily.co.kr",
                "isplus.joins.com",
                "etnews.com",
                "biz.chosun.com",
                "chosun.com",
                "koreajoongangdaily.joins.com",
                "joongang.joins.com",
                "zdnet.co.kr",
                "jiji.com",
                "koreaherald.com",
                "fnnews.com",
                "pressian.com",
                "hani.co.kr",
                "hankyung.com",
                "wowtv.co.kr",
                "hankookilbo.com",
                "heraldcorp.com",
                "jtbc.joins.com",
                "kbs.co.kr",
                "imbc.com",
                "mbn.co.kr",
                "osen.mt.co.kr",
                "sbs.co.kr",
                "ytn.co.kr"
        };
        for(int i =0 ; i<51;i++){
            if(contents.contains(URL1[i])) check=true;
        }

        //충북 언론사 16개소
        String[] URL2 = new String[] {"ccdailynews.com",
                "dynews.co.kr",
                "cctimes.kr",
                "jbnews.com",
                "inews365.com",
                "ccdn.co.kr",
                "cctoday.co.kr",
                "cheongju.kbs.co.kr",
                "chungju.kbs.co.kr",
                "mbccb.co.kr",
                "cjb.co.kr",
                "cjcbs.co.kr",
                "cjbbs.co.kr",
                "hcn.co.kr",
                "ccreview.co.kr",
                "cbinews.co.kr"
        };
        for(int i =0 ; i<16;i++){
            if(contents.contains(URL2[i])) check=true;
        }

        //통신사 3개소
        String[] URL3 = new String[] {"sktelecom.com",
                "kt.com",
                "uplus.co.kr"
        };
        for(int i =0 ; i<3;i++){
            if(contents.contains(URL3[i])) check=true;
        }

        //국가기관 44개소
        String[] URL4 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };
        for(int i =0 ; i<44;i++){
            if(contents.contains(URL4[i])) check=true;
        }


        //충북 자치 단체 12개소
        String[] URL5 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };
        for(int i =0 ; i<12;i++){
            if(contents.contains(URL5[i])) check=true;
        }

        //인터넷 포털사이트 및 sns 12개소
        String[] URL6 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };
        for(int i =0 ; i<12;i++){
            if(contents.contains(URL6[i])) check=true;
        }

        //금융기관-보험사 11개소
        String[] URL7 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };
        for(int i =0 ; i<11;i++){
            if(contents.contains(URL7[i])) check=true;
        }

        //금융기관 - 은행 30개소
        String[] URL8 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };
        for(int i =0 ; i<30;i++){
            if(contents.contains(URL8[i])) check=true;
        }

        //금융기관 - 증권사 25개소
        String[] URL9 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
        };
        for(int i =0 ; i<25;i++){
            if(contents.contains(URL9[i])) check=true;
        }

        //쇼핑몰(25개소)
        String[] URL10 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
        };
        for(int i =0 ; i<25;i++){
            if(contents.contains(URL10[i])) check=true;
        }

        //택배(34개소)
        String[] URL11 = new String[] {"",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",

        };
        for(int i =0 ; i<34;i++){
            if(contents.contains(URL11[i])) check=true;
        }

        if (check) {
            Log.d("NotificationListener", " 안전한 url일 확률이 있음");
            return false;
        }

        return true;
    }
}
