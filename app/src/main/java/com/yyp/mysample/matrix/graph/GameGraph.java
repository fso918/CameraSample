package com.yyp.mysample.matrix.graph;

import android.graphics.Color;

import java.util.List;

/**
 * Created by fso91 on 2016/9/22.
 */

public abstract class GameGraph {
    public static final int TYPE_LINE = 0;
    public static final int TYPE_L = 1;
    public static final int TYPE_ANTI_L = 2;
    public static final int TYPE_BIG_BLOCK = 3;
    public static final int TYPE_TU = 4;

    protected int color;
    protected List<MapBlock> blocks;

    public GameGraph(){};

    public void draw(GameMap map){
        synchronized (GameMap.class){
            if(blocks != null && blocks.size() > 0) {
                for (MapBlock b : blocks) {
                    if (b.getY() >= 0) {
                        GameUtils.getBlock(map, b.getX(), b.getY()).setColor(b.getColor()).setBlock(true);
                    }
                }
            }
        }
    }

    public void moveLeft() {
        for(MapBlock b : blocks){
            b.setX(b.getX() - 1);
        }
    }

    public void moveRight() {
        for(MapBlock b : blocks){
            b.setX(b.getX() + 1);
        }
    }

    public boolean isOutOfMap(GameMap map){
        for(MapBlock b : blocks){
            if(b.getY() < 0) return true;
        }
        return false;
    }

    public void fall() {
        synchronized (GameMap.class) {
            for (MapBlock b : blocks) {
                b.setY(b.getY() + 1);
            }
        }
    }

    public abstract void turnShape();

//    public abstract void fall();

    public abstract boolean canFallen(GameMap gmap);

    public abstract boolean canTurnShape(GameMap map);

    public abstract boolean canMoveLeft(GameMap map);

    public abstract boolean canMoveRight(GameMap map);

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<MapBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<MapBlock> blocks) {
        this.blocks = blocks;
    }
}
