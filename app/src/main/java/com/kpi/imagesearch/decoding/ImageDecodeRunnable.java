package com.kpi.imagesearch.decoding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Andrey on 19/4/16.
 */
public class ImageDecodeRunnable implements Runnable {
    public static final int DECODE_STATE_COMPLETED = 1001;

    private ImageTask mImageTask;

    public ImageDecodeRunnable(ImageTask imageTask) {
        mImageTask = imageTask;
    }

    @Override
    public void run() {
        // Tries to decode the image buffer
        byte[] imageBuffer = mImageTask.getByteBuffer();

        Bitmap returnBitmap = BitmapFactory.decodeByteArray(
                imageBuffer,
                0,
                imageBuffer.length
        );

        // Sets the ImageView Bitmap
        mImageTask.setImage(returnBitmap);
        // Reports a status of "completed"
        mImageTask.handleDecodeState(DECODE_STATE_COMPLETED);
    }
}
