package com.xiaobai.media.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.R;
import com.xiaobai.media.adapter.PreviewCheckMediaAdapter;
import com.xiaobai.media.adapter.PreviewMediaAdapter;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.ParcelableManger;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.utils.ScreenUtils;
import com.xiaobai.media.weight.TitleView;
import com.xiaobai.media.weight.Toasts;

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
    private ArrayList<MediaFile> mCheckMediaFileData;
    private int mPreviewPosition;
    private PreviewMediaAdapter mPreviewAdapter;
    private MediaSelector.MediaOption mOptions;
    private boolean mIsOpenAnimation;
    private AnimatorSet mAnimationSet;
    private RecyclerView mRvCheck;
    private PreviewCheckMediaAdapter mCheckMediaAdapter;
    private ConstraintLayout mClBottom;
    public static final int RESULT_CODE_BACK = -2;

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
        mRvCheck = findViewById(R.id.rv_check);
        mRvCheck.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mClBottom = findViewById(R.id.cl_bottom);
    }

    @Override
    protected void initData() {
        initIntent();
        initPreview();
        initCheckMedia();

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
        mCheckMediaFileData = intent.getParcelableArrayListExtra(ObjectActivity.KEY_PARCELABLE_LIST_CHECK_DATA);
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
        updateCheckView();
    }

    private void initCheckMedia() {
        mCheckMediaAdapter = new PreviewCheckMediaAdapter(this, mCheckMediaFileData, mMediaFileData, mPreviewPosition);
        mRvCheck.setAdapter(mCheckMediaAdapter);
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
                clickResultMediaData(mCheckMediaFileData);
            }
        });
        mTopTitleView.setOnBackViewClickListener(new TitleView.OnBackViewClickListener() {
            @Override
            public void onBackClick(@NonNull View view) {
                clickBackForResult();
            }
        });
        mBottomTitleView.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                if (DataUtils.getListSize(mCheckMediaFileData) >= mOptions.maxSelectorMediaCount) {
                    Toasts.showToast(PreviewActivity.this, R.string.max_selector_media_count, mOptions.maxSelectorMediaCount);
                    return;
                } else if (!mOptions.isSelectorMultiple && DataUtils.getListSize(mCheckMediaFileData) >= 1) {
                    MediaFile checkMedia = mCheckMediaFileData.get(0);
                    MediaFile mediaFile = mMediaFileData.get(mPreviewPosition);
                    if (mediaFile.mediaType != checkMedia.mediaType) {
                        Toasts.showToast(PreviewActivity.this, R.string.not_selector_video_and_image);
                        return;
                    }
                }
                updateCheckMediaData();
                updateCheckView();
                updateTitleSureText(mTopTitleView.mTvSure, mCheckMediaFileData, mOptions.maxSelectorMediaCount);
            }
        });
        mVpContent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mPreviewPosition = position;
                updatePreviewIndexView();
                if (mCheckMediaAdapter != null) {
                    mCheckMediaAdapter.notifyDataChange(mPreviewPosition);
                }
                updateCheckView();
            }
        });
        mCheckMediaAdapter.setOnClickItemListener(new PreviewCheckMediaAdapter.OnClickItemListener() {
            @Override
            public void onClickItemView(@NonNull View view, int position) {
                MediaFile checkMediaFile = mCheckMediaFileData.get(position);
                if (mMediaFileData.contains(checkMediaFile)) {
                    mVpContent.setCurrentItem(mMediaFileData.indexOf(checkMediaFile), false);
                }
            }
        });
    }

    /**
     * 更新选中的媒体数据
     */
    public void updateCheckMediaData() {
        MediaFile mediaFile = mMediaFileData.get(mPreviewPosition);
        if (mCheckMediaFileData.contains(mediaFile)) {
            mCheckMediaFileData.remove(mediaFile);
        } else {
            mCheckMediaFileData.add(mediaFile);
        }
        mCheckMediaAdapter.notifyDataChange(mPreviewPosition);
    }

    /**
     * TopTitleView 预览index
     */
    private void updatePreviewIndexView() {
        if (mVpContent != null && mTopTitleView != null) {
            mTopTitleView.mTvBack.setText(getString(R.string.preview_index_s, mVpContent.getCurrentItem() + 1 + "", mMediaFileData.size() + ""));
        }
    }

    /**
     * 更新BottomTitleView是不是被选中状态
     */
    public void updateCheckView() {
        if (mCheckMediaFileData.contains(mMediaFileData.get(mPreviewPosition))) {
            mBottomTitleView.mTvSure.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_check, 0, 0, 0);
        } else {
            mBottomTitleView.mTvSure.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_media_default, 0, 0, 0);
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
            bottomViewAnimation = ObjectAnimator.ofFloat(mClBottom, "translationY", 0, mClBottom.getMeasuredHeight());
            WindowManager.LayoutParams fullParams = getWindow().getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                fullParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            getWindow().setAttributes(fullParams);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            topViewAnimation = ObjectAnimator.ofFloat(mTopTitleView, "translationY", -(mTopTitleView.getMeasuredHeight() + ScreenUtils.getStatusWindowsHeight(this)), 0);
            bottomViewAnimation = ObjectAnimator.ofFloat(mClBottom, "translationY", mClBottom.getMeasuredHeight(), 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        mAnimationSet.setDuration(500);
        mAnimationSet.setInterpolator(new LinearInterpolator());
        mAnimationSet.playTogether(topViewAnimation, bottomViewAnimation);
        mAnimationSet.start();
    }

    private void clickBackForResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ObjectActivity.KEY_PARCELABLE_LIST_CHECK_DATA, mCheckMediaFileData);
        setResult(PreviewActivity.RESULT_CODE_BACK, intent);
    }

    @Override
    public void onBackPressed() {
        clickBackForResult();
        super.onBackPressed();

    }
}
