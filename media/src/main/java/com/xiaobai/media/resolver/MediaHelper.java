package com.xiaobai.media.resolver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.OnLoadMediaCallback;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.FileUtils;
import com.xiaobai.media.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MediaHelper {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    //查询的内容
    @SuppressLint("InlinedApi")
    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME};
    @SuppressLint("InlinedApi")
    private static final String[] ALL_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION};
    private static final String IMAGE_SELECTION_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String ALL_SELECTION_TYPE = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" +
            " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] IMAGE_WHERE_TYPE = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
    private static final String[] ALL_WHERE_TYPE = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
    private static final String SORT_ORDER = MediaStore.Files.FileColumns.DATE_MODIFIED + " desc";
    private final WeakReference<Activity> mWeak;

    private MediaHelper(@NonNull Activity activity) {
        mWeak = new WeakReference<>(activity);
    }

    public static MediaHelper create(@NonNull Activity activity) {
        return new MediaHelper(activity);
    }

    public void loadMediaResolver(MediaSelector.MediaOption option, OnLoadMediaCallback onLoadMediaCallback) {

        Activity activity = mWeak.get();
        if (activity == null || option == null) {
            return;
        }
        boolean isLoadAll = (option.mediaType == MediaSelector.MediaOption.MEDIA_ALL);
        List<MediaFile> allMediaData = new ArrayList<>();
        List<MediaFile> allMediaVideoData = new ArrayList<>();
        //所有文件夹
        List<MediaFolder> allFolderData = new ArrayList<>();
        if (onLoadMediaCallback != null) {
            onLoadMediaCallback.onMediaStart();
        }
        Cursor cursor = activity.getContentResolver().query(MediaHelper.QUERY_URI, isLoadAll ? ALL_PROJECTION : MediaHelper.IMAGE_PROJECTION, isLoadAll ? ALL_SELECTION_TYPE : MediaHelper.IMAGE_SELECTION_TYPE, isLoadAll ? ALL_WHERE_TYPE : MediaHelper.IMAGE_WHERE_TYPE, SORT_ORDER);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MediaFile mediaFile = new MediaFile();
                mediaFile.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                if (!FileUtils.existsImageFile(mediaFile.filePath) && !FileUtils.existsVideoFile(mediaFile.filePath)) {
                    continue;
                }
                Log.w("loadMediaResolver--", mediaFile.filePath + "\n" + FileUtils.getFileMinType(mediaFile.filePath));
                boolean isVideo = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

                if (isVideo) {
                    mediaFile.mediaType = MediaFile.TYPE_VIDEO;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mediaFile.fileDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    } else {
                        MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource(mediaFile.filePath);
                            mp.prepareAsync();
                            mediaFile.fileDuration = mp.getDuration();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    allMediaVideoData.add(mediaFile);
                }
                mediaFile.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                mediaFile.fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mediaFile.width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
                    mediaFile.height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
                }
                mediaFile.parentName = FileUtils.getParentFileName(mediaFile.filePath);
                mediaFile.parentPath = FileUtils.getParentFilePath(mediaFile.filePath);

             /*   List<MediaFile> mediaFiles = option.selectorFileData;
                mediaFile.isCheck = !DataUtils.isListEmpty(mediaFiles) && mediaFiles.contains(mediaFile);*/

                allMediaData.add(mediaFile);

                MediaFolder mediaFolder = new MediaFolder();
                mediaFolder.folderPath = mediaFile.parentPath;
                if (allFolderData.size() > 0 && allFolderData.contains(mediaFolder)) {
                    allFolderData.get(allFolderData.indexOf(mediaFolder)).fileData.add(mediaFile);
                } else {
                    mediaFolder.folderName = mediaFile.parentName;
                    mediaFolder.folderPath = mediaFile.parentPath;
                    mediaFolder.firstFilePath = mediaFile.filePath;
                    mediaFolder.fileData.add(mediaFile);
                    allFolderData.add(mediaFolder);
                }
            }
            cursor.close();
            if (allMediaData.size() > 0) {
                //装所有文件的文件夹
                MediaFolder allFolder = new MediaFolder();
                allFolder.folderPath = activity.getString(R.string.all_file);
                allFolder.folderName = activity.getString(R.string.all_file);
                allFolder.firstFilePath = allMediaData.get(0).filePath;
                allFolder.isCheck = true;
                allFolder.fileData.addAll(allMediaData);
                allFolderData.add(0, allFolder);

                if (allMediaVideoData.size() > 0) {
                    MediaFolder allVideoFolder = new MediaFolder();
                    allVideoFolder.folderPath = activity.getString(R.string.all_video);
                    allVideoFolder.folderName = activity.getString(R.string.all_video);
                    allVideoFolder.firstFilePath = allMediaVideoData.get(0).filePath;
                    allVideoFolder.fileData.addAll(allMediaVideoData);
                    allVideoFolder.isAllVideo = true;
                    allFolderData.add(allFolderData.indexOf(allFolder) + 1, allVideoFolder);
                }
                if (onLoadMediaCallback != null) {
                    onLoadMediaCallback.onMediaSucceed(allFolderData);
                }
            } else {
                if (onLoadMediaCallback != null) {
                    onLoadMediaCallback.onMediaEmpty();
                }
            }


        } else {
            if (onLoadMediaCallback != null) {
                onLoadMediaCallback.onMediaEmpty();
            }
        }
    }


}
