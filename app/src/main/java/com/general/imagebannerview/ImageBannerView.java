package com.general.imagebannerview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouwei on 2017/3/14.
 */

public class ImageBannerView extends ViewGroup {
    private int children;
    private int childHeight;//子视图高
    private int childWidth;//子视图宽
    private int width;
    private int lastX;
    private int index = 0;
    private Scroller scroller;

    private final int autoType = 10000;
    private Timer timer;
    private TimerTask timerTask;

    private boolean isAuto;

    private OnClickIndexOfBanner clickIndexOfBanner;
    private boolean isClick = false;
    private OnChangeBanner changeBanner;

    public void setChangeBanner(OnChangeBanner changeBanner) {
        this.changeBanner = changeBanner;
    }

    public OnChangeBanner getChangeBanner() {
        return changeBanner;
    }

    public void setClickIndeOfBanner(OnClickIndexOfBanner clickIndeOfBanner) {
        this.clickIndexOfBanner = clickIndeOfBanner;
    }

    public OnClickIndexOfBanner getClickIndeOfBanner() {
        return clickIndexOfBanner;
    }

    private Handler autoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (autoType == msg.what) {
                if (++index >= children - 1) {
                    index = 0;
                }
                scrollTo(index * childWidth, 0);
                changeBanner.changeBanner(index);
            }
        }
    };

    private void authStart() {
        isAuto = true;
    }

    private void authStop() {
        isAuto = false;
    }


    public ImageBannerView(Context context) {
        super(context);
        initObj();
    }

    public ImageBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    public ImageBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    private void initObj() {
        scroller = new Scroller(getContext());

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {
                    autoHandler.sendEmptyMessage(autoType);
                }
            }
        };

        timer = new Timer();

        timer.schedule(timerTask, 100, 3 * 1000);

        isAuto = true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 自定义ViewGroup中，我们必须实现的方法有:测量-》布局-》绘制  onMeasure-》onLayout-》onDraw
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //求出子视图的高度
        children = getChildCount();
        Log.d("childrenCount", children + "");
        if (children == 0) {
            setMeasuredDimension(0, 0);
        } else {
            //测量子视图的高贺宽
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            View view = getChildAt(0);
            childHeight = view.getMeasuredHeight();
            childWidth = view.getMeasuredWidth();
            width = childWidth * children;
            //根据子视图的高和宽求出ViewGroup的高贺宽
            Log.d("measure", width + "," + childHeight);
            setMeasuredDimension(width, childHeight);
        }
    }


    /**
     * @param changed 当ViewGroup布局发生改变为true，否者为false
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("onLayout", changed + "");
        if (changed) {
            int left = 0;
            for (int i = 0; i < children; i++) {
                View view = getChildAt(i);
                Log.d("onLayout", left + "," + 0 + "," + (left + childWidth) + "," + childHeight);
                view.layout(left, 0, left + childWidth, childHeight);
                left += childWidth;
            }

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastX = (int) event.getX();
                authStop();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distanceX = moveX - lastX;
                scrollBy(-distanceX, 0);
                lastX = moveX;
                if (Math.abs(distanceX) > 10) {
                    isClick = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                index = (scrollX + childWidth / 2) / childWidth;
                if (index < 0) {
                    index = 0;
                } else if (index > children - 1) {
                    index = children - 1;
                }
                if (isClick) {
                    clickIndexOfBanner.clickIndexOfBanner(index, getChildAt(index));
                } else {
                    int dx = index * childWidth - scrollX;
                    scroller.startScroll(scrollX, 0, dx, 0, 0);
                    postInvalidate();
                    if (index == children - 1) {
                        index = 0;
                        lastX = 0;
                        scrollTo(0, 0);
                        scroller.setFinalX(0);
                    }
                    changeBanner.changeBanner(index);
                }
                authStart();
                break;
        }
        return true;
    }

    interface OnClickIndexOfBanner {
        void clickIndexOfBanner(int index, View view);
    }

    interface OnChangeBanner {
        void changeBanner(int index);
    }

    public void addImageToBannerView(int bvId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setImageResource(bvId);
        addView(imageView);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
