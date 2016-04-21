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
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Andrey on 18/4/16.
 */
public class ThumbnailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>, View.OnClickListener{

    private static final String TAG = ThumbnailsActivity.class.getSimpleName();
    private static final String QUERY = "query";
    private static final String QUALITY = "quality";

    private RecyclerView mRvThumbnails;
    private RelativeLayout mRlPbHolder;
    private ThumbnailAdapter mThumbnailAdapter;

    private String mQueryParam = "";
    private QueryManager.Builder.ImageSizes mImgSize = QueryManager.Builder.ImageSizes.MEDIUM;

    public static void startActivity(Context context, String query, QueryManager.Builder.ImageSizes imageSize) {
        Intent intent = new Intent(context, ThumbnailsActivity.class);
        intent.putExtra(QUERY, query);
        intent.putExtra(QUALITY, imageSize);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnails);



        if (getIntent() != null) {
            mQueryParam = getIntent().getStringExtra(QUERY);
            mImgSize = (QueryManager.Builder.ImageSizes) getIntent().getSerializableExtra(QUALITY);
        }

        mRvThumbnails = (RecyclerView) findViewById(R.id.rv_thumbnails);
        mRlPbHolder = (RelativeLayout) findViewById(R.id.rl_pb_holder);
        mThumbnailAdapter = new ThumbnailAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvThumbnails.setLayoutManager(linearLayoutManager);
        mRvThumbnails.setAdapter(mThumbnailAdapter);

        mThumbnailAdapter.setOnClickListener(this);

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ImageLoader(this, mQueryParam, mImgSize);
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

    @Override
    public void onClick(View v) {
        int position = mRvThumbnails.getChildLayoutPosition(v);

        String imgUrl = mThumbnailAdapter.getData().get(position);

        FullImageActivity.startActivity(this, imgUrl);
//        startActivity(new Intent(this, FullscreenActivity.class));
    }
}
