package com.xiaobai.media.weight;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;


/**
 * 作者: 胡庆岭
 * 创建时间: 2020/4/16 18:07
 * 更新时间: 2020/4/16 18:07
 * 描述:自定义吐司
 */
public class Toasts {

    public static void showToast(@NonNull Context context, @StringRes int resString) {
        context = context.getApplicationContext();
        Toast.makeText(context, resString, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@NonNull Context context, @StringRes int resString, Object obj) {
        context = context.getApplicationContext();
        Toast.makeText(context, context.getString(resString, obj), Toast.LENGTH_SHORT).show();
    }


    public static void showToast(@NonNull Context context, @NonNull String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        context = context.getApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
