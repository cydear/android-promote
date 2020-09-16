package com.code.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName RefInvoke
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 2:08 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class RefInvoke {
    /**
     * 根据className创建实例化对象
     *
     * @param className  类的全路径名 包名+类名
     * @param pareTypes  构造方法的参数类型
     * @param pareValues 实例化时构造方法的参数值
     * @return
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class<?> r = Class.forName(className);
            Constructor ctor = r.getDeclaredConstructor(pareTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据clazz 创建类的实例对象
     *
     * @param clazz      类的Class对象
     * @param pareTypes  构造方法的参数类型
     * @param pareValues 实例化时构造方法的参数值
     * @return
     */
    public static Object createObject(Class clazz, Class[] pareTypes, Object[] pareValues) {
        try {
            Constructor ctor = clazz.getDeclaredConstructor(pareTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用普通的实例方法
     *
     * @param obj        方法所在类的实例
     * @param methodName 方法名
     * @param pareTypes  方法参数类型
     * @param pareValues 调用方法的参数值
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用静态方法
     *
     * @param className  方法所在类的全路径名：包名+类名
     * @param methodName 方法名
     * @param pareTypes  方法的参数类型
     * @param pareValues 方法参数值
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Class r = Class.forName(className);
            Method method = r.getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字段的属性值
     *
     * @param className 字段所属的类全路径名：包名+类名
     * @param obj       字段所属类的对象
     * @param fieldName 字段名
     * @return
     */
    public static Object getFieldObject(String className, Object obj, String fieldName) {
        try {
            Class obj_class = Class.forName(className);
            Field field = obj_class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取静态字段的属性值
     *
     * @param className
     * @param fieldName
     * @return
     */
    public static Object getStaticFieldObject(String className, String fieldName) {
        try {
            Class obj_class = Class.forName(className);
            Field field = obj_class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给字段设置值
     *
     * @param className  字段所属类的泉路劲名：包名+类名
     * @param obj        字段所属类的对象
     * @param fieldName  字段名
     * @param fieldValue 字段需要设置的值
     */
    public static void setFieldObject(String className, Object obj, String fieldName, Object fieldValue) {
        try {
            Class obj_class = Class.forName(className);
            Field field = obj_class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
