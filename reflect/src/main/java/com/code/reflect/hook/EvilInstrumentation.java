package com.code.reflect.hook;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.code.reflect.proxy.AMSHookHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName EvilInstrumentation
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 10:04 AM
 * @Version: 1.0
 * @Description: TODO Hook Activity中Instrumentation
 */
public class EvilInstrumentation extends Instrumentation {
    private static final String TAG = "Hook.Log";

    /**
     * 保存ActivityThread中的原始对象
     */
    private Instrumentation mBase;

    public EvilInstrumentation(Instrumentation base) {
        this.mBase = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Log.d(TAG, "Instrumentation 的 execStartActivity 方法被Hook了，现在执行的是Hook方法哈");

        try {
            Class[] paramsType = {Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class};
            Method execStartActivityMethod = mBase.getClass().getDeclaredMethod("execStartActivity", paramsType);
            execStartActivityMethod.setAccessible(true);
            Object[] params = {who, contextThread, token, target, intent, requestCode, options};
            return (ActivityResult) execStartActivityMethod.invoke(mBase, params);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e=>" + e);
        }
        return null;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Log.d(TAG, "newActivity hook 方法执行了");

        //把替身恢复成真身
        Intent rawIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);

        if (rawIntent == null) {
            return mBase.newActivity(cl, className, intent);
        }
        String newClassName = rawIntent.getComponent().getClassName();
        return mBase.newActivity(cl, newClassName, rawIntent);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        Log.d(TAG, "callActivityOnCreate hook 方法执行了");
        Class<?>[] paramsType = {Activity.class, Bundle.class};
        Object[] paramsValue = {activity, icicle};
        try {
            Method method = mBase.getClass().getDeclaredMethod("callActivityOnCreate", paramsType);
            method.invoke(mBase, paramsValue);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e =>" + e);
        }
    }
}
