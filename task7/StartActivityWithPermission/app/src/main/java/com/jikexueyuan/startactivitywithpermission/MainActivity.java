package com.jikexueyuan.startactivitywithpermission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 被启动的Activity
 * 在AndroidManifest.xml中配置权限及启动Action
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
