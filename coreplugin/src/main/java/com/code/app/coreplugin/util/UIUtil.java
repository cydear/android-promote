package com.code.app.coreplugin.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.code.app.coreplugin.R;

/**
 * @ClassName UIUtil
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 11:19 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class UIUtil {
    public static String getTextString(Context context) {
        return context.getResources().getString(R.string.hello_word);
    }

    public static Drawable getImageDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.rebort);
    }

    public static View getLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.activity_main, null);
    }
}
