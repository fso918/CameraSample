package com.yyp.mysample.mytest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.R;
import com.yyp.mysample.views.YnViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2016/9/22.
 */

public class ClipChildrenViewPageActivity extends Activity implements View.OnClickListener{
    private ViewPager vp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipchild_viewpager);
        vp = (ViewPager) findViewById(R.id.view_page);
        List<View> views = new ArrayList<>();
        TextView v = new TextView(this.getApplicationContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        v.setBackgroundColor(getResources().getColor(R.color.blue));
        v.setText("叶敏");
        views.add(v);
        v = new TextView(this.getApplicationContext());
        ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp1);
        v.setBackgroundColor(getResources().getColor(R.color.gray));
        v.setText("付敏");
        views.add(v);
        v = new TextView(this.getApplicationContext());
        ViewGroup.LayoutParams lp2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp2);
        v.setBackgroundColor(getResources().getColor(R.color.white));
        v.setText("张琪");
        views.add(v);
        YnViewPagerAdapter adapter = new YnViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);
    }

    @Override
    public void onClick(View view) {

    }

}
