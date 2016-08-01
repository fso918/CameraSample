package com.yyp.mysample.Constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by fso91 on 2016/8/1.
 */
public interface Constant {
    String EXTRA_CAMERA_PIC_MODE = "camera_pic_mode";
    String EXTRA_TITLE = "title";

    String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mysample" + File
            .separator;
}
