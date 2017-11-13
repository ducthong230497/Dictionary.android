package com.example.media.dictionary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Media on 11/1/2017.
 */

public class RectangleView extends View {
    private Paint mPaint;

    public RectangleView(Context context) {
        this(context, null);
    }

    public RectangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw component in here
        initPaint();

        //drawRect(canvas);
        drawRect_2(canvas);
    }

    private void initPaint(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void drawRect(Canvas canvas){
        //Draw Line
        float startX = getWidth() / 2.0f - 150;
        float startY = getHeight() / 2.0f - 50;
        float stopX = getWidth() / 2.0f + 150;
        float stopY = getHeight()/ 2.0f - 50;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        float startX1 = getWidth() / 2.0f - 150;
        float startY1 = getHeight() / 2.0f + 50;
        float stopX1 = getWidth() / 2.0f + 150;
        float stopY1 = getHeight()/ 2.0f + 50;
        canvas.drawLine(startX1, startY1, stopX1, stopY1, mPaint);

        float startX2 = getWidth() / 2.0f - 150;
        float startY2 = getHeight() / 2.0f - 50;
        float stopX2 = getWidth() / 2.0f - 150;
        float stopY2 = getHeight()/ 2.0f + 50;
        canvas.drawLine(startX2, startY2, stopX2, stopY2, mPaint);

        float startX3 = getWidth() / 2.0f + 150;
        float startY3 = getHeight() / 2.0f - 50;
        float stopX3 = getWidth() / 2.0f + 150;
        float stopY3 = getHeight()/ 2.0f + 50;
        canvas.drawLine(startX3, startY3, stopX3, stopY3, mPaint);

        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        //Draw Rect
        float width = 10;
        float height = 10;
        float left = (getWidth() - width) / 2.0f - 150;
        float top = (getHeight() - height) / 2.0f - 50;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        top += 100;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        left += 300;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        top -= 100;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
    }

    private void drawRect_2(Canvas canvas){
        //Draw Line
        float startX = 0;
        float startY = 3;
        float stopX = getWidth()-1;
        float stopY = 0;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        float startX1 = 0;
        float startY1 = getHeight()-2;
        float stopX1 = getWidth()-1;
        float stopY1 = getHeight()-1;
        canvas.drawLine(startX1, startY1, stopX1, stopY1, mPaint);

        float startX2 = 0;
        float startY2 = 0;
        float stopX2 = 0;
        float stopY2 = getHeight()-1;
        canvas.drawLine(startX2, startY2, stopX2, stopY2, mPaint);

        float startX3 = getWidth()-1;
        float startY3 = 0;
        float stopX3 = getWidth()-1;
        float stopY3 = getHeight()-1;
        canvas.drawLine(startX3, startY3, stopX3, stopY3, mPaint);

        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        //Draw Rect
        float width = 10;
        float height = 10;
        float left = 0;
        float top = 0;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        top += getHeight()-10;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        left += getWidth()-10;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
        top -= getHeight()-10;
        canvas.drawRect(left, top, left + width, top + height, mPaint);
    }
}
