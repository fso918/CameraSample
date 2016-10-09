package com.yyp.mysample.matrix.graph;

/**
 * Created by fso91 on 2016/9/27.
 */

public class LGraph extends GameGraph {
    private int state = 0;

    private LGraph(){}

    @Override
    public void turnShape() {

    }

    @Override
    public boolean canFallen(GameMap gmap) {
        return false;
    }

    @Override
    public boolean canTurnShape(GameMap map) {
        return false;
    }

    @Override
    public boolean canMoveLeft(GameMap map) {
        return false;
    }

    @Override
    public boolean canMoveRight(GameMap map) {
        return false;
    }

    public static GameGraph getRandomIns(){

        return null;
    }
}
