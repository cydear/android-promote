package com.code.app.baselib;

import android.app.Application;

/**
 * @ClassName App
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 6:38 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class BaseApplication extends Application {
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application get() {
        return mInstance;
    }
}
