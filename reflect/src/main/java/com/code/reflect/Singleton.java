package com.code.reflect;

/**
 * @ClassName Singleton
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 11:43 AM
 * @Version: 1.0
 * @Description: TODO
 */
public abstract class Singleton<T> {
    private T mInstance;

    protected abstract T create();

    public final T get() {
        System.out.println("获取mInstance---before");
        synchronized (this) {
            System.out.println("获取mInstance---midden");
            if (mInstance == null) {
                System.out.println("获取mInstance---end");
                mInstance = create();
            }
            return mInstance;
        }
    }
}
