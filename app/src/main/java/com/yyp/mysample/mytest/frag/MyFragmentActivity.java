package com.yyp.mysample.mytest.frag;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.yyp.mysample.R;

/**
 * Created by fso91 on 2017/1/16.
 */

public class MyFragmentActivity extends Activity {
    private FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fragment);
        container = (FrameLayout) findViewById(R.id.fragment_content);
        TestPageFragment fragment = new TestPageFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_content, fragment);
        transaction.commit();

        fragment.setText("this is first pager fragment");
    }
}
