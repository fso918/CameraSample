package com.yyp.mysample.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;

/**
 * Created by fso91 on 2017/9/28.
 */

public class LikeTaoBaoProcess extends View {
    private int width = -1;
    private int height = -1;
    private String text = "这是一个会变色的文本";

    private Context context;

    private Paint backPaintDark;
    private Paint backPaintLight;
    private Paint maskPaint;
    private Paint boundPaint;
    private Paint progessPaint;
    private Paint textPaint;
    private Paint dstInPaint;
    private Paint srcAtopPaint;
    private Paint textMaskPaint;

    private Bitmap backgroundBitmap;
    private Bitmap textBitmap;
    private Bitmap progressBitmap;
    private Bitmap textMaskBitmap;
    private Bitmap backgroundMaskBitmap;
    private int progress = 0;
    private int oldProgress = 0;
    private int animWidthTmp = 0;

    private boolean playing = false;

    public LikeTaoBaoProcess(Context context) {
        super(context);
        init(context);
    }

    public LikeTaoBaoProcess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LikeTaoBaoProcess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        backPaintDark = new Paint();
        backPaintDark.setStyle(Paint.Style.FILL);
        backPaintDark.setColor(Color.parseColor("#ffc0cb"));
        backPaintDark.setStrokeWidth(20);

        backPaintLight = new Paint();
        backPaintLight.setStyle(Paint.Style.FILL);
        backPaintLight.setColor(Color.parseColor("#fffacd"));
        backPaintLight.setStrokeWidth(20);

        boundPaint = new Paint();
        boundPaint.setStrokeWidth(5);
        boundPaint.setStyle(Paint.Style.STROKE);
        boundPaint.setColor(Color.parseColor("#ff7256"));

        maskPaint = new Paint();
        maskPaint.setStrokeWidth(5);
        maskPaint.setStyle(Paint.Style.FILL);
        maskPaint.setColor(Color.parseColor("#ff7256"));
//        boundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        progessPaint = new Paint();
        progessPaint.setStyle(Paint.Style.FILL);
        progessPaint.setColor(Color.parseColor("#80ff0000"));

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#ffa500"));
        textPaint.setTextSize(60);
        textMaskPaint = new Paint();
        textMaskPaint.setColor(Color.WHITE);

        dstInPaint = new Paint();
        dstInPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        srcAtopPaint = new Paint();
        srcAtopPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width < 0) {
            width = getMeasuredWidth();
        }
        if(height < 0){
            height = getMeasuredHeight();
        }

        canvas.drawColor(Color.WHITE);

        //draw background
        initBackgroundBitmap();
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);

//        drawProgress(canvas);

        drawText();
        canvas.drawBitmap(textBitmap, 0, 0, null);
    }

    private void drawText(){
        if(textBitmap == null) {
            textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas textCanvas = new Canvas(textBitmap);
        float textWidth = textPaint.measureText(text);

        textCanvas.drawText(text, (width - textWidth) / 2, height - (height / 6), textPaint);
        if(textMaskBitmap == null) {
            textMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas textMaskCanvas = new Canvas(textMaskBitmap);
        textMaskCanvas.drawRect(new Rect(0,0, animWidthTmp, height), textMaskPaint);
        textCanvas.drawBitmap(textMaskBitmap, 0, 0, srcAtopPaint);
    }

    private void drawProgress(){
        if(progressBitmap == null) {
            progressBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas progressCanvas = new Canvas(progressBitmap);
        progressCanvas.drawRect(new Rect(0,0, animWidthTmp, height), progessPaint);
//        canvas.drawRoundRect(new RectF(2.5f, 2.5f, 50, height - 2.5f), height / 2, height / 2, progessPaint);
    }

    private void initBackgroundBitmap(){
        Canvas canvas = null;
        if(backgroundBitmap == null) {
            backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int xLineStart = 0;
            int count = 1;
            canvas = new Canvas(backgroundBitmap);
            while (xLineStart < width + height) {
                if (count % 2 == 1) {
                    canvas.drawLine(xLineStart + 0f, -10, xLineStart - height, height + 10, backPaintDark);
                } else {
                    canvas.drawLine(xLineStart + 0f, -10, xLineStart - height, height + 10, backPaintLight);
                }
                count++;
                xLineStart += 20;
            }
            if (backgroundMaskBitmap == null) {
                backgroundMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas1 = new Canvas(backgroundMaskBitmap);
            canvas1.drawRoundRect(new RectF(2.5f, 2.5f, width - 2.5f, height - 2.5f), height / 2, height / 2, maskPaint);
            canvas.drawBitmap(backgroundMaskBitmap, 0, 0, dstInPaint);
            canvas.drawRoundRect(new RectF(2.5f, 2.5f, width - 2.5f, height - 2.5f), height / 2, height / 2, boundPaint);
        }
        drawProgress();
        if(canvas == null){
            canvas = new Canvas(backgroundBitmap);
        }
        canvas.drawBitmap(progressBitmap, 0, 0, srcAtopPaint);
    }

    public void setProgress(int progress){
        if(progress < this.progress){
            return;
        }
        this.progress = progress > 100 ? 100 : progress;
        if(!playing) {
            startAnimation();
        }
    }

    private void startAnimation(){
        ValueAnimator animator = ValueAnimator.ofInt(width * oldProgress / 100, width * progress / 100);
        animator.setDuration(10000 * (progress - oldProgress) / 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animWidthTmp = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(oldProgress < progress){
                    startAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

//    private void startAnimation(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                playing = true;
//                while (oldProgress < progress) {
//                    oldProgress += 1;
//                    postInvalidate();
//                    try {
//                        Thread.currentThread().sleep(50);
//                    }catch (Exception e){
//                        break;
//                    }
//                }
//                playing = false;
//            }
//        }).start();
//    }
}
