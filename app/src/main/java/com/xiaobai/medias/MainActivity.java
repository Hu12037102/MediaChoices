package com.xiaobai.medias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;
import com.xiaobai.media.permission.PermissionActivity;
import com.xiaobai.media.permission.imp.OnPermissionsResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends PermissionActivity implements CompoundButton.OnCheckedChangeListener {

    private RecyclerView mRvData;
    private CheckBox mCbCompress;
    private CheckBox mCbCrop;
    private AppCompatEditText mAetCount;
    private CheckBox mCbCamera;
    private CheckBox mCbVideo;
    private Button mBtnMedia;
    private MediaSelector.MediaOption mOption;
    private List<MediaFile> mData;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> allowPermissions) {
                init();

            }

            @Override
            public void onNoAllow(List<String> noAllowPermissions) {

            }

            @Override
            public void onForbid(List<String> noForbidPermissions) {

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        //   startActivity(new Intent(this, MediaActivity.class));

    }

    private void init() {
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mRvData = findViewById(R.id.rv_data);
        mRvData.setLayoutManager(new GridLayoutManager(this, 4));
        mCbCompress = findViewById(R.id.cb_compress);
        mCbCrop = findViewById(R.id.cb_crop);
        mAetCount = findViewById(R.id.act_count);
        mCbCamera = findViewById(R.id.cb_camera);
        mCbVideo = findViewById(R.id.cb_video);
        mBtnMedia = findViewById(R.id.btn_media);
    }

    private void initData() {
        mOption = new MediaSelector.MediaOption();
        mData = new ArrayList<>();
        mAdapter = new MainAdapter(this, mData);
        mRvData.setAdapter(mAdapter);
    }

    private void initEvent() {
        mCbCompress.setOnCheckedChangeListener(this);
        mCbCrop.setOnCheckedChangeListener(this);
        mCbCamera.setOnCheckedChangeListener(this);
        mCbVideo.setOnCheckedChangeListener(this);
        mBtnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption.maxSelectorMediaCount = TextUtils.isEmpty(mAetCount.getText()) ? 9 : Integer.parseInt(mAetCount.getText().toString());
                mOption.selectorFileData = (ArrayList<MediaFile>) mData;
                MediaSelector.with(MainActivity.this).buildMediaOption(mOption).openActivity();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_compress:
                mOption.isCompress = isChecked;
                break;
            case R.id.cb_crop:
                mOption.isCrop = isChecked;
                break;

            case R.id.cb_camera:
                mOption.isShowCamera = isChecked;
                break;
            case R.id.cb_video:
                mOption.mediaType = MediaSelector.MediaOption.MEDIA_ALL;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MediaSelector.REQUEST_CODE_MEDIA && resultCode == Activity.RESULT_OK) {
            List<MediaFile> list = MediaSelector.resultMediaData(data);
            mData.clear();
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}