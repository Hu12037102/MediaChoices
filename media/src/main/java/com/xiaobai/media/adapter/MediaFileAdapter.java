package com.xiaobai.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.R;
import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.GlideManger;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.FileUtils;
import com.xiaobai.media.utils.ScreenUtils;
import com.xiaobai.media.weight.Toasts;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 21:31
 * 更新时间: 2020/6/20 21:31
 * 描述:
 */
public class MediaFileAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, MediaFile> {
    private MediaSelector.MediaOption mOption;
    private List<MediaFile> mData;
    private static final int TYPE_CAMERA = 1;
    private List<MediaFile> mCheckMediaData;

    public void setOnClickCameraListener(OnClickCameraListener onClickCameraListener) {
        this.onClickCameraListener = onClickCameraListener;
    }

    private OnClickCameraListener onClickCameraListener;

    public void setOnClickMediaListener(OnClickMediaListener onClickMediaListener) {
        this.onClickMediaListener = onClickMediaListener;
    }

    private OnClickMediaListener onClickMediaListener;

    public MediaFileAdapter(Context context, MediaSelector.MediaOption option, List<MediaFile> data, List<MediaFile> checkMediaData) {
        super(context, data);
        this.mOption = option;
        this.mData = data;
        this.mCheckMediaData = checkMediaData;
    }

    @Override
    public int getItemViewType(int position) {
        if (mOption.isShowCamera && position == 0) {
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
            if (mOption.isShowCamera) {
                position -= 1;
            }
            Context context = holder.itemView.getContext();
            MediaFileViewHolder fileViewHolder = (MediaFileViewHolder) holder;
            MediaFile mediaFile = mData.get(position);

            GlideManger.get(context).loadRoundImage(mediaFile.filePath, fileViewHolder.mAivMedia, ScreenUtils.dp2px(holder.itemView.getContext(), 5));
            updateCheckMedia(mediaFile, fileViewHolder);
            if (FileUtils.isVideoMinType(mediaFile.filePath)) {
                fileViewHolder.mAtvMediaType.setVisibility(View.VISIBLE);
                fileViewHolder.mAtvMediaType.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_video, 0, 0, 0);
                fileViewHolder.mAtvMediaType.setText(FileUtils.videoDuration(mediaFile.fileDuration));
            } else if (FileUtils.isGifMinType(mediaFile.filePath)) {
                fileViewHolder.mAtvMediaType.setVisibility(View.VISIBLE);
                fileViewHolder.mAtvMediaType.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_gif, 0, 0, 0);
                fileViewHolder.mAtvMediaType.setText(null);
            } else {
                fileViewHolder.mAtvMediaType.setVisibility(View.GONE);
            }


            fileViewHolder.mFlCheck.setOnClickListener(v -> {
                if (mCheckMediaData.contains(mediaFile)) {
                    mCheckMediaData.remove(mediaFile);
                } else {
                    if (DataUtils.getListSize(mCheckMediaData) >= mOption.maxSelectorMediaCount) {
                        Toasts.showToast(mContext.getApplicationContext(), R.string.max_selector_media_count, mOption.maxSelectorMediaCount);
                        return;
                    } else if (!mOption.isSelectorMultiple && DataUtils.getListSize(mCheckMediaData) >= 1) {
                        MediaFile checkMedia = mCheckMediaData.get(0);
                        if (mediaFile.mediaType != checkMedia.mediaType) {
                            Toasts.showToast(mContext, R.string.not_selector_video_and_image);
                            return;
                        } else if (checkMedia.mediaType == MediaFile.TYPE_VIDEO && DataUtils.getListSize(mCheckMediaData) >= mOption.maxSelectorVideoCount) {
                            Toasts.showToast(mContext, R.string.max_selector_video_count, mOption.maxSelectorVideoCount);
                            return;
                        }

                    }
                    mCheckMediaData.add(mediaFile);
                }
                notifyDataSetChanged();
            });

        }
    }

    private void updateCheckMedia(@NonNull MediaFile mediaFile, @NonNull MediaFileViewHolder fileViewHolder) {

        if (mCheckMediaData.contains(mediaFile)) {
            fileViewHolder.mAtvCheck.setBackgroundResource(R.drawable.shape_check_media_count_view);
            fileViewHolder.mAtvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            fileViewHolder.mViewMask.setBackgroundResource(R.drawable.shape_media_mask_check_view);
            fileViewHolder.mAtvCheck.setText(String.valueOf(mCheckMediaData.indexOf(mediaFile) + 1));
        } else {
            fileViewHolder.mViewMask.setBackgroundResource(R.drawable.shape_media_mask_default_view);
            fileViewHolder.mAtvCheck.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_default, 0, 0, 0);
            fileViewHolder.mAtvCheck.setBackgroundResource(0);
            fileViewHolder.mAtvCheck.setText(null);
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
        return mData == null ? 0 : mOption.isShowCamera ? mData.size() + 1 : mData.size();
    }

    static class MediaFileViewHolder extends RecyclerView.ViewHolder {
        private View mViewMask;
        private AppCompatImageView mAivMedia;
        private AppCompatTextView mAtvCheck;
        private AppCompatTextView mAtvMediaType;
        private FrameLayout mFlCheck;

        public MediaFileViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            MediaFileAdapter.setItemParams(itemView);
            mAivMedia = itemView.findViewById(R.id.aiv_media);
            mAtvCheck = itemView.findViewById(R.id.atv_media_check);
            mAtvMediaType = itemView.findViewById(R.id.atv_type);
            mViewMask = itemView.findViewById(R.id.view_mask);
            mFlCheck = itemView.findViewById(R.id.fl_check);

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
