/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.yyp.mysample.R;
import com.yyp.mysample.utils.DisplayUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 通用的支持下拉刷新、上拉加载更多和分页的列表<br/>
 * 此控件主要处理各种状态的显示
 * Create by yanyunpeng
 * Date: 2016/10/22 11:22
 */
public class PageRecyclerLayout<T> extends MultiStateLayout {

    protected YNRefreshViewListener refreshViewListener;
    /** 显示列表信息 **/
    protected YNRecyclerView recyclerview;
    /** 装载数据的Adapter **/
    protected BaseRecyclerViewAdapter<T> adapter;

    private AtomicBoolean loading = new AtomicBoolean(false);

    private SwipeRefreshLayout swipeRefreshLayout;

    public PageRecyclerLayout(Context context) {
        this(context, null);
    }

    public PageRecyclerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRecyclerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        setEmptyLayoutResource(R.layout.widget_refresh_empty_view);             //可以刷新的空View
        setErrorLayoutResource(R.layout.widget_refresh_error_view);             //可以刷新的错误View
        swipeRefreshLayout = new SwipeRefreshLayout(context);

        YNRecyclerView recyclerview = new YNRecyclerView(context);
        recyclerview.setHasFixedSize(true);
        recyclerview.setVerticalScrollBarEnabled(false);

        swipeRefreshLayout.addView(recyclerview, new SwipeRefreshLayout.LayoutParams(SwipeRefreshLayout.LayoutParams.MATCH_PARENT,
                SwipeRefreshLayout.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshViewListener.onLoadData(1);    //请求数据
            }
        });

        this.recyclerview = recyclerview;
//        setContentView(swipeRefreshLayout);
        setContentView(recyclerview);
    }

    @Override
    protected void initEmptyView(ViewGroup emptyView) {
        super.initEmptyView(emptyView);
        if(emptyView instanceof SwipeRefreshLayout) {
            ((SwipeRefreshLayout) emptyView).setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh() {
                    refreshViewListener.onLoadData(1);
                }
            });
            if (topSpace != 0) {
                ((SwipeRefreshLayout) emptyView).setProgressViewOffset(false, topSpace, topSpace + DisplayUtils.dip2px(context, 30));
            }
        }
    }

    @Override
    protected void initErrorView(ViewGroup errorView) {
        super.initErrorView(errorView);
        if(errorView instanceof SwipeRefreshLayout) {
            ((SwipeRefreshLayout) errorView).setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh() {
                    refreshViewListener.onLoadData(1);
                }
            });
            if (topSpace != 0) {
                ((SwipeRefreshLayout) errorView).setProgressViewOffset(false, topSpace, topSpace + DisplayUtils.dip2px(context, 30));
            }
        }
    }

    public YNRecyclerView getRecyclerView() {
        return recyclerview;
    }

    /**
     * 设置顶部和底部空隙，使下拉刷新和进度条、空view、加载出错view、正常显示view位置显示正确
     */
    public void setSpace(int topSpace, int bottomSpace) {
        super.setSpace(topSpace, bottomSpace);
        // 矫正下拉刷新view的位置（scale设置为true的话，转圈会模糊，其它效果没看到，不清楚实际意义）
        ((SwipeRefreshLayout) contentView).setProgressViewOffset(false, topSpace, topSpace + DisplayUtils.dip2px(context, 30));
        if (emptyView != null) {
            ((SwipeRefreshLayout) emptyView).setProgressViewOffset(false, topSpace, topSpace + DisplayUtils.dip2px(context, 30));
        }
        if (errorView != null) {
            ((SwipeRefreshLayout) errorView).setProgressViewOffset(false, topSpace, topSpace + DisplayUtils.dip2px(context, 30));
        }
        // recyclerview添加顶部空栏
        recyclerview.setTopSpace(topSpace);
    }

    /**
     * 获取填充数据
     * @return
     */
    public List<T> getData() {
        if (adapter == null) {
            throw new RuntimeException("adapter is null, must invoke setAdapter method before.");
        }
        return adapter.getDatas();
    }

    /**
     * 获取当前显示的页数
     * @return
     */
    public int getPageNum() {
        return adapter == null ? 0 : adapter.getPageNum();
    }

    public void setAdapter(Adapter adapter) {
        if(adapter instanceof  BaseRecyclerViewAdapter) {
            this.adapter = (BaseRecyclerViewAdapter<T>) adapter;
            recyclerview.setAdapter(adapter);
        }else{
            throw new RuntimeException("Adapter class ERROR！");
        }
    }

    public void loadData(int pageNum){
        refreshViewListener.onLoadData(pageNum);
    }

    public BaseRecyclerViewAdapter<T> getAdapter() {
        return adapter;
    }

    /**
     * 隐藏刷新view
     */
    public void stopRefreshing() {
        ((SwipeRefreshLayout) contentView).setRefreshing(false);
        if (errorView != null) {
            ((SwipeRefreshLayout) errorView).setRefreshing(false);
        }
        if (emptyView != null) {
            ((SwipeRefreshLayout) emptyView).setRefreshing(false);
        }
    }

    /**
     * 显示刷新bar
     */
    public void showRefreshingBar(){
        ((SwipeRefreshLayout) contentView).setRefreshing(true);
        if (errorView != null) {
            ((SwipeRefreshLayout) errorView).setRefreshing(true);
        }
        if (emptyView != null) {
            ((SwipeRefreshLayout) emptyView).setRefreshing(true);
        }
    }

    public AtomicBoolean getLoading() {
        return loading;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout==null?new SwipeRefreshLayout(getContext()):swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public YNRefreshViewListener getRefreshViewListener() {
        return refreshViewListener;
    }

    public void setRefreshViewListener(YNRefreshViewListener refreshViewListener) {
        this.refreshViewListener = refreshViewListener;
    }
}
