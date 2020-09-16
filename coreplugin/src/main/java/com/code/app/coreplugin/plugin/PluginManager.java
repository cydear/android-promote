package com.code.app.coreplugin.plugin;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.code.app.coreplugin.data.PluginItem;
import com.code.app.coreplugin.loader.ZeusClassLoader;
import com.code.app.coreplugin.util.DLUtils;
import com.code.app.coreplugin.util.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * @ClassName PluginManager
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 3:32 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class PluginManager {
    /**
     * 插件集合
     */
    public final static List<PluginItem> plugins = new ArrayList<>();

    /**
     * 正在使用的Resource
     */
    public static volatile Resources mNowResources;

    /**
     * 原始Application中的BaseContext
     */
    public static volatile Context mBaseContext;

    /**
     * ContextImpl中的LoadApk对象mPackageInfo
     */
    private static Object mPackageInfo = null;

    /**
     * 系统原始的ClassLoader
     */
    public static volatile ClassLoader mNowClassLoader = null;

    /**
     * 系统原始的ClassLoader
     */
    public static volatile ClassLoader mBaseClassLoader = null;

    /**
     * 初始化plugin manager
     *
     * @param application
     */
    public static void init(Application application) {
        //初始化一些成员变量和加载已安装的插件
        try {
            Class<?> clazz = Class.forName("android.app.ContextImpl");
            Field packageInfoField = clazz.getDeclaredField("mPackageInfo");
            packageInfoField.setAccessible(true);
            mPackageInfo = packageInfoField.get(application.getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBaseContext = application.getBaseContext();
        mNowResources = application.getResources();

        mBaseClassLoader = application.getClassLoader();
        mNowClassLoader = application.getClassLoader();

        try {
            AssetManager assetManager = application.getAssets();
            String[] paths = assetManager.list("");
            Log.d("plugin.log", "paths =>" + Arrays.toString(paths));

            ArrayList<String> pluginPaths = new ArrayList<>();
            for (String path : paths) {
                //将apk拷贝至/data/user/0/x.x.x/files目录下
                if (path.endsWith(".apk")) {
                    String apkName = path;
                    Utils.extractAssets(mBaseContext, apkName);
                    PluginItem item = generatePluginItem(apkName);
                    plugins.add(item);

                    pluginPaths.add(item.getPluginPath());
                }
            }
            reloadInstalledPluginResources(pluginPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ZeusClassLoader classLoader = new ZeusClassLoader(mBaseContext.getPackageCodePath(), mBaseClassLoader);

        File dexOutputDir = mBaseContext.getDir("dex", 0);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        for (PluginItem item : plugins) {
            DexClassLoader dexClassLoader = new DexClassLoader(item.getPluginPath(),
                    dexOutputPath, null, mBaseClassLoader);
            classLoader.addPluginClassLoader(dexClassLoader);
        }
        try {
            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
            Field classLoaderField = loadApkClazz.getDeclaredField("mClassLoader");
            classLoaderField.setAccessible(true);
            classLoaderField.set(mPackageInfo, classLoader);

            Thread.currentThread().setContextClassLoader(classLoader);
            mNowClassLoader = classLoader;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载插件资源文件
     *
     * @param pluginPaths
     */
    private static void reloadInstalledPluginResources(ArrayList<String> pluginPaths) {
        if (pluginPaths == null || pluginPaths.isEmpty()) {
            return;
        }
        try {
            //构建新的AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射获取addAssetPath
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            //调用addAssetPath方法加载插件资源
            addAssetPath.invoke(assetManager, mBaseContext.getPackageResourcePath());
            //添加插件资源
            for (String pluginPath : pluginPaths) {
                addAssetPath.invoke(assetManager, pluginPath);
            }
            Resources newResources = new Resources(assetManager,
                    mBaseContext.getResources().getDisplayMetrics(),
                    mBaseContext.getResources().getConfiguration()
            );
            Class<?> clazz = Class.forName("android.app.ContextImpl");
            Field mResourcesField = clazz.getDeclaredField("mResources");
            mResourcesField.setAccessible(true);
            mResourcesField.set(mBaseContext, newResources);
            //这是最主要的需要替换的，如果不支持插件运行时更新，只保留这一个就可以
            Class<?> loadApkClazz = Class.forName("android.app.LoadedApk");
            Field loadApkResourceField = loadApkClazz.getDeclaredField("mResources");
            loadApkResourceField.setAccessible(true);
            loadApkResourceField.set(mPackageInfo, newResources);

            mNowResources = newResources;
            //需要清理theme对象，否则通过inflate方式加载资源会报错
            //如果Activity动态加载插件，则需要把activity的mTheme对象也设置为空
            Field mThemeField = clazz.getDeclaredField("mTheme");
            mThemeField.setAccessible(true);
            mThemeField.set(mBaseContext, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "e =>" + e);
        }
    }

    /**
     * 创建PluginItem
     *
     * @param apkName
     * @return
     */
    private static PluginItem generatePluginItem(String apkName) {
        PluginItem item = new PluginItem();
        File file = mBaseContext.getFileStreamPath(apkName);
        item.setPluginPath(file.getAbsolutePath());
        item.setPackageInfo(DLUtils.getPackageInfo(mBaseContext, item.getPluginPath()));
        return item;
    }
}
