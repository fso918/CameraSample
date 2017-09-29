/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by fso91 on 2016/10/26.
 */

public class YNLinearLayoutManager extends LinearLayoutManager {
    public YNLinearLayoutManager(Context context) {
        super(context);
    }

    public YNLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
        Log.i("TEST_YN", "dy:--->" + dy + ",scrollRange:--->" + scrollRange + ",overScroll:--->" + (dy - scrollRange));
        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
