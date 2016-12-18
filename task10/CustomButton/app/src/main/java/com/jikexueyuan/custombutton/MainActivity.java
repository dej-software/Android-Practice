package com.jikexueyuan.custombutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Button 在drawable目录中配置相关的背景
 * CheckBox 在style.ml中添加样式 配合drawable下的checkbox_state.xml
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
