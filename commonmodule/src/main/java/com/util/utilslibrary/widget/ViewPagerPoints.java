package com.util.utilslibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.util.utilslibrary.R;


/**
 * Description:可以与 ViewPager 同步的小圆点
 * 使用示例:
 * <p>
 * <com.qinshou.toollib.widget.ViewPagerPoints
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:count="6"
 * app:currentItem="1"
 * app:currentItemBg="@drawable/point_selected"
 * app:currentItemHeight="20dp"
 * app:currentItemWidth="20dp"
 * app:itemBg="@drawable/point_normal"
 * app:itemHeight="15dp"
 * app:itemWidth="15dp"
 * app:spacing="5dp">
 * <p>
 * Created by 禽兽先生
 * Created on 2017/8/30
 */

public class ViewPagerPoints extends LinearLayout {
    private Context mContext;
    private int count;
    private int currentItem;
    private int itemBackground;
    private int currentItemBackground;
    private int itemWidth;
    private int itemHeight;
    private int currentItemWidth;
    private int currentItemHeight;
    private int spacing;

    private LayoutParams itemParams;
    private LayoutParams currentItemParams;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        initViews(mContext);
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        if (currentItem < 0 || currentItem > getChildCount() - 1)
            return;
        this.currentItem = currentItem;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setLayoutParams(itemParams);
            getChildAt(i).setBackgroundResource(itemBackground);
        }
        getChildAt(currentItem).setLayoutParams(currentItemParams);
        getChildAt(currentItem).setBackgroundResource(currentItemBackground);
    }

    public ViewPagerPoints(Context context) {
        this(context, null);
    }

    public ViewPagerPoints(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerPoints(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(context, attrs);
        initViews(context);
    }

    /**
     * Description:获取自定义属性
     * Date:2017/8/30
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerPoints);
        count = mTypeArray.getInt(R.styleable.ViewPagerPoints_count, 5);
        currentItem = mTypeArray.getInt(R.styleable.ViewPagerPoints_currentItem, 0);
        itemBackground = mTypeArray.getResourceId(R.styleable.ViewPagerPoints_itemBg, R.drawable.viewpager_point_normal);
        currentItemBackground = mTypeArray.getResourceId(R.styleable.ViewPagerPoints_currentItemBg, R.drawable.viewpager_point_selected);
        itemWidth = (int) mTypeArray.getDimension(R.styleable.ViewPagerPoints_itemWidth, 30);
        itemHeight = (int) mTypeArray.getDimension(R.styleable.ViewPagerPoints_itemHeight, 30);
        currentItemWidth = (int) mTypeArray.getDimension(R.styleable.ViewPagerPoints_currentItemWidth, 30);
        currentItemHeight = (int) mTypeArray.getDimension(R.styleable.ViewPagerPoints_currentItemHeight, 30);
        spacing = (int) mTypeArray.getDimension(R.styleable.ViewPagerPoints_spacing, 15);
        mTypeArray.recycle();
    }

    private void initViews(Context context) {
        currentItem = 0;
        removeAllViews();
        setGravity(Gravity.CENTER);
        itemParams = new LayoutParams(itemWidth, itemHeight);
        itemParams.setMargins(spacing, 0, spacing, 0);
        currentItemParams = new LayoutParams(currentItemWidth, currentItemHeight);
        currentItemParams.setMargins(spacing, 0, spacing, 0);
        for (int i = 0; i < count; i++) {
            ImageView ivPoint = new ImageView(context);
            ivPoint.setLayoutParams(itemParams);
            ivPoint.setBackgroundResource(itemBackground);
            addView(ivPoint);
        }
        getChildAt(currentItem).setLayoutParams(currentItemParams);
        getChildAt(currentItem).setBackgroundResource(currentItemBackground);
    }

    float x = 0;
    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                if (event.getX() > 0 && event.getX() < getMeasuredWidth()
                        && event.getY() > 0 && event.getY() < getMeasuredHeight()) {
                    if (Math.abs(event.getX() - x) > getMeasuredWidth() / 10) {
                        if (event.getX() - x > 0) {
                            setCurrentItem(getCurrentItem() - 1);
                        } else {
                            setCurrentItem(getCurrentItem() + 1);
                        }
                        x = 0;
                        y = 0;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Description:与 ViewPager 状态同步
     * Date:2017/9/22
     */
    public void setupWithViewPager(ViewPager viewPager) {
        if (mOnPageChangeListener == null) {
            mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
            viewPager.addOnPageChangeListener(mOnPageChangeListener);
        }
    }
}
