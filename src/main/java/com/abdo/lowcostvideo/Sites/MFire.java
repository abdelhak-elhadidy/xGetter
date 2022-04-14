package com.abdo.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.abdo.lowcostvideo.LowCostVideo;
import com.abdo.lowcostvideo.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.abdo.lowcostvideo.LowCostVideo.agent;
import static com.abdo.lowcostvideo.Utils.FacebookUtils.getFbLink;
import static com.abdo.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for Mediafire
    By
Khun Htetz Naing
 */

public class MFire {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(fixURL(url))
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "aria-label=\"Download file\"\\n.+href=\"(.*)\"";
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
