package com.loader.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.function.Consumer;


/**
 * Description:图片加载策略的基类
 * Created by 禽兽先生
 * Created on 2017/10/12
 */

public interface IBaseImageLoadStrategy {
    void loadJpg(Context context, String url, ImageView imageView);

    void loadGif(Context context, String url, ImageView imageView);

    void getJpg(Context context, String url, Consumer<Bitmap> consumer);
}
