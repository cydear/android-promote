package com.code.app.coreplugin.resource;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.code.app.coreplugin.R;
import com.code.app.coreplugin.data.PluginInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @ClassName ResourceActivity
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 11:56 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class ResourceActivity extends BaseResourceActivity {
    private Button btnTheme1, btnTheme2, btnTheme3, btnTheme4;
    private TextView tvTitle;
    private ImageView ivHead;

    public static void startToResourceActivity(Context context) {
        Intent intent = new Intent(context, ResourceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        btnTheme1 = findViewById(R.id.btn_theme_1);
        btnTheme2 = findViewById(R.id.btn_theme_2);
        btnTheme3 = findViewById(R.id.btn_theme_3);
        btnTheme4 = findViewById(R.id.btn_theme_4);
        tvTitle = findViewById(R.id.tv_title);
        ivHead = findViewById(R.id.iv_head);

        btnTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme(plugins.get("plugin2.apk"));
            }
        });

        btnTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme(plugins.get("plugin3.apk"));
            }
        });

        btnTheme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeThemeNew(plugins.get("plugin2.apk"));
            }
        });

        btnTheme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeThemeNew(plugins.get("plugin3.apk"));
            }
        });
    }

    /**
     * 更换主题
     *
     * @param pluginInfo
     */
    private void changeTheme(PluginInfo pluginInfo) {
        if (pluginInfo == null) {
            return;
        }
        loadResource(pluginInfo.getDexPath());
        doSomething(pluginInfo.getClassLoader());
    }

    /**
     * do something
     *
     * @param classLoader
     */
    private void doSomething(DexClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        try {
            Class<?> UiUtilClazz = classLoader.loadClass("com.code.app.plugin2.UIUtil");
            Method getTextStringMethod = UiUtilClazz.getDeclaredMethod("getTextString", Context.class);
            String title = (String) getTextStringMethod.invoke(null, this);
            tvTitle.setText(title);
            Method getImageDrawableMethod = UiUtilClazz.getDeclaredMethod("getImageDrawable", Context.class);
            Drawable drawable = (Drawable) getImageDrawableMethod.invoke(null, this);
            ivHead.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "doSomething e =>" + e);
        }
    }

    /**
     * 另一种更换皮肤的方式
     *
     * @param pluginInfo
     * @des TODO 不需要使用UiUtil进行操作
     */
    private void changeThemeNew(PluginInfo pluginInfo) {
        Log.d("plugin.log", "activity classloader =>" + getClassLoader());
        if (pluginInfo == null) {
            return;
        }
        loadResource(pluginInfo.getDexPath());
        //获取classloader
        DexClassLoader classLoader = pluginInfo.getClassLoader();
        //获取资源的R文件
        try {
            Class stringClass = classLoader.loadClass("com.code.app.plugin2.R$string");
            Field helloWordField = stringClass.getDeclaredField("hello_word");
            int titleId = (int) helloWordField.get(null);
            tvTitle.setText(getResources().getText(titleId));
            //drawable
            Class drawableClass = classLoader.loadClass("com.code.app.plugin2.R$mipmap");
            Field drawableField = drawableClass.getDeclaredField("rebort");
            int drawableId = (int) drawableField.get(null);
            ivHead.setImageDrawable(getResources().getDrawable(drawableId));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "changeThemeNew e =>" + e);
        }
    }
}
