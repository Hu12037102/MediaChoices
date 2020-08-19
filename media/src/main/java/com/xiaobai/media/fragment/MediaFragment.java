package com.xiaobai.media.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.OnLoadMediaCallback;
import com.xiaobai.media.R;
import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.bean.MediaFolder;
import com.xiaobai.media.resolver.MediaHelper;
import com.xiaobai.media.utils.DataUtils;
import com.xiaobai.media.weight.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/19 10:24
 * 更新时间: 2020/8/19 10:24
 * 描述:
 */
public class MediaFragment extends Fragment implements OnLoadMediaCallback {

    private View mInflateView;
    private BaseRecyclerView mBrvData;
    public static final int GRID_COUNT = 4;
    private MediaSelector.MediaOption mMediaOption;
    private List<MediaFile> mMediaFileData;

    private MediaFragment(MediaSelector.MediaOption option) {
        this.mMediaOption = option;
    }

    public static MediaFragment create(MediaSelector.MediaOption option) {
        return new MediaFragment(option);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflateView = inflater.inflate(R.layout.fragment_media, container, false);
        return mInflateView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }


    private void initView() {
        mBrvData = mInflateView.findViewById(R.id.brv_data);
        mBrvData.setLayoutManager(new GridLayoutManager(getContext(), MediaFragment.GRID_COUNT));

    }

    private void initData() {
        mMediaFileData = new ArrayList<>();
        MediaHelper.create(Objects.requireNonNull(getActivity())).loadMediaResolver(mMediaOption, this);
    }

    private void initEvent() {

    }

    @Override
    public void onMediaSucceed(@NonNull List<MediaFolder> data) {
        if (DataUtils.getListSize(data) > 0) {
            mMediaFileData.addAll(data.get(0).fileData);

        }
    }
}
