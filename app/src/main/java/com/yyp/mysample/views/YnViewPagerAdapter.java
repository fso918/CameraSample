/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.views;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by fso91 on 2016/10/13.
 */

public class YnViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;

    public YnViewPagerAdapter(List<View> views) {
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewGroup) container).removeView(mViews.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewGroup) container).addView(mViews.get(position), 0);
        return mViews.get(position);
    }
}