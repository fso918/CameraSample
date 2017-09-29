package com.yyp.mysample.pickerview;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import java.util.Date;

/**
 * @author chenqiang.
 * @Date 2015/9/29.
 * @Todo 时间选择封装类
 */
public class DateTimePickerDialog {

    private Context context;
    private TimePopupWindow timePopupWindow;
    private TimePopupWindow.OnTimeSelectListener onTimeSelectListener;
    /* 默认显示的时间 */
    private Date oldDate;

    private TimePopupWindow.Type type;

    public DateTimePickerDialog(Context context, TimePopupWindow.OnTimeSelectListener onTimeSelectListener, Date oldDate, TimePopupWindow.Type type) {
        this(context,onTimeSelectListener,oldDate,type, TimePopupWindow.Model.NONE);
    }

    public DateTimePickerDialog(Context context, TimePopupWindow.OnTimeSelectListener
            onTimeSelectListener, Date oldDate, TimePopupWindow.Type type,TimePopupWindow.Model
            model) {
        this.context = context;
        this.onTimeSelectListener = onTimeSelectListener;
        this.oldDate = oldDate;

        this.timePopupWindow = new TimePopupWindow(context,type,model);
    }

    public DateTimePickerDialog(Context context, TimePopupWindow.OnTimeSelectListener onTimeSelectListener) {
        this(context,onTimeSelectListener,null, TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    public void show(View view){
        if(context != null && context instanceof Activity){
//            Util.hideSoftInput((Activity) context);
        }
        if(timePopupWindow == null)return;
        if(timePopupWindow.isShowing()){
            timePopupWindow.dismiss();
        }else {
            this.timePopupWindow.setOnTimeSelectListener(onTimeSelectListener);
            timePopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0,oldDate);
        }
    }

}
