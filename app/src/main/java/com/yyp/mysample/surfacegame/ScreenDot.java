package com.yyp.mysample.surfacegame;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by fso91 on 2016/9/22.
 */

public class ScreenDot {
    public static final int STATE_NORMAL = 1;
    public static final int STATE_DISPEARING = 2;
    public static final int STATE_GONE = 0;

    private int color;
    private int corX;
    private int corY;
    private int radius;
    private int state;

    public ScreenDot(int width, int height){
        Random random = new Random();
        radius = random.nextInt(GameConstants.dotMaxRadius - GameConstants.dotMinRadius) + GameConstants.dotMinRadius;
        corX = random.nextInt(width - 2 * radius) + radius;
        corY = random.nextInt(height - 2 * radius) + radius;
        color = random.nextInt(GameConstants.colors.length);
        state = STATE_NORMAL;
    }

    public void draw(Canvas canvas){
        if(state == STATE_DISPEARING){
            radius -= GameConstants.dispearStep;
            if(radius <= 0){
                state = STATE_GONE;
                return;
            }
        }
        Paint paint = new Paint();
        paint.setColor(GameConstants.colors[color % GameConstants.colors.length]);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.translate(corX, corY);
        canvas.drawCircle(0, 0, radius, paint);
    }

    public boolean contaim(int x, int y){
        int dis = (int) Math.sqrt((x - corX) * (x - corX) + (y - corY) * (y - corY));
        if(dis <= radius){
            return true;
        }
        return false;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCorX() {
        return corX;
    }

    public void setCorX(int corX) {
        this.corX = corX;
    }

    public int getCorY() {
        return corY;
    }

    public void setCorY(int corY) {
        this.corY = corY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
