package com.code.app.coreplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.code.app.baselib.BaseApplication;
import com.code.app.baselib.ICallback;
import com.code.app.baselib.IMessage;
import com.code.app.baselib.IPerson;
import com.code.app.coreplugin.data.PluginItem;
import com.code.app.coreplugin.plugin.PluginManager;
import com.code.app.coreplugin.resource.ResourceActivity;

import java.io.File;

import dalvik.system.DexClassLoader;

public class PluginActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "plugin.log";
    private final static String EXTRACT_FILE_NAME = "plugin1.apk";
    private Button btnLoadPlugin, btnApplication, btnFace;

    public static void startToPluginActivity(Context context) {
        Intent intent = new Intent(context, PluginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        btnLoadPlugin = findViewById(R.id.btn_load);
        btnApplication = findViewById(R.id.btn_application);
        btnFace = findViewById(R.id.btn_face);

        btnLoadPlugin.setOnClickListener(this);
        btnApplication.setOnClickListener(this);
        btnFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int _id = v.getId();
        if (_id == R.id.btn_load) {
            loadPlugin();
        } else if (_id == R.id.btn_application) {
            applicationPlugin();
            //PluginManager.init(BaseApplication.get());
        } else if (_id == R.id.btn_face) {
            ResourceActivity.startToResourceActivity(this);
        }
    }

    /**
     * application插件化
     */
    private void applicationPlugin() {
        //PluginManager.init(BaseApplication.get());
        for (PluginItem item : PluginManager.plugins) {
            if (item.getPackageInfo() == null || item.getPackageInfo().applicationInfo == null) {
                continue;
            }
            String className = item.getPackageInfo().applicationInfo.className;
            try {
                Class clazz = PluginManager.mNowClassLoader.loadClass(className);
                Application application = (Application) clazz.newInstance();
                if (application != null) {
                    application.onCreate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("plugin.log", "application onCreate e =>" + e);
            }
        }
    }

    /**
     * 加载插件
     */
    private void loadPlugin() {
        loadSimplePlugin1();
    }

    private void loadSimplePlugin1() {
        File extractFile = getFileStreamPath(EXTRACT_FILE_NAME);
        Log.d(TAG, "插件文件路径 =>" + extractFile.getPath());
        if (!extractFile.exists()) {
            Log.d(TAG, "插件文件: " + EXTRACT_FILE_NAME + " 不存在");
            return;
        }
        String dexPath = extractFile.getPath();
        File fileRelease = getDir("dex", 0);
        Log.d(TAG, "dexPath =>" + dexPath);
        ClassLoader classLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());
        Class mLoadClassBean = null;
        try {
            mLoadClassBean = classLoader.loadClass("com.code.plugin.impl.UserInfo");
            Object obj = mLoadClassBean.newInstance();
            IPerson bean = (IPerson) obj;
            bean.setName("通过加载插件设置Name => tom");
            Log.d(TAG, "name =>" + bean.getName());
            IMessage message = (IMessage) obj;
            message.register(new ICallback() {
                @Override
                public void sendMessage(String result) {
                    Log.d(TAG, result);
                }
            });
            message.sendMessage("我在学习插件化哈");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "加载插件异常 e=>" + e);
        }
    }
}