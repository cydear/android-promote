package com.code.app.coreplugin.data;

import java.io.Serializable;

import dalvik.system.DexClassLoader;

/**
 * @ClassName PluginInfo
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 11:31 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class PluginInfo implements Serializable {
    private DexClassLoader classLoader;
    private String dexPath;

    public DexClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(DexClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getDexPath() {
        return dexPath;
    }

    public void setDexPath(String dexPath) {
        this.dexPath = dexPath;
    }
}
