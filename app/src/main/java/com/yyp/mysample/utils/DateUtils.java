package com.yyp.mysample.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yyp.mysample.pickerview.DateTimePickerDialog;
import com.yyp.mysample.pickerview.TimePopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fso91 on 2017/2/15.
 */

public class DateUtils {
    /**
     * yyyy-MM-dd HH:mm
     */
    public static SimpleDateFormat noSecondFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static void showDateTimePicker(Context context, View view, String date, TimePopupWindow.OnTimeSelectListener listener){
        Calendar calendar = Calendar.getInstance();
        if(!TextUtils.isEmpty(date)){
            try {
                calendar.setTime(noSecondFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                long now = System.currentTimeMillis() + 60*60*1000/2;
                calendar.setTimeInMillis(now);
            }
        }else {
            long now = System.currentTimeMillis() + 60*60*1000/2;
            calendar.setTimeInMillis(now);
        }

        DateTimePickerDialog dialog = new DateTimePickerDialog(context,listener,calendar.getTime
                (), TimePopupWindow.Type.YEAR_MONTH_DAY);

        dialog.show(view);
    }
}
