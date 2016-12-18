// ITimerServiceCallback.aidl
package com.jikexueyuan.timerservice;

// Declare any non-default types here with import statements
/**
 *  回调接口 传入RemoteCallbackList中使用
 **/
interface ITimerServiceCallback {
    /**
     * 获取数据的接口 得到一个int数据：显示在应用界面的计数
     */
    void onTimerCallback(int result);
}
