/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 用于YNRefreshLinearLayout刷新控件的View
 * Create by yanyunpeng
 * Date: 2016/10/27 14:28
 */

public abstract class YNRefreshAnimView extends FrameLayout {
    public static final int DRAGING = 1;
    public static final int DRAGING_RELEASE_REFRESH = 2;
    public static final int REFRESHING = 3;
    public static final int FINISH = 4;

    public YNRefreshAnimView(Context context) {
        super(context);
    }

    public YNRefreshAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YNRefreshAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 用于获取View的高度，该高度必须是一个固定的值
     * @return
     */
    public abstract int getTotalHeight();

    /**
     * 开始加载动画的回调
     */
    public abstract void startLoadingAnim();

    /**
     * 停止加载动画的回调
     */
    public abstract void stopLoadingAnim();
    /**
     * 状态变化时的回调，状态有：1.下拉，2.下拉且松开后可以刷新，3.刷新中，4.刷新完毕
     */
    public abstract void onDragingStatusChanged(int status);

    /**
     * 正在下拉的回调
     * @param dragDistance 下拉的距离。
     */
    public abstract void onDraging(float dragDistance);
}
