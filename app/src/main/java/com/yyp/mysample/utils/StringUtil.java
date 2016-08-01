package com.yyp.mysample.utils;

/**
 * Created by fso91 on 2016/8/1.
 */
public class StringUtil {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(CharSequence value) {
        if (value != null && !"".equalsIgnoreCase(value.toString().trim()) && !"null".equalsIgnoreCase(value.toString().trim())) {
            return false;
        } else {
            return true;
        }
    }
}
