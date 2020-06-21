package com.xiaobai.media.weight;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class BaseRecyclerView extends RecyclerView {
    public BaseRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        Log.w("onScrollStateChanged--", state + "--");
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                if (state == 0) {
                    Glide.with(getContext()).resumeRequests();
                } else {
                    Glide.with(getContext()).pauseRequests();
                }
            }
        }

    }


}
