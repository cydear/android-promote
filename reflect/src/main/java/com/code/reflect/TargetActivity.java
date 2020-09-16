package com.code.reflect;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @ClassName TargetActivity
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 5:06 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class TargetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
    }
}
