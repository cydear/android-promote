package com.code.reflect.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @ClassName StubService1
 * @Author: Lary.huang
 * @CreateDate: 2020/9/17 5:02 PM
 * @Version: 1.0
 * @Description: TODO 占位的service
 */
public class StubService1 extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("plugin.log", "StubService1 启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
