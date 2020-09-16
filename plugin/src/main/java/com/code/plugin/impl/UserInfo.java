package com.code.plugin.impl;


import android.util.Log;

import com.code.app.baselib.ICallback;
import com.code.app.baselib.IMessage;
import com.code.app.baselib.IPerson;

/**
 * @ClassName UserInfo
 * @Author: Lary.huang
 * @CreateDate: 2020/9/15 7:24 PM
 * @Version: 1.0
 * @Description: TODO
 */
public class UserInfo implements IPerson, IMessage {
    private String userName;
    private ICallback callback;

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public void setName(String name) {
        this.userName = name;
    }

    @Override
    public void register(ICallback callback) {
        this.callback = callback;
    }

    @Override
    public void sendMessage(String result) {
        Log.d("plugin.log", "send message =>" + result);
        if (callback != null) {
            callback.sendMessage(userName + " => " + result);
        }
    }
}
