package com.jikexueyuan.todaydolt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dej on 2016/9/25.
 * 用于开机启动EventManageService
 */
public class BootService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("BootService自启动");
        context.startService(new Intent(context, EventManageService.class));
    }
}
