package com.yyp.mysample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.Constant.Constant;
import com.yyp.mysample.basiergame.BezierGameActivity;
import com.yyp.mysample.constraintlayout.ConstrainLayoutActivity;
import com.yyp.mysample.feature.camera.ui.AbstractCameraActivity;
import com.yyp.mysample.feature.camera.ui.CameraActivity;
import com.yyp.mysample.matrix.MatrixGameActivity;
import com.yyp.mysample.mytest.TestActivity;
import com.yyp.mysample.refreshcomponent.YNRefreshFrameLayoutTestActivity;
import com.yyp.mysample.surfacegame.SurfaceGameActivity;
import com.yyp.mysample.xfermode.XfermodeSampleActivity;

import java.util.Random;

/**
 * Created by fso91 on 2016/8/1.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private TextView cameraTv;
    private Button surfaceGame;
    private Button matrixGame;
    private Button refreshView;
    private Button bezierGame;
    private Button myTest;
    private Button myAsync;
    private Button constraintLayout;
    private Button xfermodeSample;
    private ProgressDialog progressDialog;
    private Button horizFallView;
    private Button expandableList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cameraTv = (TextView) findViewById(R.id.camera_sample);
        surfaceGame = (Button) findViewById(R.id.surfaceGame);
        matrixGame = (Button) findViewById(R.id.matrix_game);
        refreshView = (Button) findViewById(R.id.refresh_view);
        bezierGame = (Button) findViewById(R.id.bezier_game);
        myTest = (Button) findViewById(R.id.my_test);
        myAsync = (Button) findViewById(R.id.my_async);
        constraintLayout = (Button)findViewById(R.id.constrain_layout);
        xfermodeSample = (Button) findViewById(R.id.xfermode_sample);
        horizFallView = (Button) findViewById(R.id.horiz_compat_view);
        expandableList = (Button) findViewById(R.id.expandable_list);

        cameraTv.setOnClickListener(this);
        surfaceGame.setOnClickListener(this);
        matrixGame.setOnClickListener(this);
        refreshView.setOnClickListener(this);
        bezierGame.setOnClickListener(this);
        myTest.setOnClickListener(this);
        myAsync.setOnClickListener(this);
        horizFallView.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);
        xfermodeSample.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        expandableList.setOnClickListener(this);
        progressDialog.setTitle("正在下载...");
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
            case R.id.bezier_game:
                intent = new Intent(this, BezierGameActivity.class);
                startActivity(intent);
                break;
            case R.id.my_test:
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.my_async:
                new TestAsyncTask<Void, Void>().execute();
                break;
            case R.id.constrain_layout:
                intent = new Intent(this, ConstrainLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.xfermode_sample:
                intent = new Intent(this, XfermodeSampleActivity.class);
                startActivity(intent);
                break;
            case R.id.horiz_compat_view:
                intent = new Intent(this, HorizCompatViewActivity.class);
                startActivity(intent);
                break;
            case R.id.expandable_list:
                intent = new Intent(this, ExpandableListActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class TestAsyncTask<P, R> extends AsyncTask<P, Integer, R>{
        int progress;
        Random random = new Random();

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            Toast.makeText(MainActivity.this, "开始下载....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(R r) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "下载完成！！！", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]/100);
            progressDialog.setMessage(values[0] + "%");
        }

        @Override
        protected R doInBackground(P... ps) {
            try {
                while (progress <= 100) {
                    Thread.currentThread().sleep(1000l);
                    progress += random.nextInt(20);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onProgressUpdate(progress);
                        }
                    });
                }
            }catch (Exception e) {

            }
            return null;
        }
    }
}
