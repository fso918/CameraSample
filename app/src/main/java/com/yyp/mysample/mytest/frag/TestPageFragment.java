package com.yyp.mysample.mytest.frag;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyp.mysample.R;

/**
 * Created by fso91 on 2017/1/16.
 */

public class TestPageFragment extends Fragment {
    private Context mContext;
    private TextView tv;

    private String text;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_fragment_page, container, false);
        tv = (TextView) v.findViewById(R.id.text_frag);
        if(text != null) {
            tv.setText(text);
        }
        return v;
    }

    public void setText(String text){
        if(tv != null) {
            tv.setText(text);
            this.text = null;
        } else {
            this.text = text;
        }
    }
}
