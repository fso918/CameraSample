package com.yyp.mysample.matrix.graph;

import com.yyp.mysample.surfacegame.GameConstants;

import java.util.Random;

/**
 * Created by fso91 on 2016/9/25.
 */

public class GameUtils {
    public static Random random = new Random();

    public static GameGraph getRandomGraph(){
        return GameGraphSimpleFactory.createGameGraph(random.nextInt(MatrixGameConstant.GAME_GRAPH_COUNT));
    }
    public static int getGameSpeed(int step){
        int speed = MatrixGameConstant.GAME_SPEED_STEP_1;
        switch (step){
            case 1:
                speed = MatrixGameConstant.GAME_SPEED_STEP_1;
                break;
            case 2:
                speed = MatrixGameConstant.GAME_SPEED_STEP_2;
                break;
            case 3:
                speed = MatrixGameConstant.GAME_SPEED_STEP_3;
                break;
            case 4:
                speed = MatrixGameConstant.GAME_SPEED_STEP_4;
                break;
            case 5:
                speed = MatrixGameConstant.GAME_SPEED_STEP_5;
                break;
            case 6:
                speed = MatrixGameConstant.GAME_SPEED_STEP_6;
                break;
            case 7:
                speed = MatrixGameConstant.GAME_SPEED_STEP_7;
                break;
            case 8:
                speed = MatrixGameConstant.GAME_SPEED_STEP_8;
                break;
            case 9:
                speed = MatrixGameConstant.GAME_SPEED_STEP_9;
                break;
            case 10:
                speed = MatrixGameConstant.GAME_SPEED_STEP_10;
                break;
        }
        return speed;
    }

    public static int getScore(int count){
        switch (count){
            case 1:
                return 100;
            case 2:
                return 300;
            case 3:
                return 600;
            case 4:
                return 1000;
        }
        return 0;
    }

    public static int getStep(int score){
        if(score >= 46000){
            return 10;
        }else if(score >= 36000){
            return 9;
        }else if(score >= 28000){
            return 8;
        }else if(score >= 21000){
            return 7;
        }else if(score >= 15000){
            return 6;
        }else if(score >= 10000){
            return 5;
        }else if(score >= 6000){
            return 4;
        }else if(score >= 3000){
            return 3;
        }else if(score >= 1000){
            return 2;
        }
        return 1;
    }

    public static Block getBlock(GameMap map, int x, int y){
        if(map != null && x < MatrixGameConstant.GAME_WIDTH && y < MatrixGameConstant.GAME_HEIGHT){
            return map.getRows().get(y).getBlocks().get(x);
        }
        return null;
    }
}
