package com.jikexueyuan.timerservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    // 声明要操作的服务相关信息
    private Intent serviceIntent;
    // 服务是否绑定
    private boolean binding = false;

    // 界面上的一些控件
    private TextView tvResult;
    private Button btnBindService;
    private Button btnUnbindService;

    // 服务Binder对象
    private TimerService.TimerServiceBinder serviceBinder = null;

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

        serviceIntent = new Intent(MainActivity.this, TimerService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBindService:
                bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                binding = true;
                break;
            case R.id.btnUnbindService:
                if (binding) {
                    unbindService(this);
                    binding = false;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 退出程序时解绑服务
        if (binding) {
            unbindService(this);
            binding = false;
        }
    }

    @Override
    public void onServiceConnected(final ComponentName name, IBinder service) {
        // 得到service 并实现回调函数
        serviceBinder = (TimerService.TimerServiceBinder) service;
        serviceBinder.getService().setCallback(new TimerService.LocalCallback() {
            @Override
            public void onTimerCallback(int result) {
                Message msg = new Message();
                msg.arg1 = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 定义一个Handler重写handleMessage处理回调函数中的信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int result = msg.arg1;
            tvResult.setText("Current value is " + result);
        }
    };
}
