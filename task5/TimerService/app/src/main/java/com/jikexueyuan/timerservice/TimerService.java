package com.jikexueyuan.timerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class TimerService extends Service {

    // 声明RemoteCallbackList对象，用来保存回调接口对象
    private RemoteCallbackList<ITimerServiceCallback> callbackList = new RemoteCallbackList<>();
    // running服务线程是否运行的判断 timerNum服务工作处理的数据，即回调给客户端的计数值
    private boolean running = false;
    private int timerNum;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TimerServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timerServiceWork();
    }

    /**
     * 在服务里所做的工作：
     * 本服务实现一个timer返回计数
     */
    private void timerServiceWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;

                for (timerNum = 0; running; timerNum++) {
                    if (callback != null) {
                        callback.onTimerCallback(timerNum); //本地回调的处理
                    }
                    callbackBroadcast(timerNum); // 远程调用服务的处理

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * TimerService的回调方法
     *
     * @param result 要回调的结果
     */
    private void callbackBroadcast(int result) {
        // 获得所有的客户端 一一回调回去
        int callbackCount = callbackList.beginBroadcast();
        while (callbackCount-- > 0) {
            try {
                callbackList.getBroadcastItem(callbackCount).onTimerCallback(result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        // 回调结束后调用
        callbackList.finishBroadcast();
    }

    /**
     * ITimerServiceRemoteBinder.aidl
     * 实现服务的Binder
     */
    public class TimerServiceBinder extends ITimerServiceRemoteBinder.Stub {

        public TimerService getService() {
            return TimerService.this;
        }

        @Override
        public void registerCallback(ITimerServiceCallback callback) throws RemoteException {
            callbackList.register(callback);
        }

        @Override
        public void unregisterCallback(ITimerServiceCallback callback) throws RemoteException {
            callbackList.unregister(callback);
        }
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    private LocalCallback callback = null;

    public LocalCallback getCallback() {
        return callback;
    }

    public void setCallback(LocalCallback callback) {
        this.callback = callback;
    }

    /**
     * 本地服务调用接口实现
     */
    public static interface LocalCallback {
        void onTimerCallback(int result);
    }
}
