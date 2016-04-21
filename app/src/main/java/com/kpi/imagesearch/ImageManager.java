package com.kpi.imagesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andrey on 20/4/16.
 */
public class ImageManager {
    private static ImageManager sInstance;

    private LruCache<String, Bitmap> mMemoryCache;

    private ImageManager() {

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImageManager getInstance() {
        if (sInstance == null) sInstance = new ImageManager();
        return sInstance;
    }

    public void loadImageToTarget(final WeakReference<ImageView> imageViewWeakReference,
                                  @Nullable final WeakReference<ViewGroup> progressBarHolderWeakReference,
                                  final String imageUrl) {

        if (getBitmapFromMemCache(imageUrl) != null) {
            imageViewWeakReference.get().setImageBitmap(getBitmapFromMemCache(imageUrl));
            return;
        }

        if (progressBarHolderWeakReference != null) {
            progressBarHolderWeakReference.get().setVisibility(View.VISIBLE);
        }

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                imageViewWeakReference.get().setImageBitmap((Bitmap) msg.obj);

                if (progressBarHolderWeakReference != null) {
                    progressBarHolderWeakReference.get().setVisibility(View.GONE);
                }
            }
        };

        new Thread(new ImageTask(handler, imageUrl)).start();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public class ImageTask implements Runnable {
        private String mImageUrl;
        private Handler mUiThreadHandler;

        public ImageTask(Handler handler, String imageUrl) {
            mImageUrl = imageUrl;
            mUiThreadHandler = handler;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(mImageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;

                Bitmap retBitmap = BitmapFactory.decodeStream(connection.getInputStream(), null, null);

                addBitmapToMemoryCache(mImageUrl, retBitmap);

                Message completeMessage = mUiThreadHandler.obtainMessage(1001, retBitmap);
                mUiThreadHandler.sendMessage(completeMessage);
            } catch (IOException e) {
                Log.e("TET", e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
