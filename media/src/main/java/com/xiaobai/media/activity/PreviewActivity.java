package com.xiaobai.media.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.R;
import com.xiaobai.media.adapter.PreviewMediaAdapter;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.ParcelableManger;
import com.xiaobai.media.utils.ScreenUtils;
import com.xiaobai.media.weight.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/19 19:26
 * 更新时间: 2020/8/19 19:26
 * 描述:
 */
public class PreviewActivity extends ObjectActivity {

    private TitleView mTopTitleView, mBottomTitleView;
    private ViewPager2 mVpContent;
    private List<MediaFile> mMediaFileData;
    private List<MediaFile> mCheckMediaFileData;
    private int mPreviewPosition;
    private PreviewMediaAdapter mPreviewAdapter;
    private MediaSelector.MediaOption mOptions;
    private boolean mIsOpenAnimation;
    private AnimatorSet mAnimationSet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        mTopTitleView = findViewById(R.id.top_title_view);
        ScreenUtils.setMarginStatusView(mTopTitleView);
        mBottomTitleView = findViewById(R.id.bottom_title_view);
        mVpContent = findViewById(R.id.vp_content);
        mVpContent.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    @Override
    protected void initData() {
        initIntent();
        initPreview();

    }

    private void initIntent() {
        ParcelableManger manger = ParcelableManger.get();
        Object object = manger.getData();
        if (object instanceof List) {
            mMediaFileData = (List<MediaFile>) object;
        } else {
            mMediaFileData = new ArrayList<>();
        }
        Intent intent = getIntent();
        mCheckMediaFileData = intent.getParcelableArrayListExtra(ObjectActivity.KEY_PARCELABLE_CHECK_DATA);
        mPreviewPosition = intent.getIntExtra(ObjectActivity.KEY_INDEX_CHECK_POSITION, 0);
        mOptions = intent.getParcelableExtra(ObjectActivity.KEY_MEDIA_OPTION);
        Log.w("PreviewActivity--", mPreviewPosition + "--");
    }


    private void initPreview() {
        updateTitleSureText(mTopTitleView.mTvSure, mCheckMediaFileData, mOptions.maxSelectorMediaCount);

        mPreviewAdapter = new PreviewMediaAdapter(this, mMediaFileData);
        mVpContent.setAdapter(mPreviewAdapter);
        mVpContent.setPageTransformer(new PreviewTransformer(mVpContent));
        mVpContent.setOffscreenPageLimit(3);
        mVpContent.setCurrentItem(mPreviewPosition, false);
        updatePreviewIndexView();
    }

    @Override
    protected void initEvent() {
        mPreviewAdapter.setOnClickPreviewMediaListener(new PreviewMediaAdapter.OnClickPreviewMediaListener() {
            @Override
            public void onClickVideo(@NonNull View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mMediaFileData.get(position).filePath), "video/*");
                startActivity(intent);
            }

            @Override
            public void onClickPager(@NonNull View view, int position) {
                mIsOpenAnimation = !mIsOpenAnimation;
                updateTitleViewAnimation();
            }
        });
        mTopTitleView.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {

            }
        });
        mBottomTitleView.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                updateTitleSureText(mTopTitleView.mTvSure, mCheckMediaFileData, mOptions.maxSelectorMediaCount);
            }
        });
        mVpContent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                updatePreviewIndexView();
            }
        });

    }

    private void updatePreviewIndexView() {
        if (mVpContent != null && mTopTitleView != null) {
            mTopTitleView.mTvBack.setText(getString(R.string.preview_index_s, mVpContent.getCurrentItem() + 1 + "", mMediaFileData.size() + ""));
        }
    }

    private static class PreviewTransformer implements ViewPager2.PageTransformer {
        private ViewPager2 mVewPager2;

        public PreviewTransformer(ViewPager2 viewPager2) {
            this.mVewPager2 = viewPager2;
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            int leftInScreen = page.getLeft() - mVewPager2.getScrollX();
            float offsetRate = (float) leftInScreen * 0.08f / mVewPager2.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                page.setScaleX(scaleFactor);
            }
        }
    }

    public void updateTitleViewAnimation() {
        ObjectAnimator topViewAnimation, bottomViewAnimation;

        if (mAnimationSet == null) {
            mAnimationSet = new AnimatorSet();
        }
        if (mAnimationSet.isRunning()) {
            mAnimationSet.cancel();
        }
        if (mIsOpenAnimation) {
            topViewAnimation = ObjectAnimator.ofFloat(mTopTitleView, "translationY", 0, -(mTopTitleView.getMeasuredHeight() + ScreenUtils.getStatusWindowsHeight(this)));
            bottomViewAnimation = ObjectAnimator.ofFloat(mBottomTitleView, "translationY", 0, mBottomTitleView.getMeasuredHeight());
            WindowManager.LayoutParams fullParams = getWindow().getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                fullParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            getWindow().setAttributes(fullParams);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            topViewAnimation = ObjectAnimator.ofFloat(mTopTitleView, "translationY", -(mTopTitleView.getMeasuredHeight() + ScreenUtils.getStatusWindowsHeight(this)), 0);
            bottomViewAnimation = ObjectAnimator.ofFloat(mBottomTitleView, "translationY", mBottomTitleView.getMeasuredHeight(), 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        mAnimationSet.setDuration(500);
        mAnimationSet.setInterpolator(new LinearInterpolator());
        mAnimationSet.playTogether(topViewAnimation, bottomViewAnimation);
        mAnimationSet.start();
    }
}
