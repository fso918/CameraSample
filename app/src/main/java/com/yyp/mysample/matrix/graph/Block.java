package com.yyp.mysample.matrix.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by fso91 on 2016/9/22.
 */

public class Block {
    private int len = 29;
    private int x;
    private int y;
    private int color = MatrixGameConstant.GAME_DEFAULT_COLOR;
    private boolean isBlock;

    public Block(){}

    public Block(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Block(int x, int y, int color){
        this(x, y);
        this.color = color;
    }

    public void draw(Canvas canvas){
        Rect rect = new Rect(x + 1, y + 1, x + 1 + len, y + 1 + len);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect(rect, paint);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(1);
//        rect = new Rect(x + 1, y + 1, x + 7 + len, y + 7 + len);
//        canvas.drawRect(rect, paint);
    }

    public int getWidth(){
        return len + 1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public Block setColor(int color) {
        this.color = color;
        return this;
    }

    public Block resetColor(){
        this.color = MatrixGameConstant.GAME_DEFAULT_COLOR;
        return this;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public Block setBlock(boolean block) {
        isBlock = block;
        return this;
    }
}
