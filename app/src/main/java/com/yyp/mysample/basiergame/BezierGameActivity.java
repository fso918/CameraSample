package com.yyp.mysample.basiergame;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.R;
import com.yyp.mysample.matrix.graph.GameGraph;
import com.yyp.mysample.matrix.graph.GameMap;
import com.yyp.mysample.matrix.graph.GameUtils;
import com.yyp.mysample.matrix.graph.MatrixGameConstant;

/**
 * Created by fso91 on 2016/9/22.
 */

public class BezierGameActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, View.OnTouchListener{
    public static final int GAME_EVENT_UPDATE_SCORE = 1;
    public static final int GAME_EVENT_GAME_OVER = 0;

    private SurfaceView surfaceView;

    private Handler gHandler;
    private Paint bezierPaint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_game);

        surfaceView = (SurfaceView) findViewById(R.id.my_bezier_game);
        surfaceView.getHolder().addCallback(this);
        bezierPaint = new Paint();
        bezierPaint.setColor(Color.parseColor("#235146"));
        bezierPaint.setStrokeWidth(5);
        bezierPaint.setStyle(Paint.Style.STROKE);
        surfaceView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void drawBezier(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        Path path = new Path();
        path.moveTo(200, 500);
        path.quadTo(300, 700, 500, 500);
        canvas.drawPath(path, bezierPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Toast.makeText(this, "Bezier game start", Toast.LENGTH_SHORT).show();
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        drawBezier(canvas);
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                int x = Float.valueOf(motionEvent.getX()).intValue();
                int y = Float.valueOf(motionEvent.getY()).intValue();
//                if(x - 300 < 0 || x - 500 > 0 ) {
//                    break;
//                }
                if(y-500<0){
                    break;
                }
                Path p = new Path();
                p.moveTo(200, 500);
                p.quadTo(x, y - 500 > 0 ? y : 500, 500, 500);
                Canvas c = surfaceView.getHolder().lockCanvas();
                c.drawColor(Color.WHITE);
                c.drawPath(p, bezierPaint);
                surfaceView.getHolder().unlockCanvasAndPost(c);
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
