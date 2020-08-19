package com.xiaobai.media.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.xiaobai.media.R;
import com.xiaobai.media.adapter.PreviewMediaAdapter;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.manger.ParcelableManger;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        mTopTitleView = findViewById(R.id.top_title_view);
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
        mCheckMediaFileData = intent.getParcelableExtra(ObjectActivity.KEY_PARCELABLE_CHECK_DATA);
        mPreviewPosition = intent.getIntExtra(ObjectActivity.KEY_INDEX_CHECK_POSITION, 0);
        Log.w("PreviewActivity--", mPreviewPosition + "--");
    }

    private void initPreview() {
        PreviewMediaAdapter mPreviewAdapter = new PreviewMediaAdapter(this, mMediaFileData);
        mVpContent.setAdapter(mPreviewAdapter);
        mVpContent.setPageTransformer(new PreviewTransformer(mVpContent));
        mVpContent.setOffscreenPageLimit(3);
        mVpContent.setCurrentItem(mPreviewPosition, false);
    }

    @Override
    protected void initEvent() {

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
}
