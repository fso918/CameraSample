package com.yyp.mysample.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yyp.mysample.utils.DisplayUtils;

/**
 * Created by fso91 on 2017/9/27.
 */

public class XfermodeSampleView extends View {
    private Context context;
    private PorterDuff.Mode mode;
    private Paint circlePaint;
    private Paint rectPaint;
    private Paint bitmapPaint;
    private int width;
    private int paddingLeft;
    private int diameter;

    public XfermodeSampleView(Context context) {
        super(context);
        this.context = context;
        width = DisplayUtils.dip2px(context, 80f);
        initPaint();
    }

    public XfermodeSampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        width = DisplayUtils.dip2px(context, 80f);
        initPaint();
    }

    public XfermodeSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        width = DisplayUtils.dip2px(context, 80f);
        initPaint();
    }

    private void initPaint(){
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor("#ff9912"));

        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(Color.parseColor("#87ceeb"));

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

        diameter = width / 2;
        paddingLeft = 30;
    }

    public void setMode(PorterDuff.Mode mode){
        this.mode = mode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, widthSpec),
                MeasureSpec.makeMeasureSpec(width, heightSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawColor(Color.parseColor("#ffffcd"));
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bitmap);
        //画圆
        RectF oval = new RectF( paddingLeft, paddingLeft, diameter + paddingLeft, paddingLeft + diameter);
        bitmapCanvas.drawArc(oval, 0, 360, true, circlePaint);
        //画正方形
        Bitmap bitmap1 = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas1 = new Canvas(bitmap1);
        Rect rect = new Rect(paddingLeft + diameter / 2, paddingLeft + diameter / 2, width - paddingLeft, width - paddingLeft);
        bitmapCanvas1.drawRect(rect, rectPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmapPaint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(bitmap1, 0, 0, bitmapPaint);
    }
}
