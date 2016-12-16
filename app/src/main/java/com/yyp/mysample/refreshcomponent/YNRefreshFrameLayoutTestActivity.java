/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2016/10/26.
 */

public class YNRefreshFrameLayoutTestActivity extends Activity {
    private YNRefreshLinearLayout refreshLayout;
    private YNRecyclerView recyclerView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_refresh_view_test);
            refreshLayout = (YNRefreshLinearLayout) findViewById(R.id.refresh_frame_layout);
            refreshLayout.setRefreshAnimView(new YNRefreshHeaderView(this));
            recyclerView = (YNRecyclerView) refreshLayout.getRefreshView();
            refreshLayout.setRefreshListen(new YNRefreshLinearLayout.OnRefreshListen() {
                @Override
                public void onRefresh() {
                    showToast("正在刷新数据");
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.hideRefresh();
                            showToast("数据刷新完成");
                        }
                    }, 3000);
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            List<TestBean> listData = new ArrayList<TestBean>();
            TestBean bean = null;
            for (int i = 0; i < 20; i++) {
                bean = new TestBean();
                bean.setName("item[" + i + "]");
                listData.add(bean);
            }
            TestBeanAdapter adapter = new TestBeanAdapter(this, recyclerView);
            recyclerView.setAdapter(adapter);
            adapter.setData(listData);
            refreshLayout.showRefresh();
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.hideRefresh();
                }
            }, 3000);
            try {
                recyclerView.setHeaderRes(R.layout.widget_refresh_error_view_test);
            } catch (Exception e) {
                Log.i("TEST_YN", e.toString());
                e.printStackTrace();
            }
        }catch (Exception ee){
            Log.i("TEST_YN", ee.toString());
            ee.printStackTrace();
        }
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

class TestBeanAdapter extends BaseRecyclerViewAdapter<TestBean>{
    public TestBeanAdapter(Context activity, RecyclerView recyclerView) {
        super(activity, recyclerView);
    }

    public TestBeanAdapter(Context activity, RecyclerView recyclerView, int spaceFooterHeight) {
        super(activity, recyclerView, spaceFooterHeight);
    }

    public TestBeanAdapter(Context activity, RecyclerView recyclerView, boolean showFooter) {
        super(activity, recyclerView, showFooter);
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        TestBean tb = data.get(position);
        TestBeanViewHolder vh = (TestBeanViewHolder) holder;
        vh.textView.setText(tb.getName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.widget_test_bean_adapter, null);

        return new TestBeanViewHolder(itemView, viewType);
    }

    class TestBeanViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public TestBeanViewHolder(View itemView, int viewType) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_empty);
        }

    }
}

class TestBean{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
