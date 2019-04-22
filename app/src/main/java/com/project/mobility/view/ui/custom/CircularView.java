package com.project.mobility.view.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.project.mobility.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CircularView extends View {

    private Paint backgroundPaint;
    //circle and text colors
    private int circleCol, labelCol;
    //label text
    private String circleText;
    //paint for drawing custom view
    private Paint circlePaint;

    public CircularView(Context context) {
        super(context);
        init(null, context);
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        setBackgroundColor(Color.RED);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularView,
                0, 0
        );

        try {
            //get the text and colors specified using the names in attrs.xml
            circleText = a.getString(R.styleable.CircularView_circleLabel);
            circleCol = a.getInteger(R.styleable.CircularView_circleColor, 0);//0 is default
            labelCol = a.getInteger(R.styleable.CircularView_labelColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewHalfWidth = getMeasuredWidth() / 2;
        int viewHalfHeight = getMeasuredHeight() / 2;
        drawCircle(canvas, viewHalfWidth, viewHalfHeight);
        drawText(canvas, viewHalfWidth, viewHalfHeight);
    }

    private void drawText(Canvas canvas, int viewHalfWidth, int viewHalfHeight) {
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        canvas.drawText(circleText, viewHalfWidth, viewHalfHeight, circlePaint);
    }

    private void drawCircle(Canvas canvas, int viewHalfWidth, int viewHalfHeight) {
        circlePaint.setColor(circleCol);
        canvas.drawCircle(viewHalfWidth, viewHalfHeight, calculateRadius(viewHalfWidth, viewHalfHeight), circlePaint);
        circlePaint.setColor(labelCol);
    }

    private int calculateRadius(int viewHalfWidth, int viewHalfHeight) {
        int radius;
        if (viewHalfWidth > viewHalfHeight) {
            radius = viewHalfHeight - 10;
        } else {
            radius = viewHalfWidth - 10;
        }
        return radius;
    }

    public int getCircleCol() {
        return circleCol;
    }

    public int getLabelCol() {
        return labelCol;
    }

    public String getCircleText() {
        return circleText;
    }

    public void setCircleCol(int circleCol) {
        this.circleCol = circleCol;
        invalidate();
        requestLayout();
    }

    public void setLabelCol(int labelCol) {
        this.labelCol = labelCol;
        invalidate();
        requestLayout();
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
        invalidate();
        requestLayout();
    }
}
