package com.xiaobai.media.weight;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xiaobai.media.R;
import com.xiaobai.media.adapter.MediaFolderAdapter;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.utils.PhoneUtils;
import com.xiaobai.media.utils.ScreenUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/21 11:51
 * 更新时间: 2020/6/21 11:51
 * 描述:
 */
public class FolderPopupWindow {
    private Context mContext;
    private List<MediaFolder> mData;
    private PopupWindow mPopupWindow;
    private BaseRecyclerView mRvContent;
    private ObjectAnimator mFolderAnimation;

    public void setOnClickFolderListener(OnClickFolderListener onClickFolderListener) {
        this.onClickFolderListener = onClickFolderListener;
    }

    private OnClickFolderListener onClickFolderListener;

    public FolderPopupWindow(Context context, List<MediaFolder> data) {
        this.mContext = context;
        this.mData = data;
        init();
    }

    public void init() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View inflateView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_folder_view, (ViewGroup) ((Activity) mContext).getWindow().getDecorView(), false);
        mPopupWindow.setContentView(inflateView);
        mRvContent = inflateView.findViewById(R.id.brv_folder);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        MediaFolderAdapter folderAdapter = new MediaFolderAdapter(mData);
        folderAdapter.setOnFolderClickListener(new MediaFolderAdapter.OnFolderClickListener() {
            @Override
            public void onClickItem(@NonNull View view, int position) {
                if (onClickFolderListener!=null){
                    onClickFolderListener.onDismiss();
                }
            }

            @Override
            public void onClickUpdate(@NonNull View view, int position) {
                if (onClickFolderListener!=null){
                    onClickFolderListener.onClickItem(view,mData.get(position));
                }
            }
        });
        mRvContent.setAdapter(folderAdapter);
        mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.color80000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setClippingEnabled(false);
        // mPopupWindow.setTouchable(true);
        // mPopupWindow.setFocusable(true);
        // mPopupWindow.setAnimationStyle(R.style.DefaultDialogAnimation);

    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void showAsDropDown(@NonNull View view) {
       /*int height = ScreenUtils.screenHeight(mContext) - view.getHeight() - ScreenUtils.dp2px(mContext, 120);
        mPopupWindow.setHeight(height);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPopupWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM);
        } else {
            mPopupWindow.showAsDropDown(view);
        }
        // mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0,-50);
    }

    public void dismiss() {
        //folderAnimation(false);
        mPopupWindow.dismiss();
    }

    public void folderAnimation(boolean isShow) {
        if (mFolderAnimation != null && mFolderAnimation.isRunning()) {
            mFolderAnimation.cancel();
        }
        if (isShow) {
            mFolderAnimation = ObjectAnimator.ofFloat(mRvContent, "translationY", mRvContent.getTranslationY(), mRvContent.getHeight());
        } else {
            mFolderAnimation = ObjectAnimator.ofFloat(mRvContent, "translationY", mRvContent.getHeight(), 0);
        }
        mFolderAnimation.setDuration(300);
        mFolderAnimation.setInterpolator(new BounceInterpolator());
        mFolderAnimation.start();
    }

    public interface OnClickFolderListener {
        void onClickItem(@NonNull View view, MediaFolder folders);
        void onDismiss();
    }
}
