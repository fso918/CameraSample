package com.yyp.mysample.matrix.graph;

/**
 * Created by fso91 on 2016/9/27.
 */

public class GameGraphSimpleFactory {
    public static GameGraph createGameGraph(int type){
        GameGraph graph = null;
        switch (type){
            case GameGraph.TYPE_LINE:
                graph = LineGraph.getRandomIns();
                break;
            case GameGraph.TYPE_L:
                graph = LineGraph.getRandomIns();
                break;
            case GameGraph.TYPE_ANTI_L:
                graph = LineGraph.getRandomIns();
                break;
            case GameGraph.TYPE_BIG_BLOCK:
                graph = RectGraph.getRandomIns();
                break;
            case GameGraph.TYPE_TU:
                graph = RectGraph.getRandomIns();
                break;
            default:

        }
        return graph;
    }
}
