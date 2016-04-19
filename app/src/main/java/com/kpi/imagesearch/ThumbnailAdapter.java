package com.kpi.imagesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Andrey on 19/4/16.
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {

    private List<String> mData;

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    public ThumbnailAdapter.ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        return new ThumbnailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThumbnailAdapter.ThumbnailViewHolder holder, int position) {
        final String urlStr = mData.get(position);

        holder.mTvLink.setText(urlStr);
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                holder.mIvThumbnail.setImageBitmap((Bitmap) msg.obj);
            }
        };


        new Thread(new Runnable() {
            HttpURLConnection connection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;

                    Bitmap retBitmap = BitmapFactory.decodeStream(connection.getInputStream(), null, options);

                    Message completeMessage = handler.obtainMessage(1001, retBitmap);
                    handler.sendMessage(completeMessage);
                } catch (IOException e) {
                    Log.e("TET", e.toString());
                } finally {
                    connection.disconnect();
                }

            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onViewRecycled(ThumbnailViewHolder holder) {
        holder.mIvThumbnail.setImageBitmap(null);
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvLink;
        private ImageView mIvThumbnail;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);

            mTvLink = (TextView) itemView.findViewById(R.id.tv_item_thumbnail);
            mIvThumbnail = (ImageView) itemView.findViewById(R.id.iv_item_thumbnail);
        }
    }
}
