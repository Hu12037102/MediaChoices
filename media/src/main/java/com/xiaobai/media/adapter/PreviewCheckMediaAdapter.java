package com.xiaobai.media.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.GlideManger;
import com.xiaobai.media.utils.DataUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/24 12:08
 * 更新时间: 2020/8/24 12:08
 * 描述:预览选中媒体Adapter
 */
public class PreviewCheckMediaAdapter extends RecyclerView.Adapter<PreviewCheckMediaAdapter.ViewHolder> {
    private Context mContext;
    private List<MediaFile> mCheckMediaDFileData;
    private int mPreviewPosition;
    private List<MediaFile> mAllMediaFileData;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    private OnClickItemListener onClickItemListener;

    public PreviewCheckMediaAdapter(@NonNull Context context, @NonNull List<MediaFile> checkMediaDFileData, @NonNull List<MediaFile> allMediaFileData, int previewPosition) {
        this.mContext = context;
        this.mCheckMediaDFileData = checkMediaDFileData;
        this.mPreviewPosition = previewPosition;
        this.mAllMediaFileData = allMediaFileData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_preview_check_media_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaFile mediaFile = mCheckMediaDFileData.get(position);
        MediaFile previewMediaFile = mAllMediaFileData.get(mPreviewPosition);
        Log.w("onBindViewHolder", mPreviewPosition + "=-");
        GlideManger.get(mContext).loadImage(mediaFile.filePath, holder.mAivContent);
        if (MediaFile.isVideo(mediaFile)) {
            holder.mAivVideoType.setVisibility(View.VISIBLE);
        } else {
            holder.mAivVideoType.setVisibility(View.GONE);
        }
        if (previewMediaFile.equals(mediaFile)) {
            holder.mAivContent.setBackgroundResource(R.drawable.shape_preview_media_check);
        } else {
            holder.mAivContent.setBackgroundResource(R.drawable.shape_preview_media_uncheck);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllMediaFileData.contains(mediaFile) && onClickItemListener != null) {
                    onClickItemListener.onClickItemView(v, position);
                }
            }
        });
    }

    public void notifyDataChange(int previewPosition) {
        this.mPreviewPosition = previewPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return DataUtils.getListSize(mCheckMediaDFileData);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mAivContent;
        private final AppCompatImageView mAivVideoType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAivContent = itemView.findViewById(R.id.aiv_content);
            mAivVideoType = itemView.findViewById(R.id.aiv_video_type);
        }
    }

    public interface OnClickItemListener {
        void onClickItemView(@NonNull View view, int position);
    }
}
