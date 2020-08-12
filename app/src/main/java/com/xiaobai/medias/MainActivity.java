package com.xiaobai.medias;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.permission.PermissionActivity;
import com.xiaobai.media.permission.imp.OnPermissionsResult;

import java.util.List;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> allowPermissions) {

                MediaSelector.MediaOption option = new MediaSelector.MediaOption();
                option.isShowCamera = true;
                MediaSelector.with(MainActivity.this).buildMediaOption(option).openActivity();
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
}