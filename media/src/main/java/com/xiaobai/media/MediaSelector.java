package com.xiaobai.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.utils.DataUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 22:37
 * 更新时间: 2020/6/20 22:37
 * 描述:
 */
public class MediaSelector {
    private MediaOption mOption = MediaSelector.getDefaultOption();
    private WeakReference<Fragment> mSoftFragment;
    private WeakReference<Activity> mSoftActivity;
    private static final int SELECTOR_MAX_MEDIA_COUNT = 9;
    private static final int SELECTOR_MAX_VIDEO_COUNT = 5;
    public static final String KEY_PARCELABLE_MEDIA_DATA = "key_parcelable_media_data";

    public static final String KEY_MEDIA_OPTION = "key_media_option";
    public static final int REQUEST_CODE_MEDIA = 2080;

    private MediaSelector(@NonNull Fragment fragment) {
        mSoftFragment = new WeakReference<>(fragment);
    }

    private MediaSelector(@NonNull Activity activity) {
        mSoftActivity = new WeakReference<>(activity);
    }

    public static MediaSelector with(@NonNull Activity activity) {
        return new MediaSelector(activity);
    }

    public static MediaSelector with(@NonNull Fragment fragment) {
        return new MediaSelector(fragment);
    }

    public MediaSelector buildMediaOption(@NonNull MediaOption mediaOption) {
        this.mOption = mediaOption;
        return this;
    }


    public void openActivity() {
        if (mSoftActivity.get() != null) {
            Activity activity = mSoftActivity.get();
            Intent intent = new Intent(activity, MediaActivity.class);
            intent.putExtra(MediaSelector.KEY_MEDIA_OPTION, mOption);
            activity.startActivityForResult(intent, MediaSelector.REQUEST_CODE_MEDIA);
        } else if (mSoftFragment.get() != null) {
            Fragment fragment = mSoftFragment.get();
            Intent intent = new Intent(fragment.getContext(), MediaActivity.class);
            intent.putExtra(MediaSelector.KEY_MEDIA_OPTION, mOption);
            fragment.startActivityForResult(intent, MediaSelector.REQUEST_CODE_MEDIA);
        }
    }

    public static List<MediaFile> resultMediaData(Intent intent) {
        List<MediaFile> resultData = new ArrayList<>();
        if (intent != null) {
            List<MediaFile> data = intent.getParcelableArrayListExtra(MediaSelector.KEY_PARCELABLE_MEDIA_DATA);
            if (data != null && DataUtils.getListSize(data) > 0) {
                resultData.addAll(data);
            }
        }
        return resultData;
    }

    public static class MediaOption implements Parcelable {
        //所有媒体类型（图片、视频、gif）
        public static final int MEDIA_ALL = 0;
        //所有图片类型（静态图片、gif）
        public static final int MEDIA_IMAGE = 1;
        //静态图片类型（不包括gif）
        public static final int MEDIA_IMAGE_STATIC = 2;
        //所有的视频类型
        public static final int MEDIA_VIDEO = 3;

        public MediaOption() {
        }

        public int maxSelectorMediaCount = MediaSelector.SELECTOR_MAX_MEDIA_COUNT;
        public boolean isCompress = true;
        public boolean isShowCamera;
        //是不是选择多个类型
        public boolean isSelectorMultiple;
        public boolean isCrop;
        public int cropScaleX = 1;
        public int cropScaleY = 1;
        public int cropWidth = 720;
        public int cropHeight = 720;
        public int mediaType = MediaOption.MEDIA_IMAGE;
        public ArrayList<MediaFile> selectorFileData = new ArrayList<>();
        public int maxSelectorVideoCount = MediaSelector.SELECTOR_MAX_VIDEO_COUNT;


        protected MediaOption(Parcel in) {
            maxSelectorMediaCount = in.readInt();
            isCompress = in.readByte() != 0;
            isShowCamera = in.readByte() != 0;
            isSelectorMultiple = in.readByte() != 0;
            isCrop = in.readByte() != 0;
            cropScaleX = in.readInt();
            cropScaleY = in.readInt();
            cropWidth = in.readInt();
            cropHeight = in.readInt();
            mediaType = in.readInt();
            selectorFileData = in.createTypedArrayList(MediaFile.CREATOR);
            maxSelectorVideoCount = in.readInt();
        }

        public static final Creator<MediaOption> CREATOR = new Creator<MediaOption>() {
            @Override
            public MediaOption createFromParcel(Parcel in) {
                return new MediaOption(in);
            }

            @Override
            public MediaOption[] newArray(int size) {
                return new MediaOption[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(maxSelectorMediaCount);
            dest.writeByte((byte) (isCompress ? 1 : 0));
            dest.writeByte((byte) (isShowCamera ? 1 : 0));
            dest.writeByte((byte) (isSelectorMultiple ? 1 : 0));
            dest.writeByte((byte) (isCrop ? 1 : 0));
            dest.writeInt(cropScaleX);
            dest.writeInt(cropScaleY);
            dest.writeInt(cropWidth);
            dest.writeInt(cropHeight);
            dest.writeInt(mediaType);
            dest.writeTypedList(selectorFileData);
            dest.writeInt(maxSelectorVideoCount);
        }
    }

    public static MediaOption getDefaultOption() {
        return new MediaOption();
    }

}
