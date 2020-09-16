package com.code.app.baselib;

/**
 * @ClassName IMessage
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 9:30 PM
 * @Version: 1.0
 * @Description: TODO
 */
public interface IMessage {
    void register(ICallback callback);

    void sendMessage(String result);
}
