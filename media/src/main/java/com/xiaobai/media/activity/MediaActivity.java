package com.xiaobai.media.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.OnLoadMediaCallback;
import com.xiaobai.media.R;
import com.xiaobai.media.adapter.MediaFileAdapter;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.permission.imp.OnPermissionsResult;
import com.xiaobai.media.resolver.MediaHelper;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.weight.BaseRecyclerView;
import com.xiaobai.media.weight.FolderPopupWindow;
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
public class MediaActivity extends ObjectActivity implements OnLoadMediaCallback {

    private BaseRecyclerView mRvMediaFile;
    public static final int GRID_COUNT = 4;
    private MediaSelector.MediaOption mMediaOption;
    private TitleView mTitleViewTop;
    public boolean isUp;
    private List<MediaFolder> mAllMediaFolderData;
    private ObjectAnimator mFolderAnimator;
    FolderPopupWindow mFolderPopupWindow;
    private List<MediaFile> mMediaFileData;
    private MediaFileAdapter mMediaFileAdapter;
    private List<MediaFile> mCheckMediaData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void initPermission() {
        mCheckMediaData = new ArrayList<>();
        Intent intent = getIntent();
        mMediaOption = intent.getParcelableExtra(MediaSelector.KEY_MEDIA_OPTION);
        if (mMediaOption == null) {
            mMediaOption = MediaSelector.getDefaultOption();
        }
        if (DataUtils.getListSize(mMediaOption.selectorFileData) > 0) {
            mCheckMediaData.addAll(mMediaOption.selectorFileData);
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
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

    }

    @Override
    protected void initView() {
        mRvMediaFile = findViewById(R.id.rv_data);
        mRvMediaFile.setLayoutManager(new GridLayoutManager(this, MediaActivity.GRID_COUNT));
        mTitleViewTop = findViewById(R.id.title_view_top);
        mTitleViewTop.mTvCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_media_down, 0);
    }

    @Override
    protected void initData() {

        mMediaFileData = new ArrayList<>();
        mAllMediaFolderData = new ArrayList<>();
        mMediaFileAdapter = new MediaFileAdapter(this, mMediaOption.isShowCamera, mMediaFileData, mCheckMediaData);
        mMediaFileAdapter.setHasStableIds(true);
        mRvMediaFile.setAdapter(mMediaFileAdapter);
        MediaHelper.create(this).loadMediaResolver(mMediaOption, this);
    }

    @Override
    protected void initEvent() {
        mTitleViewTop.setOnCenterClickListener(new TitleView.OnCenterClickListener() {
            @Override
            public void onCenterClick(@NonNull View view) {
                notifyFolderWindow();
            }
        });
    }

    private void notifyFolderWindow() {
        if (DataUtils.isListEmpty(mAllMediaFolderData)) {
            Toasts.showToast(getApplicationContext(), R.string.you_media_not_file);
            return;
        }
        if (mFolderPopupWindow == null) {
            mFolderPopupWindow = new FolderPopupWindow(MediaActivity.this, mAllMediaFolderData);
            mFolderPopupWindow.setOnClickFolderListener(new FolderPopupWindow.OnClickFolderListener() {
                @Override
                public void onClickItem(@NonNull View view, @NonNull MediaFolder folders) {
                    mMediaFileData.clear();
                    mMediaFileData.addAll(folders.fileData);
                    mMediaFileAdapter.notifyDataSetChanged();
                }

                @Override
                public void onDismiss() {
                    notifyFolderWindow();
                }
            });
        }
        if (!mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.showAsDropDown(mTitleViewTop);
            mTitleViewTop.mTvCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_media_up, 0);
        } else {
            mTitleViewTop.mTvCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_media_down, 0);
            mFolderPopupWindow.dismiss();
        }

    }


    @Override
    public void onMediaSucceed(@NonNull List<MediaFolder> data) {
        if (DataUtils.getListSize(data) > 0) {
            mAllMediaFolderData.addAll(data);
            mMediaFileData.addAll(data.get(0).fileData);
            mMediaFileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFolderPopupWindow != null && mFolderPopupWindow.isShowing()) {
            notifyFolderWindow();
        } else {
            super.onBackPressed();
        }
    }
}
