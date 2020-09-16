package com.code.reflect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.code.reflect.proxy.AMSHookHelper;
import com.code.reflect.proxy.HookHelper;

/**
 * @ClassName ReflectMainActivity
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 3:49 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class ReflectMainActivity extends AppCompatActivity {
    public static void startToReflectMainActivity(Context context) {
        Intent intent = new Intent(context, ReflectMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);

        findViewById(R.id.btn_hook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HookHelper.hookActivityManager();
            }
        });

        findViewById(R.id.btn_start_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReflectMainActivity.this, SeconActivity.class);
                ReflectMainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_hook_pm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HookHelper.hookPackageManager();
            }
        });

        findViewById(R.id.btn_hook_instrumentation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HookHelper.hookInstrumentation(ReflectMainActivity.this);
            }
        });

        findViewById(R.id.btn_hook_taskmanager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMSHookHelper.hookActivityTaskManager();
            }
        });

        findViewById(R.id.btn_hook_mh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HookHelper.hookHandlerCallback();
            }
        });

        findViewById(R.id.btn_hook_instr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HookHelper.attachContext();
            }
        });

        findViewById(R.id.btn_hook_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReflectMainActivity.this, TargetActivity.class);
                startActivity(intent);
            }
        });
    }
}
