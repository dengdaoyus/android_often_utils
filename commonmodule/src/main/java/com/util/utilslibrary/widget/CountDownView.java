package com.util.utilslibrary.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.util.utilslibrary.R;


/**
 * Description:倒计时控件
 * 使用示例:
 * <p>
 * <com.qinshou.toollib.widget.CountDownView
 * android:id="@+id/count_down_view"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:layout_gravity="center_horizontal"
 * app:cdvText="跳过"
 * app:cdvTextColor="#FFFFFF"
 * app:cdvTextSize="15sp"
 * app:circleColor="#6495ED"
 * app:duration="10000"
 * app:ringColor="#48D1CC" />
 * <p>
 * Created by 禽兽先生
 * Created on 2017/9/18
 */

public class CountDownView extends View {
    private long duration;   //倒计时时长
    private int circleColor;    //圆圈颜色
    private int ringColor;  //圆环颜色
    private String cdvText; //文本
    private int cdvTextColor;   //文本字体颜色
    private int cdvTextSize;    //文本字体大小
    private Rect mBound = new Rect();    //绘制时控制文本绘制的范围
    private Paint mTextPaint;   //绘制文字的画笔
    private Paint mPaint;   //绘制和圆环的画笔
    private float radius;   //圆半径
    private int width;
    private int height;
    private float sweepAngle = 0;
    private onCountDownListener mOnCountDownListener;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
        initPaint();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startCountDown();
            }
        });
        startCountDown();
    }

    private void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public void setOnCountDownListener(CountDownView.onCountDownListener onCountDownListener) {
        this.mOnCountDownListener = onCountDownListener;
    }

    /**
     * Description:初始化自定义属性
     * Date:2017/9/18
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        duration = (long) mTypedArray.getFloat(R.styleable.CountDownView_duration, 3000);
        circleColor = mTypedArray.getColor(R.styleable.CountDownView_circleColor, Color.RED);
        ringColor = mTypedArray.getColor(R.styleable.CountDownView_ringColor, Color.BLUE);
        cdvText = mTypedArray.getString(R.styleable.CountDownView_cdvText);
        cdvTextColor = mTypedArray.getColor(R.styleable.CountDownView_cdvTextColor, Color.BLACK);
        cdvTextSize = mTypedArray.getDimensionPixelSize(R.styleable.CountDownView_cdvTextSize, (int) (40 * context.getResources().getDisplayMetrics().density));
        if (TextUtils.isEmpty(cdvText)) {
            cdvText = "跳过";
        }
        //重新计算文字的大小,因为获取的 sp 会自动乘以 density
//        cdvTextSize /= context.getResources().getDisplayMetrics().density;
        mTypedArray.recycle();
    }

    /**
     * Description:初始化画笔
     * Date:2017/9/18
     */
    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(cdvTextColor);
        mTextPaint.setStrokeWidth(cdvTextSize);
        mTextPaint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mTextPaint.setTextSize(cdvTextSize);
            mTextPaint.getTextBounds(cdvText, 0, cdvText.length(), mBound);
            float textWidth = mBound.width() * 2;
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mTextPaint.setTextSize(cdvTextSize);
            mTextPaint.getTextBounds(cdvText, 0, cdvText.length(), mBound);
            float textHeight = mBound.height() * 2;
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }
        width = height = Math.max(width, height);
        radius = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleColor);
        canvas.drawCircle(width / 2, height / 2, radius, mPaint);

        mPaint.setStrokeWidth(radius / 10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ringColor);
        canvas.drawArc(getRectF(), -90, sweepAngle, false, mPaint);
        canvas.drawText(cdvText, width / 2 - mBound.width() / 2, height / 2 + mBound.height() / 2, mTextPaint);
    }

    private RectF getRectF() {
        return new RectF(0 + radius / 20, 0 + radius / 20, width - radius / 20, height - radius / 20);
    }

    private int tick;
    private int temp;

    private void startCountDown() {
        tick = (int) (duration / 1000);
        final ObjectAnimator sweepAngleAnimator = ObjectAnimator.ofFloat(this, "sweepAngle", 0, 360);
        sweepAngleAnimator.setDuration(duration);
        sweepAngleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mOnCountDownListener != null) {
                    temp = (int) ((duration - sweepAngleAnimator.getCurrentPlayTime()) / 1000);
                    if (temp >= 0 && temp != tick) {
                        tick = temp;
                        mOnCountDownListener.onTick(tick);
                    }
                }
                invalidate();
            }
        });
        sweepAngleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mOnCountDownListener != null) {
                    mOnCountDownListener.onFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        sweepAngleAnimator.start();
    }

    public interface onCountDownListener {
        void onTick(int tick);

        void onFinish();
    }
}
