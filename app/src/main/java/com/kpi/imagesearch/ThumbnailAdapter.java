package com.kpi.imagesearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Andrey on 19/4/16.
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {

    private List<String> mData;

    private ImageManager mImageManager;
    private View.OnClickListener mOnClickListener;

    public ThumbnailAdapter() {
        mImageManager = ImageManager.getInstance();
    }

    public void setData(List<String> data) {
        mData = data;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public List<String> getData() {
        return mData;
    }

    @Override
    public ThumbnailAdapter.ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);

        itemView.setOnClickListener(mOnClickListener);

        return new ThumbnailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThumbnailAdapter.ThumbnailViewHolder holder, int position) {
        final String urlStr = mData.get(position);


        holder.mTvLink.setText(urlStr);
        holder.mIvThumbnail.setImageDrawable(null);
        holder.mRlPbHolder.setVisibility(View.GONE);
        mImageManager.loadImageToTarget(
                new WeakReference<>(holder.mIvThumbnail),
                new WeakReference<ViewGroup>(holder.mRlPbHolder),
                urlStr
        );
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onViewRecycled(ThumbnailViewHolder holder) {
        holder.mIvThumbnail.setImageDrawable(null);
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvLink;
        private ImageView mIvThumbnail;
        private RelativeLayout mRlPbHolder;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);

            mTvLink = (TextView) itemView.findViewById(R.id.tv_item_thumbnail);
            mIvThumbnail = (ImageView) itemView.findViewById(R.id.iv_item_thumbnail);
            mRlPbHolder = (RelativeLayout) itemView.findViewById(R.id.rl_pb_holder);
        }
    }
}
