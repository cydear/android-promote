package com.code.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.code.app.coreplugin.PluginActivity;
import com.code.app.coreplugin.plugin.BaseDexClassLoaderHookHelper;
import com.code.reflect.ReflectMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_reflect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflectMainActivity.startToReflectMainActivity(MainActivity.this);
            }
        });
        findViewById(R.id.btn_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginActivity.startToPluginActivity(MainActivity.this);
            }
        });
    }
}