package com.code.app.coreplugin.loader;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @ClassName ZeusClassLoader
 * @Author: Lary.huang
 * @CreateDate: 2020/9/16 7:05 PM
 * @Version: 1.0
 * @Description: TODO 这是一个空ClassLoader，是个ClassLoader容器
 */
public class ZeusClassLoader extends PathClassLoader {
    private List<DexClassLoader> mClassLoaderList = null;

    public ZeusClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);

        mClassLoaderList = new ArrayList<>();
    }

    /**
     * 添加一个插件到当前的classloader中
     *
     * @param dexClassLoader
     */
    public void addPluginClassLoader(DexClassLoader dexClassLoader) {
        mClassLoaderList.add(dexClassLoader);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            //先查找parent classloader 这里实际就是系统帮我们创建classloader，目标对应为宿主apk
            clazz = getParent().loadClass(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clazz != null) {
            return clazz;
        }

        //遍历插件，查找对应的类
        if (mClassLoaderList != null && !mClassLoaderList.isEmpty()) {
            for (DexClassLoader classLoader : mClassLoaderList) {
                try {
                    //这里只查找它自己的apk，不需要查parent，避免多次无用查询，提高性能
                    clazz = classLoader.loadClass(name);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new ClassNotFoundException(name + " in ClassLoader " + this);
    }
}
