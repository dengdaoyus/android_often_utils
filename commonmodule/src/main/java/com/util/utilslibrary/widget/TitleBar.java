package com.util.utilslibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.util.utilslibrary.R;


/**
 * Description:通用标题栏
 * 使用示例:
 * <p>
 * <com.qinshou.toollib.widget.TitleBar
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="#FF0000"
 * app:leftIcon="@drawable/icon_back"
 * app:leftMarginLeft="10dp"
 * app:leftText="左边"
 * app:leftTextColor="#FFFFFF"
 * app:leftTextSize="15sp"
 * app:rightIcon="@drawable/icon_more"
 * app:rightMarginRight="10dp"
 * app:rightText="右边"
 * app:rightTextColor="#FFFFFF"
 * app:rightTextSize="15sp"
 * app:titleText="标题"
 * app:titleTextColor="#FFFFFF"
 * app:titleTextSize="30sp" />
 * <p>
 * Created by 禽兽先生
 * Created on 2017/9/15
 */

public class TitleBar extends RelativeLayout {
    private TextView tvTitle;   //显示标题文字
    private TextView tvLeft;    //显示左边文字
    private ImageView ivLeft;   //显示左边 icon
    private TextView tvRight;   //显示右边文字
    private ImageView ivRight;  //显示右边 icon

    private String mTitleText;  //标题文字
    private int mTitleTextSize; //标题文字大小
    private int mTitleTextColor;    //标题文字颜色
    private String mLeftText;   //左边文字
    private int mLeftTextSize;  //左边文字大小
    private int mLeftTextColor; //左边文字颜色
    private Drawable mLeftIcon; //左边 icon
    private int mLeftMarginLeft;    //左边部分与左边距
    private String mRightText;  //右边文字
    private int mRightTextSize; //右边文字大小
    private int mRightTextColor;    //右边文字颜色
    private Drawable mRightIcon;    //右边 icon
    private int mRightMarginRight;  //右边部分与右边距
    private OnTitleBarClickListener mOnTitleBarClickListener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
        initView(context);
    }

    /**
     * Description:初始化自定义属性
     * Date:2017/9/15
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        //标题文字
        mTitleText = mTypedArray.getString(R.styleable.TitleBar_titleText);
        //标题文字大小
        mTitleTextSize = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_titleTextSize, (int) (24 * context.getResources().getDisplayMetrics().density));
        //标题文字颜色
        mTitleTextColor = mTypedArray.getColor(R.styleable.TitleBar_titleTextColor, Color.BLACK);
        //左边文字
        mLeftText = mTypedArray.getString(R.styleable.TitleBar_leftText);
        //左边文字大小
        mLeftTextSize = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_leftTextSize, (int) (15 * context.getResources().getDisplayMetrics().density));
        //左边文字颜色
        mLeftTextColor = mTypedArray.getColor(R.styleable.TitleBar_leftTextColor, Color.BLACK);
        //左边 icon
        mLeftIcon = mTypedArray.getDrawable(R.styleable.TitleBar_leftIcon);
        //左边部分与左边距
        mLeftMarginLeft = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_leftMarginLeft, 0);
        //右边文字
        mRightText = mTypedArray.getString(R.styleable.TitleBar_rightText);
        //右边文字大小
        mRightTextSize = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_rightTextSize, (int) (15 * context.getResources().getDisplayMetrics().density));
        //右边文字颜色
        mRightTextColor = mTypedArray.getColor(R.styleable.TitleBar_rightTextColor, Color.BLACK);
        //右边 icon
        mRightIcon = mTypedArray.getDrawable(R.styleable.TitleBar_rightIcon);
        //右边部分的右边距
        mRightMarginRight = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_rightMarginRight, 0);
        //重新计算文字的大小,因为获取的 sp 会自动乘以 density
        mTitleTextSize /= context.getResources().getDisplayMetrics().density;
        mLeftTextSize /= context.getResources().getDisplayMetrics().density;
        mRightTextSize /= context.getResources().getDisplayMetrics().density;
        mTypedArray.recycle();
    }

    /**
     * Description:初始化子 View
     * Date:2017/9/15
     */
    private void initView(Context context) {
        //左边子 View 的 LayoutParams
        LayoutParams leftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT);
        leftLayoutParams.setMargins(mLeftMarginLeft, 0, 0, 0);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.CENTER_VERTICAL, TRUE);
        leftLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        //优先显示文字,如果不为空则显示文字,如果没有文字再显示 icon
        if (!TextUtils.isEmpty(mLeftText)) {
            tvLeft = new TextView(context);
            tvLeft.setLayoutParams(leftLayoutParams);
            tvLeft.setText(mLeftText);
            tvLeft.setTextSize(mLeftTextSize);
            tvLeft.setTextColor(mLeftTextColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvLeft.setBackground(null);
            }
            addView(tvLeft);
        } else {
            ivLeft = new ImageView(context);
            ivLeft.setLayoutParams(leftLayoutParams);
            ivLeft.setImageDrawable(mLeftIcon);
            ivLeft.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ivLeft.setBackgroundColor(Color.TRANSPARENT);
            addView(ivLeft);
        }

        //右边子 View 的 LayoutParams
        LayoutParams rightLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT);
        rightLayoutParams.setMargins(0, 0, mRightMarginRight, 0);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        //优先显示文字,如果不为空则显示文字,如果没有文字再显示 icon
        if (!TextUtils.isEmpty(mRightText)) {
            tvRight = new TextView(context);
            tvRight.setLayoutParams(rightLayoutParams);
            tvRight.setText(mRightText);
            tvRight.setTextSize(mRightTextSize);
            tvRight.setTextColor(mRightTextColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvRight.setBackground(mRightIcon);
                tvRight.setBackground(null);
            }
            addView(tvRight);
        } else {
            ivRight = new ImageView(context);
            ivRight.setLayoutParams(rightLayoutParams);
            ivRight.setImageDrawable(mRightIcon);
            ivRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ivRight.setBackgroundColor(Color.TRANSPARENT);
            addView(ivRight);
        }

        LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        tvTitle = new TextView(context);
        tvTitle.setLayoutParams(titleLayoutParams);
        tvTitle.setText(mTitleText);
        tvTitle.setTextSize(mTitleTextSize);
        tvTitle.setTextColor(mTitleTextColor);
        tvTitle.setGravity(Gravity.CENTER);
        addView(tvTitle);
    }

    public interface OnTitleBarClickListener {
        void onLeftClick(View view);

        void onRightClick(View view);
    }

    /**
     * Description:设置左右两边控件的监听器
     * Date:2017/9/25
     */
    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.mOnTitleBarClickListener = onTitleBarClickListener;
        if (tvLeft != null) {
            tvLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TitleBar.this.mOnTitleBarClickListener.onLeftClick(tvLeft);
                }
            });
        }
        if (ivLeft != null) {
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TitleBar.this.mOnTitleBarClickListener.onLeftClick(ivLeft);
                }
            });
        }
        if (tvRight != null) {
            tvRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TitleBar.this.mOnTitleBarClickListener.onRightClick(tvRight);
                }
            });
        }
        if (ivRight != null) {
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TitleBar.this.mOnTitleBarClickListener.onRightClick(ivRight);
                }
            });
        }
    }

    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
    }

    public void setTitleText(int resID) {
        tvTitle.setText(resID);
    }
}
