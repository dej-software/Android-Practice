package com.jikexueyuan.guessnumber;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //数字编辑
    private EditText editNumber;
    //信息提示
    private TextView textInfo;
    //需要猜的数字
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //把初始化单独放入一个方法中
        initView();
    }

    private void initView() {

        //绑定相应的部件
        editNumber = (EditText) findViewById(R.id.editNumber);
        textInfo = (TextView) findViewById(R.id.textInfo);
        //数字初始化
        number = (int) (Math.random() * 100);

        MyButtonListener myButtonListener = new MyButtonListener();

        //提交按钮监听
        findViewById(R.id.btnSubmit).setOnClickListener(myButtonListener);
        //换数字按钮监听
        findViewById(R.id.btnAgain).setOnClickListener(myButtonListener);
    }

    //创建一个自己按钮监听类
    class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnSubmit:
                    //处理输入的数字
                    int inputNumber = Integer.parseInt(editNumber.getText().toString());
                    if (inputNumber == number) {
                        textInfo.setText("提示：恭喜猜对！");
                        textInfo.setTextColor(Color.GREEN);
                    } else if (inputNumber < number) {
                        textInfo.setText("提示：数字小了！");
                        textInfo.setTextColor(Color.RED);
                    } else if (inputNumber > number){
                        textInfo.setText("提示：数字大了！");
                        textInfo.setTextColor(Color.BLUE);
                    }
                    break;
                case R.id.btnAgain:
                    //初始化新数字，重新开始
                    number = (int) (Math.random() * 100);
                    textInfo.setText("提示：请输入数字!");
                    textInfo.setTextColor(Color.GRAY);
                    break;
                default:
                    break;
            }
        }
    }
}
