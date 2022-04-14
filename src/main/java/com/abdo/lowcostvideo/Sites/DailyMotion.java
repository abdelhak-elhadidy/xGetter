package com.abdo.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.abdo.lowcostvideo.Utils.DailyMotionUtils;
import com.abdo.lowcostvideo.LowCostVideo;
import com.abdo.lowcostvideo.Model.XModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.abdo.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for DailyMotion
    By
Khun Htetz Naing
 */

public class DailyMotion {
    private static String COOKIE = null;
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                COOKIE = cookies.toString();
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        AndroidNetworking.get("https://www.dailymotion.com/embed/video/"+ DailyMotionUtils.getDailyMotionID(url))
                .setOkHttpClient(okHttpClient)
                .setUserAgent(LowCostVideo.agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        new DailyMotionUtils().fetch(response, new DailyMotionUtils.OnDone() {
                            @Override
                            public void onResult(ArrayList<XModel> xModels) {
                                if (xModels!=null){
                                    onComplete.onTaskCompleted(sortMe(xModels),true);
                                }else onComplete.onError();
                            }
                        });
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }
}
