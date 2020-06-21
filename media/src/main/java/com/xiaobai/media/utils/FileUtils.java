package com.xiaobai.media.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 16:59
 * 更新时间: 2020/6/20 16:59
 * 描述:
 */
public class FileUtils {
    public static boolean existsImageFile(@NonNull String path) {
        if (existsFile(path)) {
            String name = new File(path).getName();
            return name.endsWith(".jpg") || name.endsWith(".jpeg")
                    || name.endsWith(".png") || name.endsWith(".bpm")
                    || name.endsWith(".webp");
        }
        return false;
    }

    public static boolean existsFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean existsVideoFile(@NonNull String path) {
        if (existsFile(path)) {
            String name = new File(path).getName();
            return name.endsWith(".3gp") || name.endsWith(".mp4")
                    || name.endsWith(".avi") || name.endsWith(".flv");
        }
        return false;
    }

    public static String getParentFileName(@NonNull String filePath) {
        return getParentFile(filePath).getName();
    }
    private static File getParentFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.getParentFile();
        }
        throw new NullPointerException("file must exists or isFile");
    }

    public static String getParentFilePath(@NonNull String filePath) {
        return getParentFile(filePath).getAbsolutePath();
    }
}
