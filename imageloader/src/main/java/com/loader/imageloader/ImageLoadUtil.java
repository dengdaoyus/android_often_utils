package com.loader.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.function.Consumer;


/**
 * Description:图片加载工具类
 * Created by 禽兽先生
 * Created on 2017/10/12
 */

public class ImageLoadUtil {
    //单例模式
    private static ImageLoadUtil sImageLoadUtil;
    //图片加载策略
    private IBaseImageLoadStrategy mImageLoadStrategy;

    /**
     * Description:获取单例
     * Date:2017/10/12
     */
    public static ImageLoadUtil getInstance() {
        if (sImageLoadUtil == null) {
            synchronized (ImageLoadUtil.class) {
                if (sImageLoadUtil == null) {
                    sImageLoadUtil = new ImageLoadUtil();
                }
            }
        }
        return sImageLoadUtil;
    }

    private ImageLoadUtil() {
        //默认选择 Fresco 图片加载框架
        mImageLoadStrategy = new GlideImageLoadStrategy();
    }

    /**
     * Description:设置图片加载策略
     * Date:2017/10/12
     */
    public void setImageLoaderStrategy(IBaseImageLoadStrategy imageLoaderStrategy) {
        this.mImageLoadStrategy = imageLoaderStrategy;
    }

    /**
     * Description:加载 jpg 格式的图片
     * Date:2017/10/12
     */
    public void loadJpg(Context context, String url, ImageView imageView) {
        mImageLoadStrategy.loadJpg(context, url, imageView);
    }

    /**
     * Description:加载 gif 格式的图片
     * Date:2017/10/12
     */
    public void loadGif(Context context, String url, ImageView imageView) {
        mImageLoadStrategy.loadGif(context, url, imageView);
    }

    public void getJpg(Context context, String url, Consumer<Bitmap> consumer) {
        mImageLoadStrategy.getJpg(context, url, consumer);
    }
}
