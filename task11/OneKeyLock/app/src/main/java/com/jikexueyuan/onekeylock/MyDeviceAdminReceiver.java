package com.jikexueyuan.onekeylock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dej on 2016/10/21.
 */
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

        Toast.makeText(context, "已获取设备管理者权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);

        Toast.makeText(context, "您取消了设备管理者权限", Toast.LENGTH_SHORT).show();
    }
}
