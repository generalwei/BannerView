package com.general.imagebannerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouwei on 2017/3/15.
 */

public class TextBannerView extends ViewGroup {
    private int children;
    private int childWidth;
    private int childHeight;
    private int height;
    private OnClickIndexOfBanner clickIndexOfBanner;
    private int index = 0;
    private final int sendMessage = 10000;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }
            super.handleMessage(msg);
            if (sendMessage == msg.what) {
                if (++index > children - 1) {
                    index = 0;
                }
                scrollTo(0, index * childHeight);
            }
        }
    };


    public void setClickIndexOfBanner(OnClickIndexOfBanner clickIndexOfBanner) {
        this.clickIndexOfBanner = clickIndexOfBanner;
    }

    public OnClickIndexOfBanner getClickIndexOfBanner() {
        return clickIndexOfBanner;
    }

    public TextBannerView(Context context) {
        super(context);
        initObj();
    }

    public TextBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    public TextBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    private void initObj() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(sendMessage);
            }
        };

        timer = new Timer();

        timer.schedule(timerTask, 100, 3 * 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        children = getChildCount();
        if (children == 0) {
            setMeasuredDimension(0, 0);
        } else {
            //测量子视图的高贺宽
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            Log.d("text childrenCount", children + "," + widthMeasureSpec + "," + heightMeasureSpec + "," + getWidth() + "," + getHeight());

            View view = getChildAt(0);
            childHeight = view.getMeasuredHeight();
            childWidth = view.getMeasuredWidth();
            //根据子视图的高和宽求出ViewGroup的高贺宽
            height = childHeight * children;
            Log.d("text size", childWidth + "," + height);
            setMeasuredDimension(childWidth, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("text onLayout", changed + "");
        if (changed) {
            int top = 0;
            for (int i = 0; i < children; i++) {
                View view = getChildAt(i);
                Log.d("text onLayout", 0 + "," + top + "," + childHeight + "," + (top + childHeight));
                view.layout(0, top, childWidth, top + childHeight);
                top += childHeight;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                clickIndexOfBanner.onClickBanner(index, getChildAt(index));
                break;
        }
        return true;
    }

    interface OnClickIndexOfBanner {
        void onClickBanner(int index, View view);
    }

    public void addText(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        addView(textView);
    }
}
