package com.xiaobai.media.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 17:02
 * 更新时间: 2020/6/20 17:02
 * 描述:
 */
public class MediaFolder implements Parcelable {
    public MediaFolder(){}
    public String folderName;
    public String folderPath;
    public List<MediaFile> fileData = new ArrayList<>();
    public boolean isCheck;
    public String firstFilePath;
    public boolean isAllVideo;

    protected MediaFolder(Parcel in) {
        folderName = in.readString();
        folderPath = in.readString();
        fileData = in.createTypedArrayList(MediaFile.CREATOR);
        isCheck = in.readByte() != 0;
        firstFilePath = in.readString();
        isAllVideo = in.readByte() != 0;
    }

    public static final Creator<MediaFolder> CREATOR = new Creator<MediaFolder>() {
        @Override
        public MediaFolder createFromParcel(Parcel in) {
            return new MediaFolder(in);
        }

        @Override
        public MediaFolder[] newArray(int size) {
            return new MediaFolder[size];
        }
    };

    /**
     * 判断文件夹的路径是否一致判断是否相等
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || folderPath == null)
            return false;
        if (obj instanceof MediaFolder) {
            MediaFolder folder = (MediaFolder) obj;
            return this.folderPath.equals(folder.folderPath);
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folderName);
        dest.writeString(folderPath);
        dest.writeTypedList(fileData);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeString(firstFilePath);
        dest.writeByte((byte) (isAllVideo ? 1 : 0));
    }
}
