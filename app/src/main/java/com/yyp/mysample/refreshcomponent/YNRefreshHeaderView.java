/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyp.mysample.R;
import com.yyp.mysample.utils.DisplayUtils;

/**
 * 用于显示刷新动画的View
 * Create by yanyunpeng
 * Date: 2016/10/27 17:22
 */
public class YNRefreshHeaderView extends YNRefreshAnimView {
    private Context context;
    private int height = -1;
    private TextView tv;
    private ImageView iv;
    private int oriIvH = -1;
    private int oriIvW = -1;
    private boolean cancelAnim = false;

    public YNRefreshHeaderView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public YNRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public YNRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.widget_refresh_view_header, null);
        tv = (TextView) view.findViewById(R.id.tv_empty);
        iv = (ImageView) view.findViewById(R.id.iv_empty);
        oriIvW = oriIvH = DisplayUtils.dip2px(context, 50);
        addView(view);
    }

    @Override
    public int getTotalHeight() {
        if(height == -1){
            height = DisplayUtils.dip2px(context, 80);
        }
        return height;
    }

    @Override
    public void startLoadingAnim() {
        Animation animation = new RotateAnimation(0, 360, oriIvW / 2, oriIvH / 2);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cancelAnim = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!cancelAnim) {
                    iv.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(animation);
    }

    @Override
    public void stopLoadingAnim() {
        iv.clearAnimation();
        cancelAnim = true;
    }

    @Override
    public void onDragingStatusChanged(int status) {
        switch (status){
            case DRAGING:
                tv.setText("下拉刷新");
                break;
            case DRAGING_RELEASE_REFRESH:
                tv.setText("松开刷新数据");
                break;
            case REFRESHING:
                tv.setText("正在刷新");
                break;
            case FINISH:
                tv.setText("数据已刷新");
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDraging(float dragDistance) {
        iv.setRotation(360 * dragDistance);
    }
}
