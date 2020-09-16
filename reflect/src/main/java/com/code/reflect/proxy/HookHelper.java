package com.code.reflect.proxy;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Handler;
import android.util.Log;

import com.code.reflect.hook.EvilInstrumentation;
import com.code.reflect.hook.MyHandlerCallback;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @ClassName HookHelper
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 3:54 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class HookHelper {
    private final static String TAG = "HookHelper.Log";

    public static void hookActivityManager() {
        try {
            Class clazz = Class.forName("android.app.ActivityTaskManager");
            Field field = clazz.getDeclaredField("IActivityTaskManagerSingleton");
            field.setAccessible(true);
            Object obj = field.get(null);
            Log.d(TAG, "IActivityTaskManagerSingleton=>" + obj);
            Class singletonClazz = Class.forName("android.util.Singleton");
            Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object activityTaskManager = instanceField.get(obj);
            Log.d(TAG, "activityTaskManager=>" + activityTaskManager);
            //hook
            Class taskManagerClazz = Class.forName("android.app.IActivityTaskManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{taskManagerClazz},
                    new HookHandler(activityTaskManager)
            );
            instanceField.set(obj, proxy);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e=>" + e.getMessage());
        }
    }

    public static void hookPackageManager() {
        try {
            //获取sCurrentActivityThread对象的值
            Class activityThreadClazz = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            Log.d(TAG, "sCurrentActivityThread=>" + sCurrentActivityThread);
            //获取PackageManager
            Field sPackagerManagerField = activityThreadClazz.getDeclaredField("sPackageManager");
            sPackagerManagerField.setAccessible(true);
            Object sPackageManagerObj = sPackagerManagerField.get(sCurrentActivityThread);
            Log.d(TAG, "sPackageManagerObj=>" + sPackageManagerObj);
            //代理
            Class<?> iPackageManager = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iPackageManager},
                    new HookHandler(sPackageManagerObj)
            );
            sPackagerManagerField.set(sCurrentActivityThread, proxy);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e=>" + e.toString());
        }
    }

    private static class HookHandler implements InvocationHandler {
        private Object realObj;

        public HookHandler(Object obj) {
            this.realObj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "hook success");
            Log.d(TAG, "method=>" + method.getName() + " , called with args=>" + Arrays.toString(args));
            return method.invoke(realObj, args);
        }
    }

    /**
     * hook instrumentation
     *
     * @param activityObj
     */
    public static void hookInstrumentation(Object activityObj) {
        if (activityObj == null) return;
        try {
            Field mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            //Instrumentation对象
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activityObj);
            //设置自定义的Instrumentation Hook
            EvilInstrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
            mInstrumentationField.set(activityObj, evilInstrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hook ActivityThread mH Callback
     */
    public static void hookHandlerCallback() {
        try {
            //ActivityThread
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            //获取ActivityThread
            Field sCurrentActivityThreadField = clazz.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            //ActivityThread Obj
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            //获取mH对象
            Field mHField = clazz.getDeclaredField("mH");
            mHField.setAccessible(true);
            //mH Obj
            Object mH = mHField.get(sCurrentActivityThread);
            //获取Handler的Callback
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            //设置mCallback的值
            mCallbackField.set(mH, new MyHandlerCallback((android.os.Handler) mH));
            Log.d("Handler.Log", "ActivityThread mH mCallback 注入成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Handler.Log", "e =>" + e);
        }
    }

    /**
     * attachContext
     */
    public static void attachContext() {
        try {
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            //sCurrentActivityThread
            Field sCurrentActivityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            //mInstrumentation Obj
            Field instrumentationField = activityThreadClazz.getDeclaredField("mInstrumentation");
            instrumentationField.setAccessible(true);
            Object mInstrumentation = instrumentationField.get(sCurrentActivityThread);
            //静态代理
            EvilInstrumentation evilInstrumentation = new EvilInstrumentation((Instrumentation) mInstrumentation);
            instrumentationField.set(sCurrentActivityThread, evilInstrumentation);
            Log.d(TAG, "EvilInstrumentation Hook 完成");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e =>" + e);
        }
    }
}
