package com.jikexueyuan.activitysendmessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        //获得启动此Activity的Intent
        Intent intent = getIntent();

        //把发送过来的信息打印出来
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("收到的信息是：" + intent.getStringExtra("data"));

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
