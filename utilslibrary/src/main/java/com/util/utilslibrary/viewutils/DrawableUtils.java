package com.util.utilslibrary.viewutils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

/**
 * 资源图片设置  上下左右
 * Created by Administrator on 2017/9/19 0019.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class DrawableUtils {
    /**
     * 设置左边的drawable
     *
     * @param tv
     * @param mContext
     * @param srcId
     */

    public static void setDrawableLeft(View tv, Context mContext, int backId, int srcId) {
        tv.setBackground(mContext.getResources().getDrawable(backId));
        Drawable drawable = mContext.getResources().getDrawable(srcId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) tv).setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置右边的drawable
     *
     * @param tv
     * @param mContext
     * @param srcId
     */
    public static void setDrawableRigth(View tv, Context mContext, int backId, int srcId) {
        tv.setBackground(mContext.getResources().getDrawable(backId));
        Drawable drawable = mContext.getResources().getDrawable(srcId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) tv).setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置左边的drawable
     *
     * @param tv
     * @param mContext
     * @param srcId
     */
    public static void setDrawableLeft(View tv, Context mContext, int srcId) {
        Drawable drawable = mContext.getResources().getDrawable(srcId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) tv).setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置右边的drawable
     *
     * @param tv
     * @param mContext
     * @param srcId
     */
    public static void setDrawableRigth(View tv, Context mContext, int srcId) {

        Drawable drawable = mContext.getResources().getDrawable(srcId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) tv).setCompoundDrawables(null, null, drawable, null);
    }
}
