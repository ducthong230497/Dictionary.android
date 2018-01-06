package com.example.media.dictionary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



public class RectangleView extends View {
    private Paint mPaint;
    public Point point1 = null;
    public Point point2;
    public Point point3;
    public Point point4;

    public RectangleView(Context context) {
        this(context, null);
    }

    public RectangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*point1 = new Point(getWidth() / 2 - 300, getHeight() / 2 - 70);
        point2 = new Point(getWidth() / 2 + 300, getHeight() / 2 - 70);
        point3 = new Point(getWidth() / 2 - 300, getHeight() / 2 + 70);
        point4 = new Point(getWidth() / 2 + 300, getHeight() / 2 + 70);*/


        initPaint();
    }
    Canvas cv;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw component in here

        //drawRect(canvas);
        drawRect_2(canvas);
    }

    private void initPaint(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        //mPaint.setStrokeWidth(1);
        //mPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawRect_2(Canvas canvas){
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        if (point1 == null){
            Log.e("inRecViewConstructor", ""+getWidth()+" "+getHeight());
            point1 = new Point(getWidth() / 2 - 250, getHeight() / 2 - 100);
            point2 = new Point(getWidth() / 2 + 250, getHeight() / 2 + 100);
        }
        //Draw Line
        //canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);

        //canvas.drawLine(point1.x, point1.y, point3.x, point3.y, mPaint);

        //canvas.drawLine(point3.x, point3.y, point4.x, point4.y, mPaint);

        //canvas.drawLine(point4.x, point4.y, point2.x, point2.y, mPaint);

        canvas.drawRect(point1.x, point1.y, point2.x, point2.y, mPaint);

        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);
        //Draw Rect
        float width = 10;
        float height = 10;
        float left = 0;
        float top = 0;
        canvas.drawRect(point1.x - width / 2, point1.y - height / 2, point1.x + width / 2, point1.y + height / 2, mPaint);
        top += getHeight()-10;
        canvas.drawRect(point2.x - width / 2, point1.y - height / 2, point2.x + width / 2, point1.y + height / 2, mPaint);
        left += getWidth()-10;
        canvas.drawRect(point1.x - width / 2, point2.y - height / 2, point1.x + width / 2, point2.y + height / 2, mPaint);
        top -= getHeight()-10;
        canvas.drawRect(point2.x - width / 2, point2.y - height / 2, point2.x + width / 2, point2.y + height / 2, mPaint);
    }
public boolean onTouchEvent(MotionEvent event){
    cv.drawRect(point1.x, point3.y, point3.x, point1.y,
            mPaint);
    return true;
}

}
