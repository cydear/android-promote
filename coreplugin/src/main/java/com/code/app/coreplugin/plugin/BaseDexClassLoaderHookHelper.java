package com.code.app.coreplugin.plugin;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import com.code.app.coreplugin.util.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

/**
 * 由于应用程序使用的ClassLoader为PathClassLoader
 * 最终继承自 BaseDexClassLoader
 * 查看源码得知,这个BaseDexClassLoader加载代码根据一个叫做
 * dexElements的数组进行, 因此我们把包含代码的dex文件插入这个数组
 * 系统的classLoader就能帮助我们找到这个类
 * <p>
 * 这个类用来进行对于BaseDexClassLoader的Hook
 *
 * @ClassName BaseDexClassLoaderHookHelper
 * @Author: Lary.huang
 * @CreateDate: 2020/9/17 2:29 PM
 * @Version: 1.0
 * @Description: TODO BaseDexClassLoaderHook
 */
public class BaseDexClassLoaderHookHelper {
    public static void init(Application application) {
        try {
            AssetManager assetManager = application.getAssets();
            String[] paths = assetManager.list("");
            Log.d("plugin.log", "paths =>" + Arrays.toString(paths));
            for (String path : paths) {
                //将apk拷贝至/data/user/0/x.x.x/files目录下
                if (path.endsWith(".apk")) {
                    String apkName = path;
                    Utils.extractAssets(application.getBaseContext(), apkName);
                    //copy完成，将插件Apk加载至dexElements
                    File apkFile = application.getFileStreamPath(apkName);
                    File outDexFile = application.getDir("dex", 0);
                    patchClassLoader(application.getClassLoader(), apkFile, outDexFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("plugin.log", "BaseDexClassLoaderHookHelper init e =>" + e);
        }
    }

    public static void patchClassLoader(ClassLoader classLoader, File apkFile, File optDexFile) throws NoSuchFieldException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        //获取BaseDexClassLoader : pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //获取DexPathList ：dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object[] dexElements = (Object[]) dexElementsField.get(pathList);
        //获取Element类型
        Class<?> elementClazz = dexElements.getClass().getComponentType();
        //构建一个新数组用来替换原来的数组
        Object[] newElements = (Object[]) Array.newInstance(elementClazz, dexElements.length + 1);
        //构建插件Element(File file, boolean isDirectory, File zip, DexFile dexFile)
        Class[] paramsType = {File.class, boolean.class, File.class, DexFile.class};
        Object[] paramsValue = {apkFile, false, apkFile, DexFile.loadDex(
                apkFile.getCanonicalPath(),
                optDexFile.getAbsolutePath(),
                0
        )};
        Constructor elementCtr = elementClazz.getDeclaredConstructor(paramsType);
        elementCtr.setAccessible(true);
        Object newElement = elementCtr.newInstance(paramsValue);
        //将原始的dexElements复制近新的Elements数组
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        //插件的element
        Object[] addNewElements = new Object[]{newElement};
        System.arraycopy(addNewElements, 0, newElements, dexElements.length, addNewElements.length);
        //使用新的dexElemens替换旧的dexElements的值
        dexElementsField.set(pathList, newElements);
        Log.d("plugin.log", "插件添加至dexElements成功");
    }
}
