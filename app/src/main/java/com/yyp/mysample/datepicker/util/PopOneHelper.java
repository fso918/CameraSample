package com.yyp.mysample.datepicker.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.yyp.mysample.R;
import com.yyp.mysample.datepicker.LoopListener;
import com.yyp.mysample.datepicker.LoopView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by baiyuliang on 2015-11-24.
 */
public class PopOneHelper {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> listItem;
    private String _item;

    public PopOneHelper(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.picker_one, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initPop();
    }


    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView = (LoopView) view.findViewById(R.id.loopView);
        if (null == listItem) {
            listItem = new ArrayList<>();
        }
        loopView.setList(listItem);
        loopView.setNotLoop();
        loopView.setCurrentItem(0);
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                _item = listItem.get(item);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickOkListener.onClickOk(_item);
                    }
                }, 500);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        if (null == listItem || listItem.size() <= 0) {
            Toast.makeText(context, "请初始化您的数据", Toast.LENGTH_LONG).show();
            return;
        }
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    public void setListItem(List<String> listItem) {
        this.listItem = listItem;
        initView();
    }

    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        pop.setOnDismissListener(onDismissListener);
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        public void onClickOk(String str);
    }

}
