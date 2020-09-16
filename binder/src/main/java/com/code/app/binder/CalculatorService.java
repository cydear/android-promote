package com.code.app.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.code.app.binder.aidl.ICalculator;

/**
 * @ClassName CalculatorService
 * @Author: Lary.huang
 * @CreateDate: 2020/9/10 10:23 AM
 * @Version: 1.0
 * @Description: TODO
 */
public class CalculatorService extends Service {
    private static final String TAG = "Cal.Log";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return calculatorBinder;
    }

    private ICalculator.Stub calculatorBinder = new ICalculator.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void sum(int a, int b) throws RemoteException {
            Log.d(TAG, "sum=" + (a + b));
        }

        @Override
        public void sub(int a, int b) throws RemoteException {
            Log.d(TAG, "sub=" + (a + b));
        }
    };
}
