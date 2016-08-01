package com.yyp.mysample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yyp.mysample.Constant.Constant;
import com.yyp.mysample.feature.camera.ui.AbstractCameraActivity;
import com.yyp.mysample.feature.camera.ui.CameraActivity;

/**
 * Created by fso91 on 2016/8/1.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView cameraTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cameraTv = (TextView) findViewById(R.id.camera_sample);

        cameraTv.setOnClickListener(this);
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
        }
    }
}
