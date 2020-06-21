package com.xiaobai.medias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.xiaobai.media.MediaSelector;
import com.xiaobai.media.activity.MediaActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaSelector.MediaOption option = new MediaSelector.MediaOption();
        option.isShowCamera = true;
        MediaSelector.with(this).buildMediaOption(option).openActivity();
     //   startActivity(new Intent(this, MediaActivity.class));
    }
}