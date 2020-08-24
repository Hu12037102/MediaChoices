package com.xiaobai.medias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.GlideManger;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.ScreenUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/24 18:20
 * 更新时间: 2020/8/24 18:20
 * 描述:
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
    private List<MediaFile> mData;

    public MainAdapter(@NonNull Context context, List<MediaFile> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaFile mediaFile = mData.get(position);
        GlideManger.get(mContext).loadImage(mediaFile.filePath, holder.mAivContent);

    }

    @Override
    public int getItemCount() {
        return DataUtils.getListSize(mData);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mAivContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ScreenUtils.getScreenWidth(itemView.getContext()) / 4;
            layoutParams.height = ScreenUtils.getScreenWidth(itemView.getContext()) / 4;
            itemView.setLayoutParams(layoutParams);
            mAivContent = itemView.findViewById(R.id.aiv_content);
        }
    }
}
