package com.jikexueyuan.dailywish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 显示许下的愿望
    private TextView showWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        System.out.println("MainActivity onCreate");
    }

    // 一些activity的初始化
    void initView() {
        showWish = (TextView) findViewById(R.id.showWish);

        findViewById(R.id.btnWish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessageInputActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity onResume");
    }

    // 设置launchMode="singleTask"时 使用onNewIntent得到其他Activity传回来的Intent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        String wishStr = getIntent().getStringExtra("wishMsg");

        System.out.println("onNewIntent" + wishStr);
        if (wishStr != null) {
            showWish.setText("您今天的愿望是：\n" + wishStr);
        } else {
            showWish.setText("");
            Toast.makeText(MainActivity.this, "您取消了许愿操作", Toast.LENGTH_SHORT).show();
        }
    }
}
