package com.jikexueyuan.usingcprintlog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // 加载库
    static {
        System.loadLibrary("usingCLog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsingCPrintLog.usingCPrintLog("This is a log from cpp");
    }
}
