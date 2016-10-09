package com.yyp.mysample.matrix.graph;

/**
 * Created by fso91 on 2016/9/22.
 */

public class MapBlock {
    private int x;          //x坐标
    private int y;          //y坐标
    private int color = MatrixGameConstant.GAME_BLOCK_COLOR;

    public MapBlock(int x, int y){
        this.x = x;
        this.y = y;
    }

    public MapBlock(int x, int y, int color){
        this(x, y);
        this.color = color;
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

    public void setColor(int color) {
        this.color = color;
    }
}
