package com.xiaobai.media.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.chrisbanes.photoview.PhotoView;
import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.GlideManger;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.FileUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/19 20:25
 * 更新时间: 2020/8/19 20:25
 * 描述:预览媒体适配器
 */
public class PreviewMediaAdapter extends RecyclerView.Adapter<PreviewMediaAdapter.ViewHolder> {
    private Context mContext;
    private List<MediaFile> mData;

    public void setOnClickPreviewMediaListener(OnClickPreviewMediaListener onClickPreviewMediaListener) {
        this.onClickPreviewMediaListener = onClickPreviewMediaListener;
    }

    private OnClickPreviewMediaListener onClickPreviewMediaListener;

    public PreviewMediaAdapter(@NonNull Context context, @NonNull List<MediaFile> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_preview_media_view, parent, false));
    }

    @Override
    public int getItemCount() {
        return DataUtils.getListSize(mData);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaFile mediaFile = mData.get(position);
        if (FileUtils.isVideoMinType(mediaFile.filePath)) {
            holder.mPhotoView.setVisibility(View.GONE);
            holder.mAivContent.setVisibility(View.VISIBLE);
            holder.mAivVideo.setVisibility(View.VISIBLE);
            GlideManger.get(mContext).loadPreviewImage(mediaFile.filePath, holder.mAivContent);
            holder.mAivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickPreviewMediaListener != null) {
                        onClickPreviewMediaListener.onClickVideo(v, position);
                    }

                }
            });
        } else if (FileUtils.isGifMinType(mediaFile.filePath)) {
            holder.mPhotoView.setVisibility(View.GONE);
            holder.mAivContent.setVisibility(View.VISIBLE);
            holder.mAivVideo.setVisibility(View.GONE);
            GlideManger.get(mContext).loadPreviewImage(mediaFile.filePath, holder.mAivContent);
        } else {
            holder.mPhotoView.setVisibility(View.VISIBLE);
            holder.mAivContent.setVisibility(View.GONE);
            holder.mAivVideo.setVisibility(View.GONE);
            GlideManger.get(mContext).loadPreviewImage(mediaFile.filePath, holder.mPhotoView);
            holder.mPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickPreviewMediaListener != null) {
                        onClickPreviewMediaListener.onClickPager(v, position);
                    }
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickPreviewMediaListener != null) {
                    onClickPreviewMediaListener.onClickPager(v, position);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mAivVideo;
        private final AppCompatImageView mAivContent;
        private final PhotoView mPhotoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoView = itemView.findViewById(R.id.pv_media_img);
            mAivContent = itemView.findViewById(R.id.aiv_content);
            mAivVideo = itemView.findViewById(R.id.aiv_video);
        }
    }

    public interface OnClickPreviewMediaListener {
        void onClickVideo(@NonNull View view, int position);

        void onClickPager(@NonNull View view, int position);
    }
}
