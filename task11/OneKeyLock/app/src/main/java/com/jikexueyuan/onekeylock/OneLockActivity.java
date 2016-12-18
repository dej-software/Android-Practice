package com.jikexueyuan.onekeylock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

/**
 * 一键锁屏Activity
 * 设置 android:theme="@android:style/Theme.NoDisplay"
 */
public class OneLockActivity extends Activity {

    private DevicePolicyManager myDevPolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否有权限 没有则启动设置界面
        myDevPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if (myDevPolicyManager.isAdminActive(new ComponentName(this, MyDeviceAdminReceiver.class))) {
            myDevPolicyManager.lockNow();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        System.out.println("finish");
        finish();
    }
}
