package com.jikexueyuan.onekeylock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 主界面 一键锁屏设置
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDevAdmin, btnLock;
    private DevicePolicyManager myDevPolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        btnDevAdmin = (Button) findViewById(R.id.btnDevAdmin);
        btnLock = (Button) findViewById(R.id.btnLock);

        btnDevAdmin.setOnClickListener(this);
        btnLock.setOnClickListener(this);

        // 获得设备安全管理
        myDevPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        // 需要授权的组件名
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);

        if (myDevPolicyManager.isAdminActive(componentName)) {
            btnDevAdmin.setText("取消设备管理者权限");
            btnLock.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDevAdmin:
                if (!myDevPolicyManager.isAdminActive(componentName)) {
                    // 申请权限
                    Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    startActivityForResult(i, 0);
                } else {
                    myDevPolicyManager.removeActiveAdmin(componentName);
                    btnDevAdmin.setText("获取设备管理者权限");
                    btnLock.setVisibility(View.GONE);
                }
                break;
            case R.id.btnLock:
                if (myDevPolicyManager.isAdminActive(componentName)) {
                    myDevPolicyManager.lockNow();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("resultCode:" + resultCode);
        // resultCode 0：不同意授权 -1：同意授权
        if (resultCode == -1) {
            btnDevAdmin.setText("取消设备管理者权限");
            btnLock.setVisibility(View.VISIBLE);
        }
    }
}
