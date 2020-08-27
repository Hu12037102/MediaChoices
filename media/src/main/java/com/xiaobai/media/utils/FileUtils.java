package com.xiaobai.media.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.UUID;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 16:59
 * 更新时间: 2020/6/20 16:59
 * 描述:
 */
public class FileUtils {
    public static final String MEDIA_FOLDER = "media";
    public static final String MEDIA_CHOICES = "MediaChoices";

    /**
     * 外部存储
     *
     * @return
     */
    public static File getRootFile(@NonNull Context context) {
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = context.getExternalFilesDir(null);
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), MEDIA_CHOICES);
                mkdirsDirector(file);
            } else {
                file = context.getFilesDir();
            }
        }
        return file;
    }

    public static File createChildDirector(@NonNull String name, @NonNull File parentDirector) {
        File file = new File(parentDirector, TextUtils.isEmpty(name) ? FileUtils.MEDIA_FOLDER : name);
        mkdirsDirector(file);
        return file;
    }


    private static void mkdirsDirector(File director) {
        if (director != null && director.isDirectory()) {
            director.mkdirs();
        }
    }

    public static boolean existsImageFile(@NonNull String path) {
        if (existsFile(path)) {
            String name = new File(path).getName();
            return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")
                    || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".bpm")
                    || name.toLowerCase().endsWith(".webp") || name.toLowerCase().endsWith(".gif");
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

    public static String getFileMinType(File file) {
        if (file == null || !FileUtils.existsFile(file.getAbsolutePath())) {
            return "";
        }
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file.getName());
    }

    public static String getFileMinType(String filePath) {
        if (DataUtils.isEmpty(filePath)) {
            return "*/*";
        }
        return getFileMinType(new File(filePath));
    }

    public static boolean isVideoMinType(String path) {
        return !DataUtils.isEmpty(path) && FileUtils.existsFile(path) && getFileMinType(path).startsWith("video/");
    }

    public static boolean isGifMinType(String path) {
        return !DataUtils.isEmpty(path) && FileUtils.existsFile(path) && getFileMinType(path).startsWith("image/gif");
    }

    public static boolean isImageMinType(String path) {
        return !DataUtils.isEmpty(path) && FileUtils.existsFile(path) && getFileMinType(path).startsWith("image/");
    }

    /**
     * 获取时间长度
     *
     * @param videoDuration
     * @return
     */
    public static String videoDuration(long videoDuration) {
        StringBuilder sb = new StringBuilder();
        if (videoDuration >= 1000) {
            int second = (int) (videoDuration / 1000);
            if (second / 60 >= 1) {
                int minute = second / 60;
                int remainderSecond = second % 60;
                sb.append(minute).append(remainderSecond >= 10 ? ":" + remainderSecond : ":0" + remainderSecond);
            } else {
                if (second >= 10) {
                    sb.append("0:").append(second);
                } else {
                    sb.append("0:0").append(second);
                }
            }

        } else {
            sb.append("0:01");
        }
        return sb.toString();
    }

    public static String createFileHost() {
        return "MediaChoices_" + UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }

    public static String createImageName() {
        return createFileHost() + ".jpg";
    }

    public static String createGifName() {
        return createFileHost() + ".gif";
    }

    public static String createVideoName() {
        return createFileHost() + ".mp4";
    }
}
