package com.yyp.mysample.mytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.R;
import com.yyp.mysample.datepicker.util.PopBirthHelper;
import com.yyp.mysample.mytest.frag.MyFragmentActivity;
import com.yyp.mysample.pickerview.TimePopupWindow;
import com.yyp.mysample.utils.DateUtils;
import com.yyp.mysample.views.YnViewPagerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fso91 on 2016/9/22.
 */

public class TestActivity extends Activity implements View.OnClickListener{
    private Button clipChildButton;
    private Button fragmentButton;
    private Button recyclerContentButton;
    private Button datePickerButton;
    private Button datePicker3DButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        clipChildButton = (Button) findViewById(R.id.clip_child);
        clipChildButton.setOnClickListener(this);

        fragmentButton = (Button) findViewById(R.id.fragment_test);
        fragmentButton.setOnClickListener(this);

        recyclerContentButton = (Button) findViewById(R.id.recycler_wrap);
        recyclerContentButton.setOnClickListener(this);

        datePickerButton = (Button) findViewById(R.id.date_picker);
        datePickerButton.setOnClickListener(this);

        datePicker3DButton = (Button) findViewById(R.id.date_picker_3d);
        datePicker3DButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.clip_child:
                intent = new Intent(this, ClipChildrenViewPageActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_test:
                intent = new Intent(this, MyFragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.recycler_wrap:
                intent = new Intent(this, RecyclerWrapcontentActivity.class);
                startActivity(intent);
                break;
            case R.id.date_picker:
                DateUtils.showDateTimePicker(this, datePickerButton, "", new TimePopupWindow.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        Toast.makeText(TestActivity.this, "date--->" + DateUtils.noSecondFormat.format(date), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.date_picker_3d:
                new PopBirthHelper(this).show(datePicker3DButton);
                break;
        }
    }

}
