package com.yyp.mysample.feature.camera.ui;

import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by fso91 on 2016/7/28.
 */
public class CameraActivity extends AbstractCameraActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPictureComplete(String imgLocalPath) {
        Toast.makeText(this, "拍摄照片成功！", Toast.LENGTH_SHORT).show();
    }
}
