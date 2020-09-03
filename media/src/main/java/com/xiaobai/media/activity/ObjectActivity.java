package com.xiaobai.media.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.xiaobai.media.BuildConfig;
import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.permission.PermissionActivity;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.FileUtils;
import com.xiaobai.media.weight.Toasts;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import utils.bean.ImageConfig;
import utils.task.CompressImageTask;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 20:38
 * 更新时间: 2020/6/20 20:38
 * 描述:
 */
public abstract class ObjectActivity extends PermissionActivity {
    public static final String KEY_PARCELABLE_LIST_CHECK_DATA = "key_parcelable_list_check_data";
    public static final String KEY_INDEX_CHECK_POSITION = "key_index_check_position";
    public static final String KEY_MEDIA_OPTION = "key_media_option";
    protected MediaSelector.MediaOption mMediaOption;
    protected boolean mIsRunCompressMedia;
    protected ArrayList<MediaFile> mCheckMediaData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        RxFFmpegInvoke.getInstance().setDebug(BuildConfig.DEBUG);
        initStatusBar();
        initPermission();
    }

    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorTheme));
        }
        setStatusTextColor(true);
    }


    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected void initPermission() {
        initView();
        initData();
        initEvent();
    }

    public void setStatusTextColor(boolean dark) {
        View decorView = getWindow().getDecorView();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public static void uCropImage(Activity activity, File firstFile, File lastFile, int scaleX, int scaleY,
                                  int cropWidth, int cropHeight) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorTheme));
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorBlack));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(activity, R.color.colorAccent));
        options.setLogoColor(ContextCompat.getColor(activity, R.color.colorTheme));
        //   options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.colorClickButtonCenter));
        UCrop.of(Uri.fromFile(firstFile), Uri.fromFile(lastFile))
                .withAspectRatio(scaleX, scaleY)
                .withMaxResultSize(cropWidth, cropHeight)
                .withOptions(options)
                .start(activity);
    }

    public void updateTitleSureText(@NonNull TextView textView, @NonNull List<MediaFile> checkMediaData, int maxSelectorSize) {
        if (DataUtils.getListSize(checkMediaData) <= 0) {
            textView.setEnabled(false);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color4));
            textView.setText(R.string.complete);
        } else {
            textView.setEnabled(true);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color1));
            //   textView.setText(getString(R.string.complete_s, checkMediaData.size() + "", maxSelectorSize + ""));
            textView.setText(R.string.complete);
        }
    }

    public void clickResultMediaData() {
        if (!DataUtils.isListEmpty(mCheckMediaData) && mMediaOption != null) {
            if (mMediaOption.isCrop) {
                MediaFile mediaFile = mCheckMediaData.get(0);
                if (mediaFile.mediaType == MediaFile.TYPE_IMAGE) {
                    File firstFile = new File(mediaFile.filePath);
                    File lastFile = FileUtils.createChildDirector(FileUtils.MEDIA_FOLDER, FileUtils.getRootFile(this));
                    lastFile = new File(lastFile, FileUtils.createImageName());
                    uCropImage(this, firstFile, lastFile, mMediaOption.cropScaleX, mMediaOption.cropScaleY, mMediaOption.cropWidth, mMediaOption.cropHeight);
                } else {
                    Toasts.showToast(this, R.string.video_not_can_crop);
                }

            } else if (mMediaOption.isCompress) {
                if (mIsRunCompressMedia) {
                    Toasts.showToast(this, R.string.please_compress_media_wait);
                    return;
                }
                mIsRunCompressMedia = true;
                Log.w("clickResultMediaData--", mCheckMediaData.size() + "--");
                for (int i = 0; i < mCheckMediaData.size(); i++) {
                    int index = i;
                    MediaFile mediaFile = mCheckMediaData.get(i);
                    if (FileUtils.isImageMinType(mediaFile.filePath)) {
                        File compressFile = FileUtils.createChildDirector(FileUtils.MEDIA_FOLDER, FileUtils.getRootFile(this));
                        compressFile = new File(compressFile, FileUtils.createImageName());
                        ImageConfig imageConfig = ImageConfig.getDefaultConfig(mediaFile.filePath, compressFile.getAbsolutePath());
                        CompressImageTask.get().compressImage(imageConfig, new CompressImageTask.OnImageResult() {
                            @Override
                            public void startCompress() {
                                mIsRunCompressMedia = true;
                            }

                            @Override
                            public void resultFileSucceed(File file) {
                                mediaFile.fileCompressPath = file.getAbsolutePath();
                                if (index == mCheckMediaData.size() - 1) {
                                    mIsRunCompressMedia = false;
                                    resultMediaData();
                                }

                            }

                            @Override
                            public void resultFileError() {
                                mIsRunCompressMedia = true;
                            }
                        });
                    } else if (FileUtils.isVideoMinType(mediaFile.filePath)) {
                        File compressFile = FileUtils.createChildDirector(FileUtils.MEDIA_FOLDER, FileUtils.getRootFile(this));
                        compressFile = new File(compressFile, FileUtils.createVideoName());
                        String compressPath = compressFile.getAbsolutePath();
                        String[] complexCommand = new String[]{"ffmpeg", "-i", mediaFile.filePath, "-s",
                                mediaFile.width > mediaFile.height ? "1280*720" : "720*1280", "-c:v",
                                "libx264", "-crf", "30", "-preset", "ultrafast", "-y", "-acodec", "libmp3lame", compressPath};
                        RxFFmpegInvoke.getInstance().exit();
                        RxFFmpegInvoke.getInstance().runCommandRxJava(complexCommand)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new RxFFmpegSubscriber() {
                                    @Override
                                    public void onFinish() {
                                        mediaFile.fileCompressPath = compressPath;
                                        if (index == mCheckMediaData.size() - 1) {
                                            mIsRunCompressMedia = false;
                                            resultMediaData();

                                        }
                                        Log.w("clickResultMediaData--", "onFinish:" + mediaFile.fileCompressPath);
                                    }


                                    @Override
                                    public void onProgress(int progress, long progressTime) {
                                        mIsRunCompressMedia = true;
                                        Log.w("clickResultMediaData--", "onProgress:" + progress + "--" + progressTime);
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.w("clickResultMediaData--", "onCancel:");
                                        mIsRunCompressMedia = false;
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.w("clickResultMediaData--", "onError:");
                                        mIsRunCompressMedia = true;
                                    }
                                });
                    }
                }
            } else {
                resultMediaData();
            }
        }

    }

    protected void resultMediaData() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MediaSelector.KEY_PARCELABLE_MEDIA_DATA, mCheckMediaData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null && resultUri.getPath() != null) {

            }
            Log.w("onActivityResult--", resultUri.getPath() + "--");
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void openCamera() {

    }
}
