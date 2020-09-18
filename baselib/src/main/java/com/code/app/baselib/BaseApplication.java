package com.code.app.baselib;

import android.app.Application;

import java.util.HashMap;

/**
 * @ClassName App
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 6:38 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class BaseApplication extends Application {
    private static Application mInstance;

    public final static HashMap<String, String> servicePlugins = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        addServicePlugin();
    }

    private void addServicePlugin() {
        servicePlugins.put("com.code.app.plugin3.TestService", "com.code.reflect.service.StubService1");
    }

    public static Application get() {
        return mInstance;
    }
}
