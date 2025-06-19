package com.example.leanandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.leanandroid.AIDL.AidlInterface1;

// 使用AIDL进行通信
// 服务端实现接口并公开

public class AIDLService1 extends Service
{

    public AIDLService1()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return new MyBinder();
    }

    class MyBinder extends AidlInterface1.Stub
    {

        @Override
        public int add(int a, int b) throws RemoteException {
            Log.d("AIDLService1", "add: ");
            return a + b;
        }
    }
}