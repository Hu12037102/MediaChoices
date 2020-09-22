package com.xiaobai.media.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.xiaobai.media.bean.MediaFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

import utils.MediaScanner;

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
        if (director == null) {
            return;
        }
        if (!director.isDirectory()) {
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

    public static Uri fileToUri(@NonNull Context context, @NonNull File file, @NonNull Intent intent) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".provider";
            uri = FileProvider.getUriForFile(context, authority, file);
            List<ResolveInfo> resolveInfoData = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfoData.size() > 0)
                for (ResolveInfo resolveInfo : resolveInfoData) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static void scanImage(@NonNull Context context, @NonNull File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaScanner ms = new MediaScanner(context, file);
            ms.refresh();
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        }
    }

    public static Uri insertImageUri(@NonNull Context context, @NonNull File file) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        int[] size = getImageWidthHeight(file);
        contentValues.put(MediaStore.Images.Media.WIDTH, size[0]);
        contentValues.put(MediaStore.Images.Media.HEIGHT, size[1]);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, getImageMinType(file));
        contentValues.put(MediaStore.Images.Media.TITLE, file.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + FileUtils.MEDIA_FOLDER);
        } else {
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static int[] getImageWidthHeight(File file) {
        int[] data = new int[2];
        if (file != null && FileUtils.existsFile(file.getAbsolutePath())) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                options.outConfig = Bitmap.Config.RGB_565;
            }
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            data[0] = options.outWidth;
            data[1] = options.outHeight;
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

        return data;
    }

    public static String getImageMinType(File file) {
        if (file == null || !FileUtils.existsFile(file.getAbsolutePath())) {
            return "";
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return options.outMimeType;
    }

    /**
     * 把图片保存到共有库
     *
     * @param context
     * @param uri
     * @param file
     */
    public static void savePublicFile(@NonNull Context context, @NonNull Uri uri, @NonNull File file) {
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            os = context.getContentResolver().openOutputStream(uri, "w");
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int read;
                while ((read = fis.read(bytes)) != -1) {
                    if (os != null) {
                        os.write(bytes, 0, read);
                        os.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
