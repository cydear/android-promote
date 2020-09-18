package com.code.reflect.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.code.app.baselib.BaseApplication;
import com.code.reflect.proxy.AMSHookHelper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName MyHandlerCallback
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 3:04 PM
 * @Version: 1.0
 * @Description: TODO Hook ActivityThread mH
 */
public class MyHandlerCallback implements Handler.Callback {
    private Handler mHandler;

    public MyHandlerCallback(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.d("Handler.Log", "msg.what =>" + msg.what + " , msg.obj =>" + msg.obj);
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
            case 114:
                handleCreateService(msg.obj);
                break;
            case 115:
                //handleServiceArgs(msg.obj);
                break;
        }
        mHandler.handleMessage(msg);
        return true;
    }

    /**
     * 替换ServiceArgsData中的Intent
     *
     * @param obj
     */
    private void handleServiceArgs(Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Field argsField = obj.getClass().getDeclaredField("args");
            argsField.setAccessible(true);
            Intent args = (Intent) argsField.get(obj);
            String stubServiceName = args.getComponent().getClassName();
            String realServiceName = null;
            for (Map.Entry<String, String> entry : BaseApplication.servicePlugins.entrySet()) {
                if (entry.getValue().equals(stubServiceName)) {
                    realServiceName = entry.getKey();
                    break;
                }
            }
            if (!TextUtils.isEmpty(realServiceName)) {
                String realServicePackage = realServiceName.substring(0, realServiceName.lastIndexOf("."));
                ComponentName componentName = new ComponentName(realServicePackage, realServiceName);
                args.setComponent(componentName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换service为插件service
     *
     * @param obj
     */
    private void handleCreateService(Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Field serviceInfoField = obj.getClass().getDeclaredField("info");
            serviceInfoField.setAccessible(true);
            ServiceInfo info = (ServiceInfo) serviceInfoField.get(obj);
            String realServiceName = null;
            for (Map.Entry<String, String> entry : BaseApplication.servicePlugins.entrySet()) {
                if (entry.getValue().equals(info.name)) {
                    realServiceName = entry.getKey();
                    break;
                }
            }
            if (!TextUtils.isEmpty(realServiceName)) {
                info.name = realServiceName;
            }
            Log.d("Handler.Log", "handleCreateService realServiceName =>" + realServiceName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Handler.Log", "handleCreateService e =>" + e);
        }
    }

    private void handleLaunchActivity(Message msg) {
        Object obj = msg.obj;
        Log.d("Handler.Log", "msg =>" + obj.toString());

//        try {
//            //替身回复成真身
//            Field intentField = obj.getClass().getField("intent");
//            Intent intent = (Intent) intentField.get(obj);
//
//            Intent targetIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
//            intent.setComponent(targetIntent.getComponent());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
