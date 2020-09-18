package com.code.reflect.proxy;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.code.app.baselib.BaseApplication;
import com.code.reflect.SubActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName AMSHookHelper
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 10:53 AM
 * @Version: 1.0
 * @Description: TODO ActivityManagerService Hook
 */
public class AMSHookHelper {
    private static final String TAG = "AMSHookHelper.Log";
    public static final String EXTRA_TARGET_INTENT = "extral_target_intent";

    /**
     * Hook ActivityTaskManager
     */
    public static void hookActivityTaskManager() {
        //获取ActivityTaskManager的IActivityTaskManagerSingleton
        try {
            Class<?> activityTaskManagerClazz = Class.forName("android.app.ActivityTaskManager");
            //IActivityTaskManagerSingleton Singleton实例对象
            Field iActivityTaskManagerSingletonField = activityTaskManagerClazz.getDeclaredField("IActivityTaskManagerSingleton");
            iActivityTaskManagerSingletonField.setAccessible(true);
            Object iActivityTaskManagerSingleton = iActivityTaskManagerSingletonField.get(null);
            //获取Singleton中mInstance字段
            Class<?> singletonClazz = Class.forName("android.util.Singleton");
            Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object mInstance = instanceField.get(iActivityTaskManagerSingleton);
            //代理对象
            Class<?> iActivityTaskManager = Class.forName("android.app.IActivityTaskManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityTaskManager},
                    new ActivityTaskManagerHandler(mInstance)
            );
            instanceField.set(iActivityTaskManagerSingleton, proxy);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e =>" + e);
        }
    }

    /**
     * 代理IActivityTaskManager的具体实现类
     */
    private static class ActivityTaskManagerHandler implements InvocationHandler {
        private Object mBase;

        public ActivityTaskManagerHandler(Object base) {
            this.mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "ActivityTaskManagerHandler Hook方法调用");
            if ("startActivity".equals(method.getName())) {
                Log.d(TAG, "hook method =>" + method.getName());

                Intent raw;
                int index = 0;

                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        index = i;
                        break;
                    }
                }

                raw = (Intent) args[index];

                Intent newIntent = new Intent();

                //替身Activity包名
                String subPackage = raw.getComponent().getPackageName();
                //把启动的Activity临时替换成SubActivity
                ComponentName componentName = new ComponentName(subPackage, SubActivity.class.getName());
                newIntent.setComponent(componentName);

                //把原始TargetActivity保存起来
                newIntent.putExtra(EXTRA_TARGET_INTENT, raw);

                //替换掉Intent，达到欺骗的目的
                args[index] = newIntent;

                Log.d(TAG, "hook method =>" + method.getName() + " 注入成功");
                return method.invoke(mBase, args);
            }
            return method.invoke(mBase, args);
        }
    }

    /**
     * hook activity manager service
     */
    public static void hookActivityManagerService() {
        try {
            //获取ActivityManager
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            //获取IActivityManagerSingleton
            Field iActivityManagerSingletonField = activityManagerClazz.getDeclaredField("IActivityManagerSingleton");
            iActivityManagerSingletonField.setAccessible(true);
            //Singleton<IActivityManager> 对象
            Object iActivityManagerSingleton = iActivityManagerSingletonField.get(null);
            //获取Singleton 中mInstance
            Class<?> singletonClazz = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClazz.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //mInstance对象
            Object mInstance = mInstanceField.get(iActivityManagerSingleton);
            //IActivityManager
            Class<?> iActivityManager = Class.forName("android.app.IActivityManager");
            //设置代理对象
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManager},
                    new ActivityManagerHandler(mInstance)
            );
            mInstanceField.set(iActivityManagerSingleton, proxy);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "hookActivityManagerService e =>" + e);
        }
    }

    /**
     * ActivityManager代理
     */
    private static class ActivityManagerHandler implements InvocationHandler {
        private Object mBase;

        public ActivityManagerHandler(Object obj) {
            this.mBase = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "ActivityManagerHandler Hook方法调用");
            if ("startService".equals(method.getName())) {
                Log.d(TAG, "ActivityManagerHandler method =>" + method.getName() + " 调用了");

                Intent raw;
                int index = 0;

                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        index = i;
                        break;
                    }
                }
                raw = (Intent) args[index];

                //启动的插件service的包名
                String pluginServiceName = raw.getComponent().getClassName();
                Log.d(TAG, "ActivityManagerHandler pluginServiceName =>" + pluginServiceName);
                String subServiceName = BaseApplication.servicePlugins.get(pluginServiceName);
                if (!TextUtils.isEmpty(subServiceName)) {
                    ComponentName componentName = new ComponentName("com.code.app", subServiceName);
                    //创建替身Intent
                    Intent newIntent = new Intent();
                    newIntent.setComponent(componentName);
                    //替换掉Intent,达到欺骗目的
                    args[index] = newIntent;
                }
            }
            return method.invoke(mBase, args);
        }
    }
}
