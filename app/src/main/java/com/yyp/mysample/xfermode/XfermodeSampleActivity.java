package com.yyp.mysample.xfermode;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yyp.mysample.R;
import com.yyp.mysample.views.LikeTaoBaoProcess;
import com.yyp.mysample.views.XfermodeSampleView;

public class XfermodeSampleActivity extends AppCompatActivity {
    private XfermodeSampleView xv1;
    private XfermodeSampleView xv2;
    private XfermodeSampleView xv3;
    private XfermodeSampleView xv4;

    private XfermodeSampleView xv5;
    private XfermodeSampleView xv6;
    private XfermodeSampleView xv7;
    private XfermodeSampleView xv8;

    private XfermodeSampleView xv9;
    private XfermodeSampleView xv10;
    private XfermodeSampleView xv11;
    private XfermodeSampleView xv12;

    private XfermodeSampleView xv13;
    private XfermodeSampleView xv14;
    private XfermodeSampleView xv15;
    private XfermodeSampleView xv16;

    private LikeTaoBaoProcess taoBaoProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_sample);
        xv1 = (XfermodeSampleView) findViewById(R.id.xv_1);
        xv2 = (XfermodeSampleView) findViewById(R.id.xv_2);
        xv3 = (XfermodeSampleView) findViewById(R.id.xv_3);
        xv4 = (XfermodeSampleView) findViewById(R.id.xv_4);

        xv5 = (XfermodeSampleView) findViewById(R.id.xv_5);
        xv6 = (XfermodeSampleView) findViewById(R.id.xv_6);
        xv7 = (XfermodeSampleView) findViewById(R.id.xv_7);
        xv8 = (XfermodeSampleView) findViewById(R.id.xv_8);

        xv9 = (XfermodeSampleView) findViewById(R.id.xv_9);
        xv10 = (XfermodeSampleView) findViewById(R.id.xv_10);
        xv11 = (XfermodeSampleView) findViewById(R.id.xv_11);
        xv12 = (XfermodeSampleView) findViewById(R.id.xv_12);

        xv13 = (XfermodeSampleView) findViewById(R.id.xv_13);
        xv14 = (XfermodeSampleView) findViewById(R.id.xv_14);
        xv15 = (XfermodeSampleView) findViewById(R.id.xv_15);
        xv16 = (XfermodeSampleView) findViewById(R.id.xv_16);

        xv1.setMode(PorterDuff.Mode.CLEAR);
        xv2.setMode(PorterDuff.Mode.SRC);
        xv3.setMode(PorterDuff.Mode.DST);
        xv4.setMode(PorterDuff.Mode.SRC_OVER);

        xv5.setMode(PorterDuff.Mode.DST_OVER);
        xv6.setMode(PorterDuff.Mode.SRC_IN);
        xv7.setMode(PorterDuff.Mode.DST_IN);
        xv8.setMode(PorterDuff.Mode.SRC_OUT);

        xv9.setMode(PorterDuff.Mode.DST_OUT);
        xv10.setMode(PorterDuff.Mode.SRC_ATOP);
        xv11.setMode(PorterDuff.Mode.DST_ATOP);
        xv12.setMode(PorterDuff.Mode.XOR);

        xv13.setMode(PorterDuff.Mode.DARKEN);
        xv14.setMode(PorterDuff.Mode.LIGHTEN);
        xv15.setMode(PorterDuff.Mode.MULTIPLY);
        xv16.setMode(PorterDuff.Mode.SCREEN);

        taoBaoProcess = (LikeTaoBaoProcess) findViewById(R.id.tao_bao_progress);
        xv2.postDelayed(new Runnable() {
            @Override
            public void run() {
                taoBaoProcess.setProgress(100);
            }
        }, 1000);
    }
}
