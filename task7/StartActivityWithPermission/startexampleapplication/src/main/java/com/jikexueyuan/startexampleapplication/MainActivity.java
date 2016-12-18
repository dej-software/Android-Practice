package com.jikexueyuan.startexampleapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 启动其他Activity
 * 在AndroidManifest.xml中配置权限
 */
public class MainActivity extends AppCompatActivity {

    private String ACTION = "com.jikexueyuan.startactivitywithpermission.intent.action.ExampleApplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStartAty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ACTION));
            }
        });
    }
}
