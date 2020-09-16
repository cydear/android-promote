package com.code.app.coreplugin.data;

import android.content.pm.PackageInfo;

import java.io.Serializable;

/**
 * @ClassName PluginItem
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 3:41 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class PluginItem implements Serializable {
    private PackageInfo packageInfo;
    private String pluginPath;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }
}
