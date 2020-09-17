package com.code.app.coreplugin.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.code.app.coreplugin.data.PluginInfo;
import com.code.app.coreplugin.util.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * @ClassName BaseResourceActivity
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 11:28 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class BaseResourceActivity extends AppCompatActivity {
    /**
     * 集合插件资源的新AssetManager
     */
    protected AssetManager mAssetManager;
    /**
     * 资源Resources
     */
    protected Resources mResources;
    /**
     * 主题
     */
    protected Resources.Theme mTheme;

    protected HashMap<String, PluginInfo> plugins = new HashMap<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            AssetManager assetManager = getAssets();
            String[] paths = assetManager.list("");
            Log.d("plugin.log", "paths =>" + Arrays.toString(paths));
            for (String path : paths) {
                if (TextUtils.isEmpty(path)) {
                    continue;
                }
                if (path.endsWith(".apk")) {
                    String apkName = path;
                    Utils.extractAssets(this, apkName);
                    File file = getFileStreamPath(apkName);
                    PluginInfo info = new PluginInfo();
                    info.setDexPath(file.getAbsolutePath());
                    File dexOutputDir = getDir("dex", 0);
                    DexClassLoader loader = new DexClassLoader(info.getDexPath(),
                            dexOutputDir.getAbsolutePath(), null,
                            getClassLoader());
                    info.setClassLoader(loader);
                    plugins.put(apkName, info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "BaseResourceActivity attachBaseContext e =>" + e);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 加载插件资源
     *
     * @param dexPath
     */
    protected void loadResource(String dexPath) {
        //使用插件资源替换现有的资源
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "change face e =>" + e);
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager,
                superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }
}
