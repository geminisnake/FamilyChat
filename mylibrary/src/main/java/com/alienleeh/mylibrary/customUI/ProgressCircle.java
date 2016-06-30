package com.alienleeh.mylibrary.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.alienleeh.mylibrary.R;

/**
 * Created by AlienLeeH <br/>
 * on 2016/7/23..Hour:08<br/>
 * Email:alienleeh@foxmail.com<br/>
 * Description:自定义进度圆环
 */
public class ProgressCircle extends View{

    private Paint innerPaint;
    private Paint ringPaint;
    private Paint textPaint;
    private TypedArray typedArray;
    private float radius;
    private int innerColor;
    private int ringColor;
    private int textColor;
    private float storke;
    private RectF rectArc;
    private float circleX;
    private float circleY;

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    private int progress;
    private Rect tBounds;

    public ProgressCircle(Context context) {
        this(context,null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressCircle);

        init();
    }

    private void init() {
        radius = typedArray.getFloat(R.styleable.ProgressCircle_radius,60);
        radius = dp2px(radius);
        innerColor = typedArray.getColor(R.styleable.ProgressCircle_inner_color,0);
        ringColor = typedArray.getColor(R.styleable.ProgressCircle_ring_color,0);
        textColor = typedArray.getColor(R.styleable.ProgressCircle_text_color,0);


        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);


        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setColor(ringColor);
        ringPaint.setStyle(Paint.Style.STROKE);
        storke = radius * 0.1f;
        ringPaint.setStrokeWidth(storke);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(radius/2);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        tBounds = new Rect();
        textPaint.getTextBounds("00%",0,"00%".length(), tBounds);
    }

    private float dp2px(float dp) {
        float d = getContext().getResources().getDisplayMetrics().density;
        return d * dp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleX = (float)getWidth() / 2;
        circleY = (float)getHeight() / 2;
        rectArc = new RectF(circleX - (radius - 2*storke),
                circleY - (radius - 2*storke),
                circleX + (radius - 2*storke),
                circleY + (radius - 2*storke));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sweepAngle = progress * 360f / 100f;
        String text = progress + "%";
        canvas.drawCircle(circleX,circleY,radius,innerPaint);
        canvas.drawArc(rectArc,270,sweepAngle,false,ringPaint);
        canvas.drawText(text,
                circleX,
                circleY + tBounds.height() / 2,
                textPaint);
    }
}
