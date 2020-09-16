package com.code.reflect.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.code.reflect.proxy.AMSHookHelper;

import java.lang.reflect.Field;

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
        }
        mHandler.handleMessage(msg);
        return true;
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
