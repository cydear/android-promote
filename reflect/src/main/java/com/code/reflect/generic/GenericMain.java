package com.code.reflect.generic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName GenericMain
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 11:47 AM
 * @Version: 1.0
 * @Description: TODO
 */
public class GenericMain {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //反射拿到mInstance
        Class<?> singleton = Class.forName("com.code.reflect.Singleton");
        //获取mInstance字段
        Field instanceField = singleton.getDeclaredField("mInstance");
        instanceField.setAccessible(true);

        //反射获取GenericAMN
        Class<?> genericAMN = Class.forName("com.code.reflect.generic.GenericAMN");
        //获取gDefault字段
        Field gDefaultField = genericAMN.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        //获取字段值
        Object gDefault = gDefaultField.get(null);
        System.out.println("gDefault=>" + gDefault);
        //查找原始的B2对象
        Method getMethod = singleton.getDeclaredMethod("get");
        getMethod.setAccessible(true);
        Object rawB2Object = getMethod.invoke(gDefault);
        System.out.println("rawB2Object=>" + rawB2Object);

        Class<?> classB2Interface = Class.forName("com.code.reflect.generic.ClassB2Interface");
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{classB2Interface},
                new ClassB2(rawB2Object)
        );
        instanceField.set(gDefault, proxy);
        System.out.println("rawB2Object=>" + instanceField.get(gDefault));
    }
}
