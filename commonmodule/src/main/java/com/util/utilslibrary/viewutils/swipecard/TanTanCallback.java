package com.util.utilslibrary.viewutils.swipecard;


import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.util.utilslibrary.R;

import java.util.List;

import static com.util.utilslibrary.viewutils.swipecard.CardConfig.MAX_SHOW_COUNT;
import static com.util.utilslibrary.viewutils.swipecard.CardConfig.SCALE_GAP;
import static com.util.utilslibrary.viewutils.swipecard.CardConfig.TRANS_Y_GAP;


/**
 * 探探效果定制的Callback
 */
public class TanTanCallback extends RenRenCallback {
    private static final int MAX_ROTATION = 5;
    //判断 此次滑动方向是否是竖直的 ，水平方向上的误差(阈值，默认我给了50dp)
    private int mHorizontalDeviation;
    private MOUSCallBack mousCallBack;

    public TanTanCallback(RecyclerView rv, BaseQuickAdapter adapter, List datas, MOUSCallBack mousCallBack) {
        super(rv, adapter, datas);
        this.mousCallBack = mousCallBack;
        mHorizontalDeviation = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, mRv.getContext().getResources().getDisplayMetrics());
    }

    public TanTanCallback(int dragDirs, int swipeDirs, RecyclerView rv, BaseQuickAdapter adapter, List datas) {
        super(dragDirs, swipeDirs, rv, adapter, datas);
    }

    public int getHorizontalDeviation() {
        return mHorizontalDeviation;
    }

    public TanTanCallback setHorizontalDeviation(int horizontalDeviation) {
        mHorizontalDeviation = horizontalDeviation;
        return this;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        if (isTopViewCenterInHorizontal(viewHolder.itemView)) {
            return Float.MAX_VALUE;
        }
        return super.getSwipeThreshold(viewHolder);
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        View topView = mRv.getChildAt(mRv.getChildCount() - 1);
        if (isTopViewCenterInHorizontal(topView)) {
            return Float.MAX_VALUE;
        }
        return super.getSwipeEscapeVelocity(defaultValue);
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {

        View topView = mRv.getChildAt(mRv.getChildCount() - 1);
        if (isTopViewCenterInHorizontal(topView)) {
            return Float.MAX_VALUE;
        }
        return super.getSwipeVelocityThreshold(defaultValue);
    }

    //返回TopView此时在水平方向上是否是居中的
    private boolean isTopViewCenterInHorizontal(View topView) {
        return Math.abs(mRv.getWidth() / 2 - topView.getX() - (topView.getWidth() / 2)) < mHorizontalDeviation;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        super.onSwiped(viewHolder, direction);
        //如果不需要循环删除
//Object remove = mDatas.remove(viewHolder.getLayoutPosition());
//        mAdapter.notifyDataSetChanged();
//        Log.e("swipecard", "厉害了");

        if (isLeftSwipe) {
            if (mousCallBack != null) {
                mousCallBack.onLeftCard(viewHolder.getLayoutPosition());
            }
        } else {
            if (mousCallBack != null) {
                mousCallBack.onRightCard(viewHolder.getLayoutPosition());
            }
        }
        //探探只是第一层加了rotate & alpha的操作
        //对rotate进行复位
        viewHolder.itemView.setRotation(0);

        //自己感受一下吧 Alpha
//        if (viewHolder instanceof RecyclerView.ViewHolder) {
//            RecyclerView.ViewHolder holder = viewHolder;
//            holder.setAlpha(R.id.iv_love, 0);
//            holder.setAlpha(R.id.iv_del, 0);
//        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //探探的效果
        double swipValue = Math.sqrt(dX * dX + dY * dY);
        double fraction = swipValue / getThreshold(viewHolder);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = childCount - i - 1;
            if (level > 0) {
                child.setScaleX((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));

                if (level < MAX_SHOW_COUNT - 1) {
                    child.setScaleY((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));
                    child.setTranslationY((float) (TRANS_Y_GAP * level - fraction * TRANS_Y_GAP));
                }
            } else {
                //探探只是第一层加了rotate & alpha的操作
                //不过他区分左右
                float xFraction = dX / getThreshold(viewHolder);
                //边界修正 最大为1
                if (xFraction > 1) {
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                //rotate
                child.setRotation(xFraction * MAX_ROTATION);

                //自己感受一下吧 Alpha
//                if (viewHolder != null) {
//                    RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) viewHolder;
//                    if (dX > 0) {
//                        //露出左边，比心
//                        holder.setAlpha(R.id.iv_love, xFraction);
//                    } else if (dX<0){
//                        //露出右边，滚犊子
//                        holder.setAlpha(R.id.iv_del, -xFraction);
//                    }else {
//                        holder.setAlpha(R.id.iv_love, 0);
//                        holder.setAlpha(R.id.iv_del, 0);
//                    }
//
//
//                }
            }
        }
        //可以在此判断左右滑：
        float v = mRv.getWidth() / 2 - viewHolder.itemView.getX() - (viewHolder.itemView.getWidth() / 2);
        if (v > 0) {
            isLeftSwipe = true;
        } else if (v < 0) {
            isLeftSwipe = false;
        }


    }

    //一个flag 判断左右滑
    private boolean isLeftSwipe;

    public interface MOUSCallBack {
        void onLeftCard(int position);

        void onRightCard(int position);

        void onTopCard(int position);

        void onBottomCard(int position);
    }
}
