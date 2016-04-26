package com.kpi.imagesearch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by Andrey on 21/4/16.
 */
public class FullImageActivity extends AppCompatActivity{

    private static final String IMG_URL = "img_url";
    private static final String IMAGE_FORMAT = ".jpeg";
    private static final String IMAGE_TYPE = "image/*";
    private static final int IMAGE_QUALITY = 100;

    private ImageView mIvFullScreen;

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

        mIvFullScreen = (ImageView) findViewById(R.id.iv_full);

        String imgUrl = getIntent().getStringExtra(IMG_URL);

        ImageManager.getInstance().loadImageToTarget(
                new WeakReference<>(mIvFullScreen),
                null,
                imgUrl
        );
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fullscreen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.save:
                saveImageToGallery();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveImageToGallery() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File imageFile = saveImageToExternalPublicStorage();

                if (imageFile != null) {
                    addImageToGallery(imageFile.getPath());
                }
            }
        }).start();
    }
    private File saveImageToExternalPublicStorage() {
        mIvFullScreen.buildDrawingCache();
        Bitmap bm = mIvFullScreen.getDrawingCache();

        OutputStream fOut = null;
        File imageFile = null;
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File imageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

                if (!imageDir.exists())
                    imageDir.mkdirs();

                String fileName = Calendar.getInstance().getTimeInMillis() + IMAGE_FORMAT;
                imageFile = new File(imageDir, fileName);
                fOut = new FileOutputStream(imageFile);

                bm.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, fOut);
            }

            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    public void addImageToGallery(final String filePath) {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
