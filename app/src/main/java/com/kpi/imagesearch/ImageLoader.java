package com.kpi.imagesearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 18/4/16.
 */
public class ImageLoader extends AsyncTaskLoader<List<String>> {
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static final String SEARCH_TYPE = "image";
    private static final String RESPONSE_TYPE = "json";

    private String mQuery;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public ImageLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Override
    public List<String> loadInBackground() {
        QueryManager queryManager = new QueryManager.Builder(Constants.API_KEY, Constants.CX, mQuery)
                .setSearchType(SEARCH_TYPE)
                .setResponseType(RESPONSE_TYPE)
                .build();

        HttpURLConnection connection = null;
        List<String> linksToReturn = new ArrayList<>();

        try {
            URL url = queryManager.getQueryUrl();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                if(output.contains("\"link\": \"")){
                    String link = output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                    linksToReturn.add(link);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "ImageLoader#loadInBackground() fell down with exception: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return linksToReturn;
    }
}
