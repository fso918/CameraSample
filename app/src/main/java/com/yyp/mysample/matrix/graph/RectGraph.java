package com.yyp.mysample.matrix.graph;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fso91 on 2016/9/27.
 */

public class RectGraph extends GameGraph {

    private RectGraph(){
        color = Color.parseColor("#4B0082");
        blocks = new ArrayList<MapBlock>();
        MapBlock block = null;
        block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 - 1, -2, color);
        blocks.add(block);
        block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -2, color);
        blocks.add(block);
        block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 - 1, -1, color);
        blocks.add(block);
        block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -1, color);
        blocks.add(block);
    }

    @Override
    public void turnShape() {
        //do nothing
    }

    @Override
    public boolean canFallen(GameMap gmap) {
        MapBlock block1 = blocks.get(2);
        MapBlock block2 = blocks.get(3);
        if(block1.getY() == MatrixGameConstant.GAME_HEIGHT -1) return false;
//        if(GameUtils.getBlock(gmap, block1.getX(), block1.getY() + 1).isBlock() || GameUtils.getBlock(gmap, block2.getX(), block2.getY() + 1).isBlock()){
//            return false;
//
        if(GameUtils.isDownBlocked(gmap, block1, block2)){
            return false;
        }
        return true;
    }

    @Override
    public boolean canTurnShape(GameMap map) {
        return true;
    }

    @Override
    public boolean canMoveLeft(GameMap map) {
        MapBlock block1 = blocks.get(0);
        MapBlock block2 = blocks.get(2);
        if(block1.getX() == 0) return false;
        if(GameUtils.getBlock(map, block1.getX() - 1, block1.getY()).isBlock() || GameUtils.getBlock(map, block2.getX() - 1, block2.getY()).isBlock()){
            return false;
        }
        return true;
    }

    @Override
    public boolean canMoveRight(GameMap map) {
        MapBlock block1 = blocks.get(1);
        MapBlock block2 = blocks.get(3);
        if(block1.getX() == MatrixGameConstant.GAME_WIDTH - 1) return false;
        if(GameUtils.getBlock(map, block1.getX() + 1, block1.getY()).isBlock() || GameUtils.getBlock(map, block2.getX() + 1, block2.getY()).isBlock()){
            return false;
        }
        return true;
    }

    public static GameGraph getRandomIns(){
        return new RectGraph();
    }
}
