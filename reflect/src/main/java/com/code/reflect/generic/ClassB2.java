package com.code.reflect.generic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName ClassB2
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 11:45 AM
 * @Version: 1.0
 * @Description: TODO
 */
public class ClassB2 implements ClassB2Interface, InvocationHandler {
    public int id;
    private ClassB2Interface classB2Interface;

    public ClassB2(Object rawB2Object) {
        this.classB2Interface= (ClassB2Interface) rawB2Object;
    }

    public ClassB2() {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
