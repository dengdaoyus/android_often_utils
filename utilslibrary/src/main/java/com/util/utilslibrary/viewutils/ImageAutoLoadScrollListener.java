package com.util.utilslibrary.viewutils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;


/**
 * 监听滚动来对图片加载进行判断处理
 * Created by Administrator on 2017/5/26 0026.
 */

public class ImageAutoLoadScrollListener extends RecyclerView.OnScrollListener {
    private Context mContext;
    private Fragment mFragment;

    public ImageAutoLoadScrollListener(Context context, OnScrollListener onScrollListener) {
        this.mContext = context;
        this.onScrollListener = onScrollListener;
    }

    public ImageAutoLoadScrollListener(Context context) {
        this.mContext = context;

    }

    public ImageAutoLoadScrollListener(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(newState);
        }
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //当屏幕停止滚动，加载图片
                try {
                    if (mContext != null) {
                        Glide.with(mContext).resumeRequests();
                    } else {
                        if (mFragment != null) {
                            Glide.with(mFragment).resumeRequests();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                try {
                    if (mContext != null) {
                        Glide.with(mContext).pauseRequests();
                    } else {
                        if (mFragment != null) {
                            Glide.with(mFragment).pauseRequests();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                try {
                    if (mContext != null) {
                        Glide.with(mContext).pauseRequests();
                    } else {
                        if (mFragment != null) {
                            Glide.with(mFragment).pauseRequests();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private OnScrollListener onScrollListener;

    public interface OnScrollListener {
        void onScrollStateChanged(int newState);
    }
}
