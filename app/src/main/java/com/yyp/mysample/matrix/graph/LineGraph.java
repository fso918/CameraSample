package com.yyp.mysample.matrix.graph;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fso91 on 2016/9/22.
 */

public class LineGraph extends GameGraph {
    public static final int HORIZ = 0;
    public static final int VERTICAL = 1;
    private int state = VERTICAL;

    private LineGraph(){
        color = Color.BLUE;
        blocks = new ArrayList<MapBlock>();
        for(int i = 0; i < 4; i++){
            blocks.add(new MapBlock(4,-4 + i));
        }
        state = VERTICAL;
    }

    private void init(int x, int y){
        color = Color.BLUE;
        blocks = new ArrayList<MapBlock>();
        if(state == VERTICAL) {
            for (int i = 0; i < 4; i++) {
                blocks.add(new MapBlock(x, y + i));
            }
        }else{
            for (int i = 0; i < 4; i++) {
                blocks.add(new MapBlock(x + i, y));
            }
        }
    }

    private LineGraph(int x, int y, int state) {
        this(state);
        init(x, y);
    }

    private LineGraph(int state){
        this.state = state;
        init(0, 0);
    }

    public static LineGraph getRandomIns(){
        int state = new Random().nextInt(2);
        return new LineGraph(MatrixGameConstant.GAME_WIDTH / 2, state == 0 ? 0 : -3, state);
    }

    @Override
    public void turnShape() {
        if(state == HORIZ){
            state = VERTICAL;
            MapBlock b = blocks.get(0);
            b.setX(b.getX() + 2);
            b.setY(b.getY() - 2);
            b = blocks.get(1);
            b.setX(blocks.get(0).getX());
            b.setY(blocks.get(0).getY() + 1);
            b = blocks.get(2);
            b.setX(blocks.get(1).getX());
            b.setY(blocks.get(1).getY() + 1);
            b = blocks.get(3);
            b.setX(blocks.get(2).getX());
            b.setY(blocks.get(2).getY() + 1);
        }else{
            state = HORIZ;
            MapBlock b = blocks.get(0);
            b.setX(b.getX() - 2);
            b.setY(b.getY() + 2);
            b = blocks.get(1);
            b.setX(blocks.get(0).getX() + 1);
            b.setY(blocks.get(0).getY());
            b = blocks.get(2);
            b.setX(blocks.get(1).getX() + 1);
            b.setY(blocks.get(1).getY());
            b = blocks.get(3);
            b.setX(blocks.get(2).getX() + 1);
            b.setY(blocks.get(2).getY());
        }
    }

    @Override
    public boolean canFallen(GameMap gmap) {
        int i = blocks.get(0).getX();
        int j = blocks.get(0).getY();
        if(state == HORIZ){
            if(j + 1 < 0) return true;
            if(j + 1 == MatrixGameConstant.GAME_HEIGHT) return false;
            for(int a = i; a < i + 4; a++) {
                if(gmap.getRows().get(j + 1).getBlocks().get(a).isBlock()){
                    return false;
                }
            }
        }else if(state == VERTICAL){
            if(j + 4 == MatrixGameConstant.GAME_HEIGHT) return false;
            if(gmap.getRows().get(j + 4).getBlocks().get(i).isBlock()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canTurnShape(GameMap map) {
        int i = blocks.get(0).getX();
        int j = blocks.get(0).getY();
        if(state == HORIZ){
            i = i + 2;
            j = j - 2;
            if(j < 0 || j + 4 >= MatrixGameConstant.GAME_HEIGHT) return false;
            for(int a = j; a < j + 4; a++){
                if(a == j + 2) continue;
                if(map.getRows().get(a).getBlocks().get(i).isBlock()){
                    return false;
                }
            }
        }else if(state == VERTICAL){
            i = i - 2;
            j = j + 2;
            if(i < 0 || i + 4 > MatrixGameConstant.GAME_WIDTH) return false;
            for(int a = i; a < i + 4; a++){
                if(a == i + 2) continue;
                if(map.getRows().get(j).getBlocks().get(a).isBlock()){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canMoveLeft(GameMap map) {
        int i = blocks.get(0).getX();
        int j = blocks.get(0).getY();
        if(i == 0) return false;
        if(state == HORIZ){
            if(j < 0) return true;
            if(map.getRows().get(j).getBlocks().get(i - 1).isBlock()){
                return false;
            }
        }else if(state == VERTICAL){
            for(int a = j; a < j + 4; a++){
                if(a < 0) continue;
                if(map.getRows().get(a).getBlocks().get(i - 1).isBlock()){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canMoveRight(GameMap map) {
        int i = blocks.get(0).getX();
        int j = blocks.get(0).getY();
        if(state == HORIZ){
            if(j < 0) return true;
            if(i == MatrixGameConstant.GAME_WIDTH - 4) return false;
            if(map.getRows().get(j).getBlocks().get(i + 4).isBlock()){
                return false;
            }
        }else if(state == VERTICAL){
            if(i == MatrixGameConstant.GAME_WIDTH - 1) return false;
            for(int a = j; a < j + 4; a++){
                if(a < 0) continue;
                if(map.getRows().get(a).getBlocks().get(i + 1).isBlock()){
                    return false;
                }
            }
        }
        return true;
    }
}
