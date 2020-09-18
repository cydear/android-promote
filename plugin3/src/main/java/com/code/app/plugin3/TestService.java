package com.code.app.plugin3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @ClassName TestService
 * @Author: Lary.huang
 * @CreateDate: 2020/9/17 4:57 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class TestService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("plugin.log", "TestService 启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
