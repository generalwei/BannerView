package com.general.imagebannerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhouwei on 2017/3/15.
 */

public class SearlCurvePath extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private Canvas canvas;
    private float radius = 50;
    private boolean isMove = false;
    private float x, y;


    public SearlCurvePath(Context context) {
        super(context);
    }

    public SearlCurvePath(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearlCurvePath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        x = getWidth() / 2;
        y = getHeight() / 2;
//        canvas.drawColor(Color.RED);
//        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

//        paint.setStyle(Paint.Style.STROKE);//设置为空心

        paint.setStrokeWidth(3);
        canvas.drawCircle(x, y,radius,paint);

//        path.addCircle(x, y, radius, Path.Direction.CW);

//        path.moveTo(getWidth(), getHeight());
//        path.quadTo(getWidth() / 2, 0, 0, getHeight());
        path.close();
        this.canvas.drawPath(path, paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= x - radius && event.getX() <= x + radius && event.getY() >= y - radius && event.getY() <= y + radius) {
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {
                    drawCurve(event.getX(), event.getY());
                }
                if (event.getY() > getHeight() && event.getX() < getWidth()) {
                    isMove = false;
                    path.reset();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                path.reset();
                invalidate();
                Log.d("tag","ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                isMove = false;
                path.reset();
                invalidate();
                break;
        }
        return true;
    }

    private void drawCurve(float moveX, float moveY) {
        path.reset();
        Point point[] = getPoint(moveX, moveY);
        Point starP = point[0];
        Point endP = point[1];
//        path.addCircle(x, y, radius, Path.Direction.CCW);
        Log.d("point", starP.x + "," + starP.y + "," + endP.x + "," + endP.y);
        path.moveTo(starP.x, starP.y);
        path.quadTo(moveX, moveY, endP.x, endP.y);
        path.close();
//        canvas.drawPath(path, paint);
        invalidate();
    }

    private Point[] getPoint(float moveX, float moveY) {
        Point points[] = new Point[]{new Point(), new Point()};
        if (moveX < x && moveY < y) {
            points[0].x = (int) (x - radius);
            points[0].y = (int) (y);

            points[1].x = (int) (x + radius);
            points[1].y = (int) (y);
        } else if (moveX > x && moveY < y) {
            points[0].x = (int) (x);
            points[0].y = (int) (y - radius);

            points[1].x = (int) (x);
            points[1].y = (int) (y + radius);
        } else if (moveX > x && moveY < y) {
            points[0].x = (int) (x + radius);
            points[0].y = (int) (y);

            points[1].x = (int) (x - radius);
            points[1].y = (int) (y);
        }else {
            points[0].x = (int) (x);
            points[0].y = (int) (y + radius);

            points[1].x = (int) (x);
            points[1].y = (int) (y - radius);
        }


        return points;
    }
}
