package com.jikexueyuan.launchlocalappfrombrowser;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStartActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用隐式Intent启动
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("local://example.cpp")));
            }
        });
    }
}
