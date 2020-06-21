package com.xiaobai.media.weight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.core.graphics.drawable.DrawableCompat;

import com.xiaobai.media.R;
import com.xiaobai.media.bean.MediaFolder;

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

    public FolderPopupWindow(Context context, List<MediaFolder> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void init() {
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        View inflateView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_folder_view, null);
        BaseRecyclerView mRvContent = inflateView.findViewById(R.id.brv_folder);

    }
}
