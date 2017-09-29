package com.yyp.mysample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by fso91 on 2016/12/16.
 */

public class HeartView extends View {
    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path heartPath = new Path();
        canvas.drawColor(Color.WHITE);
        int width = getWidth();
        int height = getHeight();
        int padding = 100;
        int r = (Math.min(width, height) - 2 * padding) / 2;
        int lenth = Double.valueOf(4 * r * Math.tan(Math.PI / 8) / 3).intValue();
        Log.i("TEST", "width=" + width + ",height=" + height + ", r=" + r + ",lenth=" + lenth);
        heartPath.moveTo(padding, height / 2);
        heartPath.cubicTo(padding, height / 2 - lenth, r + padding - lenth, height / 2 - r, r + padding, height / 2 - r + 100);
        heartPath.cubicTo(r + padding + lenth, height / 2 - r, r + padding + r, height / 2 - lenth, r + padding + r, height / 2);
        heartPath.cubicTo(r + padding + r, height / 2 + lenth, r + padding + lenth, height / 2 + r, r + padding, height / 2 + r + 80);
        heartPath.cubicTo(r + padding - lenth, height / 2 + r, padding, height / 2 + lenth, padding, height / 2);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        canvas.drawPath(heartPath, paint);
    }
}
