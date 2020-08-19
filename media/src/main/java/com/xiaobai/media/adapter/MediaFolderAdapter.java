package com.xiaobai.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.manger.GlideManger;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/21 22:37
 * 更新时间: 2020/6/21 22:37
 * 描述:
 */
public class MediaFolderAdapter extends RecyclerView.Adapter<MediaFolderAdapter.ViewHolder> {
    private List<MediaFolder> mData;

    public void setOnFolderClickListener(OnFolderClickListener onFolderClickListener) {
        this.onFolderClickListener = onFolderClickListener;
    }

    private OnFolderClickListener onFolderClickListener;

    public MediaFolderAdapter(List<MediaFolder> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_folder_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        MediaFolder mediaFolder = mData.get(position);
        GlideManger.get(context).loadImage(mediaFolder.firstFilePath, holder.mAivContent);
        holder.mAivCheck.setVisibility(mediaFolder.isCheck ? View.VISIBLE : View.GONE);
        holder.mTvName.setText(mediaFolder.folderName);
        holder.mTvCount.setText(context.getString(R.string.media_how_many_count, mediaFolder.fileData.size() + ""));
        holder.itemView.setOnClickListener(v -> {
            if (!mediaFolder.isCheck) {
                for (int i = 0; i < mData.size(); i++) {
                    mediaFolder.isCheck = i == position;
                }
                if (onFolderClickListener != null) {
                    onFolderClickListener.onClickUpdate(v, position);
                }
                notifyDataSetChanged();
            }
            if (onFolderClickListener != null) {
                onFolderClickListener.onClickItem(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView mAivContent;
        private AppCompatImageView mAivCheck;
        private TextView mTvName;
        private TextView mTvCount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            mAivContent = itemView.findViewById(R.id.aiv_content);
            mAivCheck = itemView.findViewById(R.id.aiv_check);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvCount = itemView.findViewById(R.id.tv_count);

        }
    }

    public interface OnFolderClickListener {
        void onClickItem(@NonNull View view, int position);

        void onClickUpdate(@NonNull View view, int position);
    }
}
