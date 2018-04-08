package com.util.utilslibrary.viewutils;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 设置控件Params
 * Created by Administrator
 * on 2017/2/27 0027.
 */

public class ParamsUtils {

    public static void setParams(View view, int width, int height) {
        if (view instanceof ImageView) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.width = width;
            params.height = height;
        } else {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.width = width;
            params.height = height;
        }
    }

    public static void setParams(View view, int width) {

        if (view instanceof ImageView) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = width;
            params.height = width;
        }

    }

    public static void setParamsHeight(View view, int height) {
        if (view instanceof ImageView) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = height;
        } else  {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = height;
        }


    }

    public static void setParamsLayout(View view, int width) {
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = width + 8;
    }

}
