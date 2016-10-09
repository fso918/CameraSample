package com.yyp.mysample.matrix;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.R;
import com.yyp.mysample.matrix.graph.GameMap;
import com.yyp.mysample.matrix.graph.GameUtils;
import com.yyp.mysample.matrix.graph.GameGraph;
import com.yyp.mysample.matrix.graph.LineGraph;
import com.yyp.mysample.matrix.graph.MatrixGameConstant;

/**
 * Created by fso91 on 2016/9/22.
 */

public class MatrixGameActivity extends AppCompatActivity  implements SurfaceHolder.Callback, View.OnClickListener{
    public static final int GAME_EVENT_UPDATE_SCORE = 1;
    public static final int GAME_EVENT_GAME_OVER = 0;

    private SurfaceView surfaceView;
    private Thread drawThread;
    private Thread fallThread;
    private boolean isDrawing = false;
    private float radius = 10f;
    private Paint paint;
    int i = 0;
    int touchX, touchY;
    int gWidth, gHeight;
    private GameMap gMap;
    private GameGraph graph;

    private TextView moveLeftTv;
    private TextView moveRightTv;
    private TextView turnShapeTv;
    private TextView speedAcculateTv;
    private TextView scoreTv;

    private int fallSpeed = MatrixGameConstant.GAME_SPEED_STEP_1;        //下落速度，数值越大，下落越慢
    private int step = 1;
    private int score = 0;

    private Handler gHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_game);

        surfaceView = (SurfaceView) findViewById(R.id.my_surface_game);
        surfaceView.getHolder().addCallback(this);
        moveLeftTv = (TextView) findViewById(R.id.move_left);
        moveRightTv = (TextView) findViewById(R.id.move_right);
        turnShapeTv = (TextView) findViewById(R.id.turn_shape);
        speedAcculateTv = (TextView) findViewById(R.id.speed_acculate);
        scoreTv = (TextView) findViewById(R.id.score_tv);

        moveLeftTv.setOnClickListener(this);
        moveRightTv.setOnClickListener(this);
        turnShapeTv.setOnClickListener(this);
        speedAcculateTv.setOnTouchListener(new OnAcculateTouchListener());

        gHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GAME_EVENT_UPDATE_SCORE:
                        scoreTv.setText(String.format("得分：%1$s", score + ""));
                        break;
                    case GAME_EVENT_GAME_OVER:
                        Toast.makeText(MatrixGameActivity.this, "总得分：" + score, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.turn_shape:
                    if (graph != null && graph.canTurnShape(gMap)) {
                        gMap.resetGraph(graph);
                        graph.turnShape();
                    }
                    break;
                case R.id.move_left:
                    if (graph != null && graph.canMoveLeft(gMap)) {
                        gMap.resetGraph(graph);
                        graph.moveLeft();
                    }
                    break;
                case R.id.move_right:
                    if (graph != null && graph.canMoveRight(gMap)) {
                        gMap.resetGraph(graph);
                        graph.moveRight();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        synchronized (gMap) {
            if (graph != null) {
                graph.draw(gMap);
            }
            gMap.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        gWidth = surfaceView.getWidth();
        gHeight = surfaceView.getHeight();

        gMap = new GameMap(MatrixGameConstant.GAME_WIDTH, MatrixGameConstant.GAME_HEIGHT);
        gMap.getRows().get(15).getBlocks().get(5).setBlock(false).setColor(Color.RED);
        gMap.setX(50);
        gMap.setY(150);
        drawThread = new DrawThread();
        drawThread.start();

        fallThread = new FallThread();
        fallThread.start();
        graph = GameUtils.getRandomGraph();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
        try {
            drawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDrawing = false;
    }

    class DrawThread extends Thread{
        Canvas canvas;
        @Override
        public void run() {
            while(isDrawing) {
                try {
                    synchronized (gMap) {
                        canvas = surfaceView.getHolder().lockCanvas(null);
                        draw(canvas);
                        surfaceView.getHolder().unlockCanvasAndPost(canvas);
                        canvas = null;
                        sleep(50);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    class FallThread extends Thread{
        @Override
        public void run() {
            try {
                while (isDrawing) {
                    if (graph != null) {
                        synchronized (gMap) {
                            if (graph.canFallen(gMap)) {
                                gMap.resetGraph(graph);
                                graph.fall();
                            } else {
                                int rows = gMap.deleteRow();
                                score += GameUtils.getScore(rows);
                                step = GameUtils.getStep(score);
                                Message msg = new Message();
                                msg.what = GAME_EVENT_UPDATE_SCORE;
                                gHandler.sendMessage(msg);
                                if(graph != null && graph.isOutOfMap(gMap)){
                                    isDrawing = false;
                                    Message msg1 = new Message();
                                    msg.what = GAME_EVENT_GAME_OVER;
                                    gHandler.sendMessage(msg1);
                                }else {
                                    graph = GameUtils.getRandomGraph();
                                    fallSpeed = GameUtils.getGameSpeed(step);
                                }
                            }
                        }
                    }
                    sleep(fallSpeed);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class OnAcculateTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(graph != null){
                        fallSpeed = MatrixGameConstant.GAME_SPEED_ACCELATE;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    fallSpeed = GameUtils.getGameSpeed(step);
                    break;
            }
            return true;
        }
    }

}
