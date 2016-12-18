package com.jikexueyuan.todaydolt;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    private static final Uri URI = Uri.parse("content://com.jikexueyuan.eventprovider");

    private EditText etEventTime, etEventContent;
    private Button btnStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initView();
    }

    private void initView() {
        etEventTime = (EditText) findViewById(R.id.etEventTime);
        etEventContent = (EditText) findViewById(R.id.etEventContent);

        btnStorage = (Button) findViewById(R.id.btnStorage);
        btnStorage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStorage:
                storageEvent();
                break;
        }
    }

    private void storageEvent() {
        String time = etEventTime.getText().toString().trim();
        String content = etEventContent.getText().toString().trim();

        if ("".equals(time)) {
            Toast.makeText(this, "请输入时间", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(time) > 23) {
            Toast.makeText(this, "请输入0-23点", Toast.LENGTH_SHORT).show();
            etEventTime.setText("");
            return;
        }

        if ("".equals(content)) {
            Toast.makeText(this, "请输入事件", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues eventValues = new ContentValues();

        eventValues.put("time", time);
        eventValues.put("content", content);

        getContentResolver().insert(URI, eventValues);

        Toast.makeText(this, R.string.toastAdd, Toast.LENGTH_SHORT).show();
    }
}
