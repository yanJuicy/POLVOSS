package com.polvoss.etc;

import android.util.Log;

import com.polvoss.message.NotificationGetService;

public class TrustedWebsite extends NotificationGetService {

    //싱글톤 context를 통해서 다른 매서드에서 클래스 함수를 호출
    private static TrustedWebsite mContext;

    private TrustedWebsite(){
        /*mContext = this;
        Log.d("TrustedWebsite", "클래스 입장");*/
    }

    public static TrustedWebsite getContext() {
        if (mContext == null) {
            mContext = new TrustedWebsite();
        }
        return mContext;
    }

    public boolean WebsiteCheck(String contents) {

        //내용에서 .을 찾는다
        //.의 인덱스를 배열에 넣는다 순서대로 ( 브루드 포스트 알고리즘 사용 )
        //.의 인데스의 -1 +1번째 문자에 영단어가 들어가는지 판단한다
        // url인지 아닌지 구별한다
        // + 더 정확하게 url 인지 아닌지 판단할 수 있는 기준을 더한다.

        Log.d("TrustedWebsite", "내용 : " + contents);

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
        /*for(int i =0 ; i<URL1.length;i++){
            if(contents.contains(URL1[i])){
                check=true;
                Log.d("TrustedWebsite", "이게 이상한거임1 " + URL1[i]);
            }
        }*/

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
      /*  for(int i =0 ; i<URL2.length;i++){
            if(contents.contains(URL2[i])) {check=true;
                Log.d("TrustedWebsite", "이게 이상한거임2 " + URL2[i]);
            }
        }*/

        //통신사 3개소
        String[] URL3 = new String[] {"sktelecom.com",
                "kt.com",
                "uplus.co.kr"
        };
        /*for(int i =0 ; i<URL3.length;i++){
            if(contents.contains(URL3[i])) {check=true;
                Log.d("TrustedWebsite", "이게 이상한거임3 " + URL3[i]);}
        }
*/
        //국가기관 44개소
        String[] URL4 = new String[] {
                "president.go.kr",
                "nis.go.kr",
                "bai.go.kr",
                "kcc.go.kr",
                "humanrights.go.kr",
                "opm.go.kr",
                "moleg.go.kr",
                "mpva.go.kr",
                "mfds.go.kr",
                "ftc.go.kr",
                "acrc.go.kr",
                "nssc.go.kr",
                "moef.go.kr",
                "nts.go.kr",
                "customs.go.kr",
                "pps.go.kr",
                "kostat.go.kr",
                "moe.go.kr",
                "msit.go.kr",
                "mofa.go.kr",
                "unikorea.go.kr",
                "moj.go.kr",
                "spo.go.kr",
                "mnd.go.kr",
                "mma.go.kr",
                "dapa.go.kr",
                "mois.go.kr",
                "police.go.kr",
                "nfa.go.kr",
                "motie.go.kr",
                "kipo.go.kr",
                "mohw.go.kr",
                "kdca.go.kr",
                "me.go.kr",
                "weather.go.kr",
                "moel.go.kr",
                "mogef.go.kr",
                "molit.go.kr",
                "naacc.go.kr",
                "saemangeum.go.kr",
                "mof.go.kr",
                "kcg.go.kr",
                "mss.go.kr"
        };
        /*for(int i =0 ; i<URL4.length;i++){
            if(contents.contains(URL4[i])) {check=true;
                Log.d("TrustedWebsite", "이게 이상한거임 " + URL4[i]);}
        }*/


        //충북 자치 단체 12개소
        String[] URL5 = new String[] {
                "chungbuk.go.kr",
                "cheongju.go.kr",
                "chungju.go.kr",
                "jecheon.go.kr",
                "boeun.go.kr",
                "oc.go.kr",
                "yd21.go.kr",
                "jp.go.kr",
                "jincheon.go.kr",
                "goesan.go.kr",
                "eumseong.go.kr",
                "danyang.go.kr"
        };
        /*for(int i =0 ; i<URL5.length;i++){
            if(contents.contains(URL5[i])) check=true;
        }*/

        //인터넷 포털사이트 및 sns 12개소
        String[] URL6 = new String[] {
                "naver.com",
                "daum.net",
                "google.co.kr",
                "zum.com",
                "bing.com",
                "youtube.com",
                "facebook.com",
                "band.us",
                "instagram.com",
                "tiktok.com",
                "story.kakao.com",
                "twitter.com"
        };
        /*for(int i =0 ; i<URL6.length;i++){
            if(contents.contains(URL6[i])) check=true;
        }*/

        //금융기관-보험사 11개소
        String[] URL7 = new String[] {
                "mggeneralins.com",
                "kbinsure.co.kr",
                "heungkukfire.co.kr",
                "hwgeneralins.com",
                "meritzfire.com",
                "idbins.com",
                "nhfire.co.kr",
                "hi.co.kr",
                "lotteins.co.kr",
                "samsungfire.com",
                "kyobo.co.kr"
        };
        /*for(int i =0 ; i<URL7.length;i++){
            if(contents.contains(URL7[i])) check=true;
        }*/

        //금융기관 - 은행 30개소
        String[] URL8 = new String[] {
                "kbstar.com",
                "ibk.co.kr",
                "banking.nonghyup.com",
                "shinhan.com",
                "epost.go.kr",
                "standardchartered.co.kr",
                "kebhana.com",
                "citibank.co.kr",
                "wooribank.com",
                "knbank.co.kr",
                "pib.kjbank.com",
                "dgb.co.kr",
                "db.com",
                "busanbank.co.kr",
                "kdb.co.kr",
                "suhyup-bank.com",
                "jbbank.co.kr",
                "e-jejubank.com",
                "kfcc.co.kr",
                "openbank.cu.co.kr",
                "banking.nfcf.or.kr",
                "hsbc.co.kr",
                "sbcheck.bccard.com",
                "bankofamerica.com",
                "kbanknow.com",
                "kakaobank.com",
                "jpmorganchase.com",
                "ccb.com",
                "icbc.com.cn",
                "bank-of-china.com"
        };
        /*for(int i =0 ; i<URL8.length;i++){
            if(contents.contains(URL8[i])) check=true;
        }*/

        //금융기관 - 증권사 25개소
        String[] URL9 = new String[] {
                "nhqv.com",
                "myasset.com",
                "kbsec.com",
                "miraeassetdaewoo.com",
                "samsungpop.com",
                "truefriend.com",
                "iprovest.com",
                "hi-ib.com",
                "hmsec.com",
                "sks.co.kr",
                "hanwhawm.com",
                "shinhaninvest.com",
                "eugenefn.com",
                "home.imeritz.com",
                "shinyoung.com",
                "ebestsec.co.kr",
                "capefn.com",
                "bookook.co.kr",
                "kiwoom.com",
                "daishin.com",
                "db-fi.com",
                "fosskorea.com",
                "ktb.co.kr",
                "bnkfn.co.kr",
                "kakaopaysec.com"
        };
        /*for(int i =0 ; i<URL9.length;i++){
            if(contents.contains(URL9[i])) check=true;
        }*/

        //쇼핑몰(25개소)
        String[] URL10 = new String[] {
                "shopping.naver.com",
                "shoppinghow.kakao.com",
                "koharu.kr",
                "interpark.com",
                "homeplus.co.kr",
                "giftishow.com",
                "rentaldream.co.kr",
                "galleria.co.kr",
                "front.wemakeprice.com",
                "tmon.co.kr",
                "hyundaihmall.com",
                "lotteimall.com",
                "akmall.com",
                "display.cjmall.com",
                "auction.co.kr",
                "nsmall.com",
                "gmarket.co.kr",
                "emart.ssg.com",
                "11st.co.kr",
                "coupang.com",
                "g9.co.kr",
                "kurly.com",
                "baemin.com",
                "yogiyo.co.kr",
                "mukkebi.com"
        };
        /*for(int i =0 ; i<URL10.length;i++){
            if(contents.contains(URL10[i])) check=true;
        }*/

        //택배(34개소)
        String[] URL11 = new String[] {
                "cjlogistics.com",
                "parcel.epost.go.kr",
                "hanjin.co.kr",
                "lotteglogis.com",
                "ilogen.com",
                "homepick.com",
                "cvsnet.co.kr",
                "cupost.co.kr",
                "kdexp.com",
                "ds3211.co.kr",
                "ilyanglogis.com",
                "hdexp.co.kr",
                "kunyoung.com",
                "chunilps.co.kr",
                "hanjin.co.kr",
                "hanips.com",
                "ems.epost.go.kr",
                "dhl.com",
                "ups.com",
                "fedex.com",
                "usps.com",
                "pantos.com",
                "goodstoluck.co.kr",
                "honamlogis.co.kr",
                "slx.co.kr",
                "woorihb.com",
                "kgbps.com",
                "sebang.com",
                "ex.nhlogis.co.kr",
                "smartlogis.kr",
                "logisvalley.com",
                "lotteglogis.com",
                "better-logis.co.kr",
                "worker.co.kr"
        };

        for(int i =0 ; i<URL1.length;i++){
//            check = true;

            if (contents.contains(URL1[i]))
                check = URLCheck(contents, URL1[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL2.length;i++){
//            check = true;

            if (contents.contains(URL2[i]))
                check = URLCheck(contents, URL2[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL3.length;i++){
//            check = true;

            if (contents.contains(URL3[i]))
                check = URLCheck(contents, URL3[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL4.length;i++){
//            check = true;

            if (contents.contains(URL4[i]))
                check = URLCheck(contents, URL4[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL5.length;i++){
//            check = true;

            if (contents.contains(URL5[i]))
                check = URLCheck(contents, URL5[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL6.length;i++){
//            check = true;

            if (contents.contains(URL6[i]))
                check = URLCheck(contents, URL6[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL7.length;i++){
//            check = true;

            if (contents.contains(URL7[i]))
                check = URLCheck(contents, URL7[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL8.length;i++){
//            check = true;

            if (contents.contains(URL8[i]))
                check = URLCheck(contents, URL8[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL9.length;i++){
//            check = true;

            if (contents.contains(URL9[i]))
                check = URLCheck(contents, URL9[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL10.length;i++){
//            check = true;
            if (contents.contains(URL10[i]))
                check = URLCheck(contents, URL10[i]);

//            if (!check) break;
        }

        for(int i =0 ; i<URL11.length;i++){
//            check = true;
            if (contents.contains(URL11[i]))
                check = URLCheck(contents, URL11[i]);

//            if (!check) break;
        }

        if (check) {
            Log.d("TrustedWebsite", " 안전한 url일 확률이 있음");
            return false;
        }

        return true;
    }

    private boolean URLCheck(String contents, String url) {

            int f_index = contents.indexOf(url);
            if (f_index != 0) {
                char prevChar = contents.charAt(f_index-1);
                if (!(prevChar == '.' || prevChar=='/' || prevChar==':')) {
                    Log.d("TrustedWebsite", "첫번째 인덱스");
                    return false;
                }
            }


            int URL_length = url.length();
            int l_index = contents.lastIndexOf(url) + URL_length - 1;
            Log.d("TrustedWebsite", "마지막 인덱스 " + l_index);

            int content_length = contents.length();
            if (l_index + 1 < content_length) {
                char nextChar = contents.charAt(l_index+1);
                if (nextChar != '/') {
                    Log.d("TrustedWebsite", "마지막번째 인덱스");
                    return false;
                }
            }


        return true;
    }

}
