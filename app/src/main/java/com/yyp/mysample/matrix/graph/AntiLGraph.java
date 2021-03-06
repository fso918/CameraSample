package com.yyp.mysample.matrix.graph;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fso91 on 2016/9/27.
 */

public class AntiLGraph extends GameGraph {
    public static final int STATE_TURN_1 = 1;
    public static final int STATE_TURN_2 = 2;
    public static final int STATE_TURN_3 = 3;
    public static final int STATE_TURN_4 = 4;

    private AntiLGraph(){
        color = Color.GREEN;
        blocks = new ArrayList<MapBlock>();
    }

    private AntiLGraph(int state){
        this();
        this.state = state;
        MapBlock block = null;
        switch (state){
            case STATE_TURN_1:
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -3, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -2, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -1, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -1, color);
                blocks.add(block);
                break;
            case STATE_TURN_2:
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -1, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -1, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 - 1, -1, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 - 1, -2, color);
                blocks.add(block);
                break;
            case STATE_TURN_3:
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -1, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -2, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -3, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -3, color);
                blocks.add(block);
                break;
            case STATE_TURN_4:
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 - 1, -2, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2, -2, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -2, color);
                blocks.add(block);
                block = new MapBlock(MatrixGameConstant.GAME_WIDTH / 2 + 1, -1, color);
                blocks.add(block);
                break;
        }
    }

    @Override
    public void turnShape() {
        MapBlock block = null;
        int x = blocks.get(0).getX();
        int y = blocks.get(0).getY();
        blocks.clear();
        switch (state){
            case STATE_TURN_1:
                state = STATE_TURN_2;
                block = new MapBlock(x + 1, y + 2, color);
                blocks.add(block);
                block = new MapBlock(x, y + 2, color);
                blocks.add(block);
                block = new MapBlock(x - 1, y + 2, color);
                blocks.add(block);
                block = new MapBlock(x - 1, y + 1, color);
                blocks.add(block);
                break;
            case STATE_TURN_2:
                state = STATE_TURN_3;
                block = new MapBlock(x - 2, y + 1, color);
                blocks.add(block);
                block = new MapBlock(x - 2, y, color);
                blocks.add(block);
                block = new MapBlock(x - 2, y - 1, color);
                blocks.add(block);
                block = new MapBlock(x - 1, y - 1, color);
                blocks.add(block);
                break;
            case STATE_TURN_3:
                state = STATE_TURN_4;
                block = new MapBlock(x - 1, y - 2, color);
                blocks.add(block);
                block = new MapBlock(x, y - 2, color);
                blocks.add(block);
                block = new MapBlock(x + 1, y - 2, color);
                blocks.add(block);
                block = new MapBlock(x + 1, y - 1, color);
                blocks.add(block);
                break;
            case STATE_TURN_4:
                state = STATE_TURN_1;
                block = new MapBlock(x + 2, y - 1, color);
                blocks.add(block);
                block = new MapBlock(x + 2, y, color);
                blocks.add(block);
                block = new MapBlock(x + 2, y + 1, color);
                blocks.add(block);
                block = new MapBlock(x + 1, y + 1, color);
                blocks.add(block);
                break;
        }
    }

    @Override
    public boolean canFallen(GameMap gmap) {
        MapBlock block1 = null;
        MapBlock block2 = null;
        MapBlock block3 = null;
        switch (state){
            case STATE_TURN_1:
                block1 = blocks.get(2);
                block2 = blocks.get(3);
                if(block1.getY() == MatrixGameConstant.GAME_HEIGHT -1) return false;
                break;
            case STATE_TURN_2:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(2);
                if(block3.getY() == MatrixGameConstant.GAME_HEIGHT -1) return false;
                break;
            case STATE_TURN_3:
                block1 = blocks.get(0);
                block2 = blocks.get(3);
                if(block1.getY() == MatrixGameConstant.GAME_HEIGHT -1) return false;
                break;
            case STATE_TURN_4:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(3);
                if(block3.getY() == MatrixGameConstant.GAME_HEIGHT -1) return false;
                break;
        }

        if(GameUtils.isDownBlocked(gmap, block1, block2, block3)){
            return false;
        }
        return true;
    }

    @Override
    public boolean canTurnShape(GameMap map) {
        int x = blocks.get(0).getX();
        int y = blocks.get(0).getY();
        boolean canTurnShape = true;
        switch (state){
            case STATE_TURN_1:
                if(GameUtils.isBlocked(map, x - 1, y + 1) || GameUtils.isBlocked(map, x + 1, y + 2)){
                    canTurnShape = false;
                }
                break;
            case STATE_TURN_2:
                if(GameUtils.isBlocked(map, x - 1, y - 1) || GameUtils.isBlocked(map, x - 2, y + 1)){
                    canTurnShape = false;
                }
                break;
            case STATE_TURN_3:
                if(GameUtils.isBlocked(map, x + 1, y - 1) || GameUtils.isBlocked(map, x - 1, y - 2   )){
                    canTurnShape = false;
                }
                break;
            case STATE_TURN_4:
                if(GameUtils.isBlocked(map, x + 1, y + 1) || GameUtils.isBlocked(map, x + 2, y - 1)){
                    canTurnShape = false;
                }
                break;
        }
        return canTurnShape;
    }

    @Override
    public boolean canMoveLeft(GameMap map) {
        MapBlock block1 = null;
        MapBlock block2 = null;
        MapBlock block3 = null;
        switch (state){
            case STATE_TURN_1:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(3);
                break;
            case STATE_TURN_2:
                block1 = blocks.get(2);
                block2 = blocks.get(3);
                break;
            case STATE_TURN_3:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(2);
                break;
            case STATE_TURN_4:
                block1 = blocks.get(0);
                block2 = blocks.get(3);
                break;
        }
        if(GameUtils.isLeftBlocked(map, block1, block2, block3)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canMoveRight(GameMap map) {
        MapBlock block1 = null;
        MapBlock block2 = null;
        MapBlock block3 = null;
        switch (state){
            case STATE_TURN_1:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(2);
                break;
            case STATE_TURN_2:
                block1 = blocks.get(0);
                block2 = blocks.get(3);
                break;
            case STATE_TURN_3:
                block1 = blocks.get(0);
                block2 = blocks.get(1);
                block3 = blocks.get(3);
                break;
            case STATE_TURN_4:
                block1 = blocks.get(2);
                block2 = blocks.get(3);
                break;
        }
        if(GameUtils.isRightBlocked(map, block1, block2, block3)) {
            return false;
        }
        return true;
    }

    public static GameGraph getRandomIns(){
        return new AntiLGraph(new Random().nextInt(4) + 1);
    }
}
