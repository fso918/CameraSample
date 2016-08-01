package com.yyp.mysample.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by fso91 on 2016/8/1.
 */
public class DisplayUtils {
    /**
     * dip转换px
     * @param dip
     * @return
     */
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取屏幕宽度
     * @return 屏幕宽度
     */
    public static int getScreenWith(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
