// ITimerServiceRemoteBinder.aidl
package com.jikexueyuan.timerservice;

import com.jikexueyuan.timerservice.ITimerServiceCallback;
// Declare any non-default types here with import statements
/**
 *  用来获取远程服务的Binder对象
 **/
interface ITimerServiceRemoteBinder {
    /**
     * 注册或注销回调接口ITimerServiceCallback的接口
     */
    void registerCallback(ITimerServiceCallback callback);
    void unregisterCallback(ITimerServiceCallback callback);
}
