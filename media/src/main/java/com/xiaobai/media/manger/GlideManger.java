package com.xiaobai.media.manger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xiaobai.media.R;

import java.io.File;


/**
 * 作者: 胡庆岭
 * 创建时间: 2020/4/10 17:17
 * 更新时间: 2020/4/10 17:17
 * 描述: Glide图片处理加载类
 */
public class GlideManger {
    private static final int DIS_CACHE_SIZE = 50 * 1024 * 1024;
    private Context mApplicationContext;
    private static final String GLIDE_CACHE = "glide_cache";

    private GlideManger(Context context) {
        this.mApplicationContext = context.getApplicationContext();
        if (mGlideManger != null) {
            throw new RuntimeException("this not is null");
        }
        initGlide();
    }

    private static GlideManger mGlideManger;

    public static GlideManger get(@NonNull Context context) {
        synchronized (GlideManger.class) {
            if (mGlideManger == null) {
                synchronized (GlideManger.class) {
                    mGlideManger = new GlideManger(context);
                }
            }
        }
        return mGlideManger;
    }

    private String initCacheFilePath() {
        File file = new File(mApplicationContext.getCacheDir(), GLIDE_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    @SuppressLint("VisibleForTests")
    private void initGlide() {
        RequestOptions defaultOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .skipMemoryCache(false);
        LruResourceCache lruResourceCache = new LruResourceCache(Runtime.getRuntime().maxMemory() / 8);

        DiskLruCacheFactory diskLruCacheFactory = new DiskLruCacheFactory(initCacheFilePath(), GlideManger.DIS_CACHE_SIZE);

        GlideBuilder builder = new GlideBuilder()
                .setDefaultRequestOptions(defaultOptions)
                .setMemoryCache(lruResourceCache)
                .setDiskCache(diskLruCacheFactory)
                .setLogLevel(Log.WARN);
        Glide.init(mApplicationContext, builder);
        // Glide.init(UIUtils.getBaseContext(), );
    }

    public void loadImage(Object o, ImageView imageView) {
        if (o == null || imageView == null) {
            return;
        }
        Glide.with(imageView).load(o).placeholder(R.color.colorPlaceholder).error(R.color.colorError).into(imageView);
    }

    public void loadRoundImage(Object o, ImageView imageView, int radius) {
        if (o == null || imageView == null) {
            return;
        }
        Glide.with(imageView.getContext()).load(o).apply(createRoundedRequestOptions(radius)).placeholder(R.drawable.shape_placeholder_error_radous_view).error(R.drawable.shape_placeholder_error_radous_view).into(imageView);
    }

    public void loadPreviewImage(Object o, ImageView imageView) {
        if (o == null || imageView == null) {
            return;
        }
        Glide.with(imageView).load(o).placeholder(R.color.colorPlaceholder).error(R.color.colorError).centerInside().override(imageView.getContext().getResources().getDisplayMetrics().widthPixels, Target.SIZE_ORIGINAL).into(imageView);
    }

    private RequestOptions createRoundedRequestOptions(int radius) {
        return new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(radius));
    }
}
