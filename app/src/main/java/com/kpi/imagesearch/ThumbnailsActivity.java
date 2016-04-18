package com.kpi.imagesearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrey on 18/4/16.
 */
public class ThumbnailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{
    private static final String TAG = ThumbnailsActivity.class.getSimpleName();
    private static final String QUERY = "query";

    private String mQueryParam = "";

    public static void startActivity(Context context, String query) {
        Intent intent = new Intent(context, ThumbnailsActivity.class);
        intent.putExtra(QUERY, query);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnails);

        if (getIntent() != null) {
            mQueryParam = getIntent().getStringExtra(QUERY);
        }

        Log.d(TAG, mQueryParam);
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new ImageLoader(this, mQueryParam);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d(TAG, "Load complete! This is data: ");
        Log.d(TAG, data.toString());
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
