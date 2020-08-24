package com.xiaobai.media.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.permission.PermissionActivity;
import com.xiaobai.media.utils.DataUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
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

    public void clickResultMediaData(ArrayList<MediaFile> checkMediaFileData) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MediaSelector.KEY_PARCELABLE_MEDIA_DATA, checkMediaFileData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
