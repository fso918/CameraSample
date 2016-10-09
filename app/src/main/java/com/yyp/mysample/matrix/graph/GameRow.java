package com.yyp.mysample.matrix.graph;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2016/9/22.
 */

public class GameRow {
    private int index;
    private int width;
    private int x;
    private int y;
    private List<Block> blocks;

    public GameRow(int width){
        this.width = width;
        blocks = new ArrayList<Block>();
        for(int i = 0; i < width; i++){
            blocks.add(new Block());
        }
    }

    public GameRow(int index, int width){
        this(width);
        this.index = index;
    }

    public void draw(Canvas canvas){
        for(Block b : blocks){
            b.draw(canvas);
        }
    }

    public boolean delete(){
        if(canDelete()){
            for(Block b : blocks){
                b.setBlock(false);
                b.setColor(MatrixGameConstant.GAME_DEFAULT_COLOR);
            }
            return true;
        }
        return false;
    }

    private boolean canDelete(){
        for(Block b : blocks){
            if(!b.isBlock()){
                return false;
            }
        }
        return true;
    }

    public void copyBlocks(GameRow row){
        for(int i = 0; i < blocks.size(); i++){
            blocks.get(i).setColor(row.getBlocks().get(i).getColor()).setBlock(row.getBlocks().get(i).isBlock());
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        for(int i = 0; i < width; i++){
            blocks.get(i).setX(x + i * MatrixGameConstant.GAME_BLOCK_WIDTH);
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        for(Block b : blocks){
            b.setY(y);
        }
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
