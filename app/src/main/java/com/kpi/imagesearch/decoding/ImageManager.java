package com.kpi.imagesearch.decoding;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

/**
 * Created by Andrey on 19/4/16.
 */
public class ImageManager {

    private Handler mHandler;
    private ImageView mImageView;

    public ImageManager(ImageView imageView) {
        mImageView = imageView;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                ImageTask imageTask = (ImageTask) msg.obj;
                switch (msg.what) {
                    case 1001:
                        mImageView.setImageBitmap(imageTask.getDecodedImage());
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
    }

    public void handleState(ImageTask imageTask, int state) {
        switch (state) {
            // The task finished downloading and decoding the image
            case 1001:
                /*
                 * Creates a message for the Handler
                 * with the state and the task object
                 */
                Message completeMessage = mHandler.obtainMessage(state, imageTask);
                completeMessage.sendToTarget();
                break;
        }
    }
}
