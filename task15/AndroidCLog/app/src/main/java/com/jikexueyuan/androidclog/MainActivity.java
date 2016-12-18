package com.jikexueyuan.androidclog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 加载库
    static {
        System.loadLibrary("using_c_print_log-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用C++方法打印Log
        UsingCPrintLog.usingCPrintLog("This is a log from cpp");
    }
}
