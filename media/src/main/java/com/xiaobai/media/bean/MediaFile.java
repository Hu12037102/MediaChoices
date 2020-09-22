package com.xiaobai.media.bean;

import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xiaobai.media.utils.FileUtils;

import java.io.File;

public class MediaFile implements Parcelable {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public String fileName;
    public String filePath;
    public String fileCompressPath = "";
    public int fileSize;
    public int width;
    public int height;
    public String parentName;
    public String parentPath;
    // public boolean isCheck;
    public long fileDuration;
    public int mediaType;

    public MediaFile() {
    }

    protected MediaFile(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        fileSize = in.readInt();
        width = in.readInt();
        height = in.readInt();
        parentName = in.readString();
        parentPath = in.readString();
        // isCheck = in.readByte() != 0;
        fileDuration = in.readLong();
        mediaType = in.readInt();
        fileCompressPath = in.readString();
    }

    public static final Creator<MediaFile> CREATOR = new Creator<MediaFile>() {
        @Override
        public MediaFile createFromParcel(Parcel in) {
            return new MediaFile(in);
        }

        @Override
        public MediaFile[] newArray(int size) {
            return new MediaFile[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MediaFile) {
            MediaFile mediaFile = (MediaFile) obj;
            if (mediaFile.filePath != null && this.filePath != null &&
                    mediaFile.filePath.equals(filePath)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeInt(fileSize);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(parentName);
        dest.writeString(parentPath);
        //dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeLong(fileDuration);
        dest.writeInt(mediaType);
        dest.writeString(fileCompressPath);
    }

    public static boolean isVideo(MediaFile mediaFile) {
        return mediaFile != null && mediaFile.mediaType == MediaFile.TYPE_VIDEO;
    }

    public static MediaFile createMediaImageFile(String filePath) {
        if (!FileUtils.existsFile(filePath)) {
            return null;
        }
        MediaFile mediaFile = new MediaFile();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        mediaFile.width = options.outWidth;
        mediaFile.height = options.outHeight;
        File file = new File(filePath);
        mediaFile.fileName = file.getName();
        mediaFile.filePath = filePath;
        mediaFile.mediaType = MediaFile.TYPE_IMAGE;
        mediaFile.fileSize = (int) file.length();
        mediaFile.parentPath = file.getPath();
        return mediaFile;
    }
}
