package com.xiaobai.media.utils;

import android.text.TextUtils;

import com.xiaobai.media.bean.MediaFile;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/19 10:39
 * 更新时间: 2020/8/19 10:39
 * 描述:
 */
public class DataUtils {
    public static boolean isListEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static int getListSize(List<?> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return TextUtils.isEmpty(charSequence);
    }

    public static int getCheckMediaTypeSize(List<MediaFile> checkMediaData, int mediaType) {
        int count = 0;
        if (DataUtils.isListEmpty(checkMediaData)) {
            return count;
        }
        for (MediaFile mediaFile : checkMediaData) {
            if (mediaFile.mediaType == mediaType) {
                count++;
            }
        }
        return count;
    }

}
