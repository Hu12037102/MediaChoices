package com.xiaobai.media;

import androidx.annotation.NonNull;

import com.xiaobai.media.bean.MediaFolder;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 18:58
 * 更新时间: 2020/6/20 18:58
 * 描述:
 */
public interface OnLoadMediaCallback {
    default void onStart() {
    }

    void onSucceed(@NonNull List<MediaFolder> data);

   default void onEmpty(){};
}
