package com.abdo.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.abdo.lowcostvideo.Core.Twitter;
import com.abdo.lowcostvideo.LowCostVideo;
import com.abdo.lowcostvideo.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.abdo.lowcostvideo.LowCostVideo.agent;
import static com.abdo.lowcostvideo.Utils.Utils.putModel;
import static com.abdo.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for Twitter
    By
Khun Htetz Naing
 */

public class TW {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.post("https://twdown.net/download.php")
                .addBodyParameter("URL", url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onTaskCompleted(sortMe(Twitter.fetch(response)),true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }
}
