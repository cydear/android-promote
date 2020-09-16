package com.code.app.coreplugin.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

/**
 * @ClassName DLUtils
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 6:36 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class DLUtils {
    /**
     * get PackageInfo by apk
     *
     * @param context
     * @param apkFilePath
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String apkFilePath) {
        if (context == null || TextUtils.isEmpty(apkFilePath)) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }

    /**
     * get App Icon by apk
     *
     * @param context
     * @param apkFilePath
     * @return
     */
    public static Drawable getAppIcon(Context context, String apkFilePath) {
        if (context == null || TextUtils.isEmpty(apkFilePath)) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        Drawable appIcon = null;
        PackageInfo pkgInfo = getPackageInfo(context, apkFilePath);
        if (pkgInfo == null) {
            return null;
        }
        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
        }
        return appInfo.loadIcon(pm);
    }

    /**
     * get App Label by apk
     *
     * @param context
     * @param apkFilePath
     * @return
     */
    public static CharSequence getAppLabel(Context context, String apkFilePath) {
        if (context == null || TextUtils.isEmpty(apkFilePath)) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        CharSequence appLabel = null;
        PackageInfo pkgInfo = getPackageInfo(context, apkFilePath);
        if (pkgInfo == null) {
            return null;
        }
        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
        }
        return appInfo.loadLabel(pm);
    }
}
