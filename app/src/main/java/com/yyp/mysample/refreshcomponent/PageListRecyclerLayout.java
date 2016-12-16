/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 通用的支持下拉刷新的list列表<br/>
 * 垂直布局
 * Create by yanyunpeng
 * Date: 2016/10/25 10:29
 */
public class PageListRecyclerLayout<T> extends PageRecyclerLayout<T> {

	public PageListRecyclerLayout(Context context) {
		this(context, null);
	}

	public PageListRecyclerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		super.init(context, attrs);
		YNLinearLayoutManager layoutManager = new YNLinearLayoutManager(context);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);
	}

}
