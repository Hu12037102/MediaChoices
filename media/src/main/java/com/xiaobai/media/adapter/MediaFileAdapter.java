package com.xiaobai.media.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xiaobai.media.R;
import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.GlideManger;
import com.xiaobai.media.utils.ScreenUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 21:31
 * 更新时间: 2020/6/20 21:31
 * 描述:
 */
public class MediaFileAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, MediaFile> {
    private boolean mIsShowCamera;
    private List<MediaFile> mData;
    private static final int TYPE_CAMERA = 1;

    public void setOnClickCameraListener(OnClickCameraListener onClickCameraListener) {
        this.onClickCameraListener = onClickCameraListener;
    }

    private OnClickCameraListener onClickCameraListener;

    public void setOnClickMediaListener(OnClickMediaListener onClickMediaListener) {
        this.onClickMediaListener = onClickMediaListener;
    }

    private OnClickMediaListener onClickMediaListener;

    public MediaFileAdapter(Context context, boolean isShowCamera, List<MediaFile> data) {
        super(context, data);
        this.mIsShowCamera = isShowCamera;
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowCamera && position == 0) {
            return MediaFileAdapter.TYPE_CAMERA;
        }
        return super.getItemViewType(position);
    }


    @Override
    protected void onBindViewDataHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaCameraViewHolder) {
            MediaCameraViewHolder cameraViewHolder = (MediaCameraViewHolder) holder;
            int finalPosition = position;
            cameraViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickCameraListener != null) {
                        onClickCameraListener.onClickCamera(v, finalPosition);
                    }
                }
            });

        } else if (holder instanceof MediaFileViewHolder) {
            if (mIsShowCamera) {
                position -= 1;
            }
            Context context = holder.itemView.getContext();
            MediaFileViewHolder fileViewHolder = (MediaFileViewHolder) holder;
            MediaFile mediaFile = mData.get(position);
            GlideManger.get(context).loadMediaImage(context, mediaFile.filePath, fileViewHolder.mRivMedia);
            if (mediaFile.isCheck) {
                fileViewHolder.mViewMask.setVisibility(View.VISIBLE);
                // GlideManger.get(context).loadImage(context, R.mipmap.icon_media_check, fileViewHolder.mAivCheck);
                fileViewHolder.mAtvCheck.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_check,0,0,0);
            } else {
                fileViewHolder.mViewMask.setVisibility(View.GONE);
                // GlideManger.get(context).loadImage(context, R.mipmap.icon_media_default, fileViewHolder.mAivCheck);
                fileViewHolder.mAtvCheck.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_default,0,0,0);
            }
            fileViewHolder.mAtvCheck.setOnClickListener(v -> {

            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MediaFileAdapter.TYPE_CAMERA) {
            return new MediaCameraViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_camera_view, parent, false));
        } else {
            return new MediaFileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_file_view, parent, false));
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mIsShowCamera ? mData.size() + 1 : mData.size();
    }

    static class MediaFileViewHolder extends RecyclerView.ViewHolder {
        private View mViewMask;
        private RoundedImageView mRivMedia;
        private AppCompatTextView mAtvCheck;
        private AppCompatTextView mAtvVideoLength;

        public MediaFileViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            MediaFileAdapter.setItemParams(itemView);
            mRivMedia = itemView.findViewById(R.id.riv_media);
            mAtvCheck = itemView.findViewById(R.id.atv_media_check);
            mAtvVideoLength = itemView.findViewById(R.id.atv_video_length);
            mViewMask = itemView.findViewById(R.id.view_mask);

        }
    }

    static class MediaCameraViewHolder extends RecyclerView.ViewHolder {


        public MediaCameraViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            MediaFileAdapter.setItemParams(itemView);
        }
    }

    public interface OnClickCameraListener {
        void onClickCamera(@NonNull View view, int position);
    }

    public interface OnClickMediaListener {
        void onClickMedia(@NonNull View view, int position);

        default void onCheckMedia(@NonNull View view, int position, boolean isCheck) {
        }
    }

    public static void setItemParams(@NonNull View itemView) {
        Context context = itemView.getContext();
        ViewGroup.LayoutParams itemParams = itemView.getLayoutParams();
        itemParams.width = ScreenUtils.screenWidth(context) / MediaActivity.GRID_COUNT;
        itemParams.height = ScreenUtils.screenWidth(context) / MediaActivity.GRID_COUNT;
        itemView.setLayoutParams(itemParams);
        itemView.setPadding(ScreenUtils.dp2px(context, 2f), ScreenUtils.dp2px(context, 2f),
                ScreenUtils.dp2px(context, 2f), ScreenUtils.dp2px(context, 2f));
    }
}
