package com.yyp.mysample.surfacegame;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.yyp.mysample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2016/9/21.
 */

public class SurfaceGameActivity extends Activity implements SurfaceHolder.Callback, View.OnTouchListener{
    private SurfaceView surfaceView;
    private Thread drawThread;
    private boolean isDrawing = false;
    private float radius = 10f;
    private Paint paint;
    int i = 0;
    int touchX, touchY;
    List<ScreenDot> dots = new ArrayList<ScreenDot>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_game);
        surfaceView = (SurfaceView) findViewById(R.id.my_surface_game);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if(paint != null){
//            if(i % 2 == 0){
//                paint.setColor(Color.BLACK);
//            }else {
//                paint.setColor(Color.BLUE);
//            }
//            i++;
//        }
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX = (int) motionEvent.getX();
                touchY = (int) motionEvent.getY();
                for(ScreenDot dot : dots){
                    if(dot.contaim(touchX, touchY)){
                        dot.setState(ScreenDot.STATE_DISPEARING);
                    }
                }
                break;
        }
        return false;
    }

    class DrawThread extends Thread{
        Canvas canvas;
        @Override
        public void run() {
            while(isDrawing) {
                try {
                    synchronized (surfaceView.getHolder()) {
                        canvas = surfaceView.getHolder().lockCanvas(null);
                        draw(canvas);
                        sleep(50);
                        surfaceView.getHolder().unlockCanvasAndPost(canvas);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        if(dots.size() == 0){
            dots.add(new ScreenDot(surfaceView.getWidth(), surfaceView.getHeight()));
        }
        for(ScreenDot dot : dots){
            if(dot.getState() == ScreenDot.STATE_GONE){
                dots.remove(dot);
                continue;
            }
            dot.draw(canvas);
        }


//        if(paint == null){
//            paint = new Paint();
//            paint.setColor(Color.BLUE);
//            paint.setAntiAlias(true);
//            paint.setStyle(Paint.Style.FILL);
//            paint.setStrokeWidth(5);
//        }
//        canvas.drawColor(Color.WHITE);
//        canvas.drawCircle(0, 0, radius++, paint);
//        if(radius > 100f){
//            radius = 10f;
//        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        drawThread = new DrawThread();
        drawThread.start();
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
}
