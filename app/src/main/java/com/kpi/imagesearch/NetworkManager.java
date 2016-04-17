package com.kpi.imagesearch;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sasha on 17/4/16.
 * This class used to perform network requests.
 */
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();
    private static final String SEARCH_TYPE = "image";
    private static final String RESPONSE_TYPE = "json";

    public void beginSearch(String qry) throws IOException {
        ImageSearchManager imageSearchManager = new ImageSearchManager.Builder(Constants.API_KEY, Constants.CX, qry)
                .setSearchType(SEARCH_TYPE)
                .setResponseType(RESPONSE_TYPE)
                .build();
        URL url = imageSearchManager.getQueryUrl();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        Log.v(TAG, "Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            Log.v(TAG, output);

//            if(output.contains("\"link\": \"")){
//                String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
//                Log.v(TAG, link);       //Will print the google search links
//            }
        }
        conn.disconnect();
    }
}
