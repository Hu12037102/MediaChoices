package com.xiaobai.media.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 15:11
 * 更新时间: 2020/6/20 15:11
 * 描述: 相机bean
 */
public class MediaCamera  implements Parcelable {
    public MediaCamera(){

    }

    public @DrawableRes int cameraRes;

    protected MediaCamera(Parcel in) {
        cameraRes = in.readInt();
    }

    public static final Creator<MediaCamera> CREATOR = new Creator<MediaCamera>() {
        @Override
        public MediaCamera createFromParcel(Parcel in) {
            return new MediaCamera(in);
        }

        @Override
        public MediaCamera[] newArray(int size) {
            return new MediaCamera[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cameraRes);
    }
}
