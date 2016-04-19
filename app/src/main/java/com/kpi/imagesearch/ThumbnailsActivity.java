package com.kpi.imagesearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 18/4/16.
 */
public class ThumbnailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{
    private static final String TAG = ThumbnailsActivity.class.getSimpleName();
    private static final String QUERY = "query";

    private RecyclerView mRvThumbnails;
    private RelativeLayout mRlPbHolder;
    private ThumbnailAdapter mThumbnailAdapter;

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

        mRvThumbnails = (RecyclerView) findViewById(R.id.rv_thumbnails);
        mRlPbHolder = (RelativeLayout) findViewById(R.id.rl_pb_holder);
        mThumbnailAdapter = new ThumbnailAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvThumbnails.setLayoutManager(linearLayoutManager);
        mRvThumbnails.setAdapter(mThumbnailAdapter);

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ImageLoader(this, mQueryParam);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        mThumbnailAdapter.setData(data);
        mThumbnailAdapter.notifyDataSetChanged();
        mRlPbHolder.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
