package com.loader.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.function.Consumer;


/**
 * Description:使用 Glide 库来加载图片的策略
 * Created by 禽兽先生
 * Created on 2017/11/13
 */

public class GlideImageLoadStrategy implements IBaseImageLoadStrategy {
    @Override
    public void loadJpg(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadGif(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    @Override
    public void getJpg(Context context, String url, final Consumer<Bitmap> consumer) {
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    consumer.accept(resource);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
