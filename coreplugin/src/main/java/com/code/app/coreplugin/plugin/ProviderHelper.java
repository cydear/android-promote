package com.code.app.coreplugin.plugin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProviderHelper
 * @Author: Lary.huang
 * @CreateDate: 2020/9/18 5:03 PM
 * @Version: 1.0
 * @Description: TODO Provider插件化相关操作
 */
public class ProviderHelper {
    /**
     * 解析Apk文件中的 <provider>, 并存储起来
     * 主要是调用PackageParser类的generateProviderInfo方法
     *
     * @param apkFile
     * @return
     */
    public static List<ProviderInfo> parseProviders(File apkFile) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        //获取PackageParser对象
        Class<?> packageParserClazz = Class.forName("android.content.pm.PackageParser");
        Object packageParserObj = packageParserClazz.newInstance();
        //获取parsePackage(File packageFile, int flags)
        Class[] paramsType = {File.class, int.class};
        Object[] paramsValue = {apkFile, PackageManager.GET_PROVIDERS};
        Method parsePackage = packageParserClazz.getDeclaredMethod("parsePackage", paramsType);
        //获取Package对象
        Object packageObj = parsePackage.invoke(packageParserObj, paramsValue);
        //获取android.content.pm.PackageParser$Package : providers
        Field providersField = packageObj.getClass().getDeclaredField("providers");

        //读取Package对象里面的services字段
        //接下来要做的就是根据这个List<Provider> 获取到Provider对应的ProviderInfo
        List providers = (List) providersField.get(packageObj);

        //调用generateProviderInfo 方法, 把PackageParser.Provider转换成ProviderInfo
        //准备generateProviderInfo方法所需要的参数
        Class<?> packageParser$ProviderClazz = Class.forName("android.content.pm.PackageParser$Provider");
        Class<?> packageUserStateClazz = Class.forName("android.content.pm.PackageUserState");
        Object defaultUserState = packageUserStateClazz.newInstance();
        Class<?> userHandleClazz = Class.forName("android.os.UserHandle");
        Method getCallingUserId = userHandleClazz.getDeclaredMethod("getCallingUserId");
        getCallingUserId.setAccessible(true);
        int userId = (int) getCallingUserId.invoke(userHandleClazz.newInstance());
        Class[] p2 = {packageParser$ProviderClazz, int.class, packageUserStateClazz, int.class};

        List<ProviderInfo> ret = new ArrayList<>();
        for (Object provider : providers) {
            Object[] v2 = {provider, 0, defaultUserState, userId};
            Method generateProviderInfo = packageParserClazz.getDeclaredMethod("generateProviderInfo", p2);
            generateProviderInfo.setAccessible(true);
            ProviderInfo info = (ProviderInfo) generateProviderInfo.invoke(packageParserObj, v2);
            ret.add(info);
        }
        return ret;
    }

    /**
     * 在进程内部安装provider, 也就是调用 ActivityThread.installContentProviders方法
     *
     * @param context
     * @param apkFile
     */
    public static void installProviders(Context context, File apkFile) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, NoSuchMethodException, ClassNotFoundException {
        List<ProviderInfo> providerInfos = parseProviders(apkFile);

        for (ProviderInfo providerInfo : providerInfos) {
            providerInfo.applicationInfo.packageName = context.getPackageName();
        }

        Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);
        Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

        Class[] p1 = {Context.class, List.class};
        Object[] v1 = {context, providerInfos};

        Method installContentProviders = activityThreadClazz.getDeclaredMethod("installContentProviders", p1);
        installContentProviders.setAccessible(true);
        installContentProviders.invoke(sCurrentActivityThread, v1);
    }
}
