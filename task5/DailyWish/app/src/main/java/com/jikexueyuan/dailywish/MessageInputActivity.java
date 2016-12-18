package com.jikexueyuan.dailywish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageInputActivity extends AppCompatActivity implements View.OnClickListener {

    // 用到的控件
    private EditText etInputWish;
    private Intent intentMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_input);

        initView();
    }

    // 初始化Activity里的相关控件
    private void initView() {

        etInputWish = (EditText) findViewById(R.id.etInputWish);
        intentMsg = new Intent(MessageInputActivity.this, MainActivity.class);

        findViewById(R.id.btnConfirm).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // 确定 或 返回 按钮的操作
        switch (v.getId()) {
            case R.id.btnConfirm:
                String wishStr = etInputWish.getText().toString();
                if (!wishStr.equals("")) {
                    intentMsg.putExtra("wishMsg", wishStr);
                    startActivity(intentMsg);
                } else {
                    Toast.makeText(MessageInputActivity.this, "请输入愿望", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:
                intentMsg.putExtra("wishMsg", (String) null);
                startActivity(intentMsg);
                break;
        }
    }
}
