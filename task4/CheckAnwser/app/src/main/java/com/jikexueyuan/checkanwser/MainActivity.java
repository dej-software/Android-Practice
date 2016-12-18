package com.jikexueyuan.checkanwser;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //所需要用到的控件
    private Button btnSubmit;
    private TextView tvSex, tvQuestion, tvMessage;
    private RadioButton rbMan, rbWoman;
    private CheckBox cbOcean1, cbOcean2, cbOcean3, cbOcean4;
    private CheckBox cbError1, cbError2;
    // 提交后要显示的信息
    private String sex, anwser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化
        initView();
    }

    private void initView() {
        //绑定控件id
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        rbMan = (RadioButton) findViewById(R.id.rbMan);
        rbWoman = (RadioButton) findViewById(R.id.rbWoman);

        cbError1 = (CheckBox) findViewById(R.id.cbError1);
        cbError2 = (CheckBox) findViewById(R.id.cbError2);
        cbOcean1 = (CheckBox) findViewById(R.id.cbOcean1);
        cbOcean2 = (CheckBox) findViewById(R.id.cbOcean2);
        cbOcean3 = (CheckBox) findViewById(R.id.cbOcean3);
        cbOcean4 = (CheckBox) findViewById(R.id.cbOcean4);

        //设置一些初始信息 如默认选择、颜色等
        rbMan.setChecked(true);
        sex = "男，";
        anwser = "多项选择题错误";

        tvSex.setTextColor(Color.BLUE);
        tvQuestion.setTextColor(Color.BLUE);
        tvMessage.setTextColor(Color.BLUE);

        //设置控件监听方法
        btnSubmit.setOnClickListener(this);
    }

    //普通按钮的处理 Button类
    @Override
    public void onClick(View v) {
        if (rbMan.isChecked()) {
            sex = "男，";
        }
        if (rbWoman.isChecked()) {
            sex = "女，";
        }

        if (cbError1.isChecked() && cbError2.isChecked() && (!cbOcean1.isChecked()) && (!cbOcean2.isChecked()) && (!cbOcean3.isChecked()) && (!cbOcean4.isChecked())) {
            anwser = "多项选择题正确";
        } else {
            anwser = "多项选择题错误";
        }

        tvMessage.setText("你选择的性别：" + sex + anwser);
    }
}
