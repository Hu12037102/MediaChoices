package com.xiaobai.media.weight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public FolderPopupWindow(Context context, List<MediaFolder> data) {
        this.mContext = context;
        this.mData = data;
        init();
    }

    public void init() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View inflateView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_folder_view, null);
        mPopupWindow.setContentView(inflateView);
        mRvContent = inflateView.findViewById(R.id.brv_folder);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        MediaFolderAdapter folderAdapter = new MediaFolderAdapter(mData);
        mRvContent.setAdapter(folderAdapter);
        mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.color80000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setClippingEnabled(false);
      // mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);

    }

    public void showAsDropDown(@NonNull View view) {
       /*int height = ScreenUtils.screenHeight(mContext) - view.getHeight() - ScreenUtils.dp2px(mContext, 120);
        mPopupWindow.setHeight(height);*/
        mPopupWindow.showAsDropDown(view);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
