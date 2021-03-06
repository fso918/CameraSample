package com.yyp.mysample.matrix.graph;

import android.util.Log;

import com.yyp.mysample.surfacegame.GameConstants;

import java.util.List;
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

    /**
     * 用于判断传入的MapBlock的正下方是否已经存在方块
     * @param gMap
     * @param blocks
     * @return
     */
    public static boolean isDownBlocked(GameMap gMap, MapBlock... blocks){
        if(blocks != null && blocks.length != 0){
            for(MapBlock block : blocks){
                if(block != null){
                    if(block.getY() + 1 == MatrixGameConstant.GAME_HEIGHT) return true;
                    Block b = GameUtils.getBlock(gMap, block.getX(), block.getY() + 1);
                    if(b == null) continue;
                    if(b.isBlock())
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isLeftBlocked(GameMap gMap, MapBlock... blocks){
        if(blocks != null && blocks.length != 0){
            for(MapBlock block : blocks){
                if(block != null){
                    Block b = GameUtils.getBlock(gMap, block.getX() - 1, block.getY());
                    if(b == null) return true;
                    if(b.isBlock())
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isRightBlocked(GameMap gMap, MapBlock... blocks){
        if(blocks != null && blocks.length != 0){
            for(MapBlock block : blocks){
                if(block != null){
                    Block b = GameUtils.getBlock(gMap, block.getX() + 1, block.getY());
                    if(b == null) return true;
                    if(b.isBlock())
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isBlocked(GameMap map, int x, int y){
        if(x < 0 || x >= MatrixGameConstant.GAME_WIDTH || y < 0 || y >= MatrixGameConstant.GAME_HEIGHT){
            return true;
        }
        return map.getRows().get(y).getBlocks().get(x).isBlock();
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
//        Log.i("TESTst", "mW:" + map.getWidth() + ",mH:" + map.getHeight() + ",x:" + x + ",y:" + y);
        if(map != null && x < map.getWidth() && y < map.getHeight() && x >= 0 && y >= 0){
            return map.getRows().get(y).getBlocks().get(x);
        }
        return null;
    }

    public static List<MapBlock> fallRows(List<MapBlock> blocks, int rows){
        if(blocks == null) return null;
        for(MapBlock block : blocks){
            block.setY(block.getY() + rows);
        }
        return blocks;
    }

    public static String toString(List<MapBlock> blocks){
        if(blocks == null || blocks.size() == 0) return "========================================";
        String str = "";
        for(MapBlock b : blocks){
            str += "[" + b.getX() + "," + b.getY() + "],";
        }
        return str;
    }
}
