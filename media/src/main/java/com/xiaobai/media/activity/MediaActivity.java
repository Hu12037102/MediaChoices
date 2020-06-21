package com.xiaobai.media.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.OnLoadMediaCallback;
import com.xiaobai.media.R;
import com.xiaobai.media.adapter.MediaFileAdapter;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.permission.imp.OnPermissionsResult;
import com.xiaobai.media.resolver.MediaHelper;
import com.xiaobai.media.weight.BaseRecyclerView;
import com.xiaobai.media.weight.TitleView;
import com.xiaobai.media.weight.Toasts;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 20:14
 * 更新时间: 2020/6/20 20:14
 * 描述:
 */
public class MediaActivity extends ObjectActivity {

    private BaseRecyclerView mRvMediaFile;
    public static final int GRID_COUNT = 4;
    private MediaSelector.MediaOption mMediaOption;
    private TitleView mTitleViewTop;
    public boolean isUp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void initView() {
        mRvMediaFile = findViewById(R.id.rv_data);
        mRvMediaFile.setLayoutManager(new GridLayoutManager(this, MediaActivity.GRID_COUNT));
        mTitleViewTop = findViewById(R.id.title_view_top);

    }

    @Override
    protected void initData() {
        List<MediaFile> mMediaFileData = new ArrayList<>();
        MediaFileAdapter mMediaFileAdapter = new MediaFileAdapter(mMediaOption.isShowCamera, mMediaFileData);
        mRvMediaFile.setAdapter(mMediaFileAdapter);
        MediaHelper.create(this).loadMediaResolver(mMediaOption.mediaType == MediaSelector.MediaOption.MEDIA_ALL, data -> {
            mMediaFileData.addAll(data.get(0).fileData);
            mMediaFileAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void initEvent() {
        mTitleViewTop.setOnCenterClickListener(new TitleView.OnCenterClickListener() {
            @Override
            public void onCenterClick(@NonNull View view) {
                isUp = !isUp;
                showTitleViewCenterAnimation(isUp);
            }
        });
    }

    @Override
    protected void initPermission() {
        Intent intent = getIntent();
        ArrayList mCheckMediaFileData = intent.getParcelableArrayListExtra(MediaSelector.KEY_MEDIA_DATA);
        mMediaOption = intent.getParcelableExtra(MediaSelector.KEY_MEDIA_OPTION);
        if (mMediaOption == null) {
            mMediaOption = MediaSelector.MediaOption.getDefaultOption();
        }
        requestPermissions();


    }

    private void requestPermissions() {
        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> allowPermissions) {
                MediaActivity.super.initPermission();
            }

            @Override
            public void onNoAllow(List<String> noAllowPermissions) {
                requestPermissions();
            }

            @Override
            public void onForbid(List<String> noForbidPermissions) {
                showForbidPermissionDialog();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

    }

    private void showTitleViewCenterAnimation(boolean isUp) {
        ObjectAnimator objectAnimator;
        if (isUp) {
            objectAnimator = ObjectAnimator.ofFloat(mTitleViewTop.mAivCenter, "rotation", 0f, 180f);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(mTitleViewTop.mAivCenter, "rotation", 180f, 360f);
        }

        objectAnimator.setDuration(200);
        objectAnimator.setRepeatCount(0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

}
