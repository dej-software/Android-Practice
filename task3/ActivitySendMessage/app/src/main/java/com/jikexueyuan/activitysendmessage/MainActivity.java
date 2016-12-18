package com.jikexueyuan.activitysendmessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        findViewById(R.id.btnSendMsg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //使用Intent发送信息到新启动的Activity
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                intent.putExtra("data", editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
