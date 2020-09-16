package com.code.reflect.generic;

import com.code.reflect.Singleton;

/**
 * @ClassName GenericAMN
 * @Author: Lary.huang
 * @CreateDate: 2020/9/11 11:41 AM
 * @Version: 1.0
 * @Description: TODO
 */
public class GenericAMN {
    private static final Singleton<ClassB2Interface> gDefault = new Singleton<ClassB2Interface>() {
        @Override
        protected ClassB2Interface create() {
            System.out.println("执行了create方法");
            ClassB2 b2 = new ClassB2();
            b2.id = 100;
            return b2;
        }
    };

    public static ClassB2Interface getDefault() {
        return gDefault.get();
    }
}
