package com.abdo.lowcostvideo.Sites;

import static com.abdo.lowcostvideo.LowCostVideo.agent;
import static com.abdo.lowcostvideo.Utils.Utils.putModel;

import android.webkit.WebView;

import com.abdo.lowcostvideo.LowCostVideo;
import com.abdo.lowcostvideo.Model.XModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidSpeeds {
    private static WebView webView;
    private static LowCostVideo.OnTaskCompleted onTaskCompleted;
    private static boolean gotMe = false;

    public static void get(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(fixURL(url))
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "sources:.+file:\"(.*)\"";
                        final Pattern pattern = Pattern.compile(regex);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(matcher.group(1),"Normal",xModels);
                            onTaskCompleted.onTaskCompleted(xModels,false);
                        } else onTaskCompleted.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String fixURL(String url){
        if (!url.startsWith("https")){
            url = url.replace("http","https");
        }
        return url;
    }
}
