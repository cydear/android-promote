package com.code.app.coreplugin.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName ReeiverHelper
 * @Author: Lary.huang
 * @CreateDate: 2020/9/18 2:53 PM
 * @Version: 1.0
 * @Description: TODO 解析插件Apk文件中的 <receiver>, 并存储起来
 */
public class ReceiverHelper {
    /**
     * 解析插件Apk文件中的 <receiver>, 并存储起来
     *
     * @param context
     * @param apkFile
     */
    public static void preLoadReceiver(Context context, File apkFile) {
        //首先调用ParsePackage获取到apk对象对应的Package
        try {
            Class<?> parsePackageClazz = Class.forName("android.content.pm.PackageParser");
            Object parsePackageObj = parsePackageClazz.newInstance();
            //获取parsePackage(File packageFile, int flags)
            Class[] paramsType = {File.class, int.class};
            Object[] paramsValue = {apkFile, PackageManager.GET_RECEIVERS};
            Method parsePackage = parsePackageClazz.getDeclaredMethod("parsePackage", paramsType);
            parsePackage.setAccessible(true);
            Object packageObj = parsePackage.invoke(parsePackageObj, paramsValue);

            //获取包名
            Field packageNameField = packageObj.getClass().getDeclaredField("packageName");
            String packageName = (String) packageNameField.get(packageObj);
            //获取Package中对应的receivers
            Field receiversField = packageObj.getClass().getDeclaredField("receivers");
            List receivers = (List) receiversField.get(packageObj);

            Class<?> componentClazz = Class.forName("android.content.pm.PackageParser$Component");

            for (Object receiver : receivers) {
                Field metaDataField = componentClazz.getDeclaredField("metaData");
                Bundle metadata = (Bundle) metaDataField.get(receiver);

                String oldAction = metadata.getString("oldAction");

                //解析出receiver以及对应的intentFilter
                Field intentField = componentClazz.getDeclaredField("intents");
                List<? extends IntentFilter> filters = (List<? extends IntentFilter>) intentField.get(receiver);

                //把解析出来的每一个静态的Receiver都注册为动态
                for (IntentFilter filter : filters) {
                    Field infoField = receiver.getClass().getDeclaredField("info");
                    ActivityInfo receiverInfo = (ActivityInfo) infoField.get(receiver);
                    BroadcastReceiver broadcastReceiver = (BroadcastReceiver) Class.forName(receiverInfo.name).newInstance();
                    context.registerReceiver(broadcastReceiver, filter);

                    String newAction = filter.getAction(0);
                    ReceiverManager.pluginReceiverMappings.put(oldAction, newAction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("service.log", "preLoadReceiver e =>" + e);
        }
    }
}
