package com.yyp.mysample.mytest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyp.mysample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fso91 on 2017/1/18.
 */

public class RecyclerWrapcontentActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_wrapcontent);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TestBean> data = new ArrayList<>();
        TestBean bean = null;
        for(int i = 0; i < 6; i++) {
            bean = new TestBean();
            bean.setName("Android wrap [" + i + "]");
            data.add(bean);
        }
        TestRecyclerAdapter adapter = new TestRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class TestBean{
        private String name;
        private int index;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public class TestRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Context context;
        private List<TestBean> data;
        public TestRecyclerAdapter(Context context, List<TestBean> data) {
            this.data = data;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_test, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TestBean bean = data.get(position);
            TestViewHolder vh = (TestViewHolder) holder;
            vh.nameTv.setText(bean.getName());
        }

        private class TestViewHolder extends RecyclerView.ViewHolder{
            private TextView nameTv;
            public TestViewHolder(View itemView) {
                super(itemView);

                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            }
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }
}
