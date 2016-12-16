package com.yyp.mysample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yyp.mysample.Constant.Constant;
import com.yyp.mysample.feature.camera.ui.AbstractCameraActivity;
import com.yyp.mysample.feature.camera.ui.CameraActivity;
import com.yyp.mysample.matrix.MatrixGameActivity;
import com.yyp.mysample.refreshcomponent.YNRefreshFrameLayoutTestActivity;
import com.yyp.mysample.surfacegame.SurfaceGameActivity;

/**
 * Created by fso91 on 2016/8/1.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private TextView cameraTv;
    private Button surfaceGame;
    private Button matrixGame;
    private Button refreshView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cameraTv = (TextView) findViewById(R.id.camera_sample);
        surfaceGame = (Button) findViewById(R.id.surfaceGame);
        matrixGame = (Button) findViewById(R.id.matrix_game);
        refreshView = (Button) findViewById(R.id.refresh_view);

        cameraTv.setOnClickListener(this);
        surfaceGame.setOnClickListener(this);
        matrixGame.setOnClickListener(this);
        refreshView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.camera_sample:
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(Constant.EXTRA_TITLE, "拍照示例");
                intent.putExtra(Constant.EXTRA_CAMERA_PIC_MODE, AbstractCameraActivity.TAKE_PIC_MODE_NOR);
                startActivityForResult(intent, 1);
                break;
            case R.id.surfaceGame:
                intent = new Intent(this, SurfaceGameActivity.class);
                startActivity(intent);
                break;
            case R.id.matrix_game:
                intent = new Intent(this, MatrixGameActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh_view:
                intent = new Intent(this, YNRefreshFrameLayoutTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
