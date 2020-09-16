package com.code.app;

import com.code.app.baselib.BaseApplication;
import com.code.app.coreplugin.Utils;

/**
 * @ClassName App
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 7:35 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.extractAssets(this, "plugin1.apk");
    }
}
