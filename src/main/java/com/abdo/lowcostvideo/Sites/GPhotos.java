package com.abdo.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.abdo.lowcostvideo.LowCostVideo;
import com.abdo.lowcostvideo.Model.XModel;
import com.abdo.lowcostvideo.Utils.GPhotosUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.abdo.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for Google Photos
    By
Khun Htetz Naing
 */

public class GPhotos {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .setUserAgent(LowCostVideo.agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = GPhotosUtils.getGPhotoLink(response);
                        onTaskCompleted.onTaskCompleted(xModels,true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }
}
