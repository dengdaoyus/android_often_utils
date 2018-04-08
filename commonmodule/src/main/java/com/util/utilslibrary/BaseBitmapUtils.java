package com.util.utilslibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/4/8 0008.
 */

public class BaseBitmapUtils {
    /**
     * Description:Drawable 转 Bitmap
     * Date:2018/3/5
     */
    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap mBitmap = Bitmap.createBitmap(width, height, config);
        Canvas mCanvas = new Canvas(mBitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(mCanvas);
        return mBitmap;
    }
}
