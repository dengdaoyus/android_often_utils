package com.util.utilslibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:侧滑关闭 Activity 的控件,推荐 BaseActivity 中使用该控件
 * 然后不需要侧滑的 Activity 调用 unbindActivity() 方法即可
 * 如果有希望不被拦截 Touch 事件的 View 也可以调用 addNotInterceptView(View view) 添加
 * Created by 禽兽先生
 * Created on 2018/1/24
 */

public class SlideBackLayout extends FrameLayout {
    private boolean startSlide = false; //是否开始侧滑动作的标志位
    private float lastX; //最后一次触摸事件的坐标
    private Activity activity;  //需要关闭的 Activity
    private List<ViewPager> viewPagerList;  //存放当前布局中的 ViewPager
    private List<View> notInterceptViewList;    //存放希望不被拦截 Touch 事件的 View

    public SlideBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置容器宽度为最大宽度,高度为最大高度
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置一个默认灰色背景,因为该控件必须设置 Activity 的主题为透明,如果布局中没有设置背景色的话,那么没有控件的部分会透明
        //显示下层的 Activity,体验不舒服,这里就设置一个默认背景
        setBackgroundColor(Color.argb(255, 250, 250, 250));
        viewPagerList = new ArrayList<>();
        notInterceptViewList = new ArrayList<>();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //如果没有绑定 Activity,那就该干嘛干嘛
        if (activity == null) {
            return super.onInterceptTouchEvent(event);
        }
        //当当前布局中存在 ViewPager 并且该 ViewPager 的当前页卡不是第一个时,让 ViewPager 先获得触摸事件
        if (!viewPagerList.isEmpty()) {
            for (int i = 0; i < viewPagerList.size(); i++) {
                if (viewPagerList.get(i).getCurrentItem() != 0) {
                    return super.onInterceptTouchEvent(event);
                }
            }
        }
        //当有自定义的不希望被拦截 Touch 事件的 View 时,如果当前触摸的位置在该控件区域内,不拦截触摸事件
        if (!notInterceptViewList.isEmpty()) {
            for (int i = 0; i < notInterceptViewList.size(); i++) {
                View mView = notInterceptViewList.get(i);
                //获取不希望被拦截触摸事件的控件在屏幕上的位置
                int[] location = new int[2];
                mView.getLocationOnScreen(location);
                //当触摸点在该控件区域内时,不拦截触摸事件
                if (event.getX() >= location[0] && event.getX() <= location[0] + mView.getWidth()
                        && event.getY() >= location[1] && event.getY() <= location[1] + mView.getHeight()) {
                    return super.onInterceptTouchEvent(event);
                }
            }
        }
        //OK,如果上面的条件都不满足,那么在触摸点的横坐标小于屏幕宽度的十分之一时就拦截触摸事件,不下发了,交给自己的 onTouchEvent() 处理
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < getMeasuredWidth() / 10) {
                    //当触摸点的横坐标小于屏幕宽度的十分之一,设置 startSlide,代表要开始侧滑动作了
                    startSlide = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果没有绑定 Activity,那就该干嘛干嘛
        if (activity == null) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < getMeasuredWidth() / 10) {
                    lastX = event.getRawX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (startSlide) {
                    float distanceX = event.getRawX() - lastX;
                    //控件将要移动到的位置
                    float nextX = getX() + distanceX;
                    setX(nextX);
                    //移动完之后记录当前坐标
                    lastX = event.getRawX();
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时,如果横坐标超过屏幕的一半,那就关闭绑定的 Activity,否则恢复初始状态,并重置标志位
                if (startSlide && event.getRawX() > getMeasuredWidth() / 2) {
                    setX(getMeasuredWidth());
                    //Finish activity
                    activity.finish();
                } else {
                    setX(0);
                }
                startSlide = false;
                break;
        }
        return true;
    }

    /**
     * Description:绑定 Activity,获取该 Activity 的根视图
     * 该根视图包含一个子 View,是一个 LinearLayout,包含 TitleBar 和 Content(详情请移步 Android 群英传第 3 章)
     * 然后将其从根视图上移除掉,添加到该控件上,然后将该控件添加到 Activity 的根视图中,实现偷梁换柱的目的
     * Date:2018/1/25
     */
    public void bindActivity(Activity activity) {
        this.activity = activity;
        ViewGroup mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        View mRootView = mDecorView.getChildAt(0);
        mDecorView.removeView(mRootView);
        addView(mRootView);
        mDecorView.addView(this);
        checkHasViewPager(this);
    }

    /**
     * Description:取消绑定
     * Date:2018/1/25
     */
    public void unbindActivity() {
        this.activity = null;
    }

    /**
     * Description:递归检查当前的根视图中是否包含 ViewPager
     * Date:2018/1/25
     */
    private void checkHasViewPager(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewGroup.getChildAt(i) instanceof ViewPager) {
                viewPagerList.add((ViewPager) viewGroup.getChildAt(i));
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                checkHasViewPager((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    /**
     * Description:提供给外部的方法,添加希望不被拦截 Touch 事件的 View
     * Date:2018/1/25
     */
    public void addNotInterceptView(View view) {
        notInterceptViewList.add(view);
    }
}
