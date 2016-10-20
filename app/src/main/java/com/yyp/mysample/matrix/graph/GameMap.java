package com.yyp.mysample.matrix.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2016/9/22.
 */

public class GameMap {
    private int width;
    private int height;
    private int x;
    private int y;
    private List<GameRow> rows;
    private List<MapBlock> graphCache;

    public GameMap(int width, int height){
        this.height = height;
        this.width = width;

        rows = new ArrayList<GameRow>();
        for(int i = 0; i < height; i++){
            rows.add(new GameRow(i, width));
        }
    }

    public void draw(Canvas canvas){
        Rect rect = new Rect(x- 1, y - 1, x + MatrixGameConstant.GAME_BLOCK_WIDTH * width + 2, y + MatrixGameConstant.GAME_BLOCK_WIDTH * height + 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        canvas.drawRect(rect, paint);
        for(GameRow r : rows){
            r.draw(canvas);
        }
    }

    public void resetGraph(){
        if(graphCache == null) return;
        List<MapBlock> blocks = graphCache;
        for(MapBlock block : blocks){
            if(block.getY() >= 0) {
                rows.get(block.getY()).getBlocks().get(block.getX()).resetColor().setBlock(false);
            }
        }
    }

    public int deleteRow(){
        int count = 0;
        for(GameRow row : rows){
            if(row.delete()){
                count++;
                int index = row.getIndex();
                for(int i = index; i > 0; i--){
                    rows.get(i).copyBlocks(rows.get(i -1));
                }
                for(Block b : rows.get(0).getBlocks()){
                    b.setBlock(false).setColor(MatrixGameConstant.GAME_DEFAULT_COLOR);
                }
            }
        }
        return count;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        for(GameRow r : rows){
            r.setX(x);
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        for(int i = 0; i < height; i++){
            rows.get(i).setY(y + i * MatrixGameConstant.GAME_BLOCK_WIDTH);
        }
    }

    public void block(GameGraph graph){
        resetGraph();
        if(graph == null) return;
        List<MapBlock> blocks = graph.getBlocks();
        for (MapBlock b : blocks) {
            if (b.getY() >= 0) {
                Block bl = GameUtils.getBlock(this, b.getX(), b.getY());
                if(bl != null){
                    bl.setColor(b.getColor()).setBlock(true);
                }
            }
        }
    }

    public List<GameRow> getRows() {
        return rows;
    }

    public List<MapBlock> getGraphCache() {
        return graphCache;
    }

    public void setGraphCache(List<MapBlock> graphCache) {
        this.graphCache = graphCache;
    }
}
