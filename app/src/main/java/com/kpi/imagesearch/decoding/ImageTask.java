package com.kpi.imagesearch.decoding;

import android.graphics.Bitmap;

/**
 * Created by Andrey on 19/4/16.
 */
public class ImageTask {

    private Bitmap mDecodedImage;

    private ImageDecodeRunnable mImageDecodeRunnable;

    ImageTask() {
        mImageDecodeRunnable = new ImageDecodeRunnable(this);
    }

    public Bitmap getDecodedImage() {
        return mDecodedImage;
    }

    public byte[] getByteBuffer() {
        return null;
    }

    public void setImage(Bitmap bitmap) {

    }

    public void handleDecodeState(int state) {

    }
}
