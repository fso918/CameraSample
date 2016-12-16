/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

/**
 * 页面刷新监听
 * Create by yanyunpeng
 * Date: 2016/10/24 11:20
 */

public interface YNRefreshViewListener {

    /**
     * 加载数据监听，当pageNum == 1 时，代表刷新操作，为其他自然数时代表下拉加载更多
     * @param pageNum 当前显示数据的页数，发送http请求时不需要+1
     */
    void onLoadData(int pageNum);
}
