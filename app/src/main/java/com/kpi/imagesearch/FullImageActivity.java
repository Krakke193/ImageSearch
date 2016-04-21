package com.kpi.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Andrey on 21/4/16.
 */
public class FullImageActivity extends AppCompatActivity{

    private static final String IMG_URL = "img_url";

    public static void startActivity(Context context, String imgUrl) {
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(IMG_URL, imgUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView mIvFull = (ImageView) findViewById(R.id.iv_full);

        String imgUrl = getIntent().getStringExtra(IMG_URL);

        ImageManager.getInstance().loadImageToTarget(
                new WeakReference<>(mIvFull),
                null,
                imgUrl
        );
    }
}
