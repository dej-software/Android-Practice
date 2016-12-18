package com.jikexueyuan.testapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jikexueyuan.timerservice.ITimerServiceCallback;
import com.jikexueyuan.timerservice.ITimerServiceRemoteBinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    // 声明要操作的服务相关信息
    private static final String TIMERSERVICE_PKG_NAME = "com.jikexueyuan.timerservice";
    private static final String TIMERSERVICE_CLS_NAME = "com.jikexueyuan.timerservice.TimerService";
    private Intent serviceIntent;

    // 界面上的一些控件
    private TextView tvResult;
    private Button btnBindService;
    private Button btnUnbindService;

    // Binder接口
    private ITimerServiceRemoteBinder serviceBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 一些初始化
     */
    private void initView() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnBindService = (Button) findViewById(R.id.btnBindService);
        btnUnbindService = (Button) findViewById(R.id.btnUnbindService);

        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);

        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName(TIMERSERVICE_PKG_NAME, TIMERSERVICE_CLS_NAME));
    }

    /**
     * 按钮监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBindService:
                bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                unRegisterAndUnbind();
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // 获取service返回的Binder
        serviceBinder = ITimerServiceRemoteBinder.Stub.asInterface(service);

        // 注册接口
        try {
            serviceBinder.registerCallback(onServiceCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // 服务被销毁时（断开连接）
        unRegisterAndUnbind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出应用时要解绑服务，注销回调接口
        unRegisterAndUnbind();
    }

    /**
     * 注销Callback接口 并解绑服务
     */
    private void unRegisterAndUnbind() {
        if (serviceBinder != null) {
            try {
                serviceBinder.unregisterCallback(onServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(this);
            serviceBinder = null;
        }
    }

    /**
     * 实现回调对象的接口
     * 得到返回数据后传给handler处理
     */
    private ITimerServiceCallback.Stub onServiceCallback = new ITimerServiceCallback.Stub(){
        @Override
        public void onTimerCallback(int result) throws RemoteException {
            Message msg = new Message();
            msg.obj = MainActivity.this;
            msg.arg1 = result;
            handler.sendMessage(msg);
        }
    };

    // 声明一个MyHandler对象
    private final MyHandler handler = new MyHandler();

    /**
     * 实现自己的Handler处理Message
     * 显示到界面中
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int result = msg.arg1;
            MainActivity _this = (MainActivity) msg.obj;
            _this.tvResult.setText("Current value is " + result);
        }
    }
}
