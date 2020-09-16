package com.code.plugin.impl;

import android.app.Application;
import android.util.Log;

/**
 * @ClassName CorePluginApp
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 8:13 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class CorePluginApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("plugin.log", "CorePluginApp onCreate init");
    }
}
