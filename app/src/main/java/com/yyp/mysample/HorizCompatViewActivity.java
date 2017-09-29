package com.yyp.mysample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyp.mysample.views.HorizFallView;

import java.util.ArrayList;
import java.util.List;

public class HorizCompatViewActivity extends AppCompatActivity {
    private HorizFallView horizFallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horiz_compat_view);
        horizFallView = (HorizFallView) findViewById(R.id.horiz_fall_view);
        List<String> language = new ArrayList<>();
        language.add("C");
        language.add("C++");
        language.add("Java");
        language.add("Javascript");
        language.add("CSS");
        language.add("Python");
        language.add("shell");
        language.add("C#");
        language.add(".NET");
        BaseAdapter adapter = new HorizFallAdapter(language);
        horizFallView.setAdapter(adapter);
    }

    private class HorizFallAdapter extends BaseAdapter{
        private List<String> data;

        private HorizFallAdapter(List<String> data){
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = new TextView(HorizCompatViewActivity.this);
            tv.setText(data.get(i));
            tv.setTextSize(25);
            tv.setPadding(20, 20, 20, 20);
            tv.setBackgroundColor(Color.parseColor("#ffffcd"));
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            return tv;
        }
    }
}
