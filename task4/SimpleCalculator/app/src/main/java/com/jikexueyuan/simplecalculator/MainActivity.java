package com.jikexueyuan.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 计算结果
    private TextView tvResult;
    // 0-9 按钮
    private Button btnNumber0, btnNumber1, btnNumber2;
    private Button btnNumber3, btnNumber4, btnNumber5;
    private Button btnNumber6, btnNumber7, btnNumber8, btnNumber9;
    // + - * / 按钮、等于=、清零C 按钮
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnEqual, btnClean;

    // 计算结果
    private String resultStr;
    // 算式：左值 右值 运算符
    private String leftNumber, rightNumber, operator;
    // 输入的数字字符
    private String inputNumber;
    // 是否可计算 是否输入了左右值
    boolean bOperator = false;
    boolean newOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    // 初始化
    private void initView() {
        tvResult = (TextView) findViewById(R.id.tvResult);

        btnNumber0 = (Button) findViewById(R.id.btnNumber0);
        btnNumber1 = (Button) findViewById(R.id.btnNumber1);
        btnNumber2 = (Button) findViewById(R.id.btnNumber2);
        btnNumber3 = (Button) findViewById(R.id.btnNumber3);
        btnNumber4 = (Button) findViewById(R.id.btnNumber4);
        btnNumber5 = (Button) findViewById(R.id.btnNumber5);
        btnNumber6 = (Button) findViewById(R.id.btnNumber6);
        btnNumber7 = (Button) findViewById(R.id.btnNumber7);
        btnNumber8 = (Button) findViewById(R.id.btnNumber8);
        btnNumber9 = (Button) findViewById(R.id.btnNumber9);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSubtract = (Button) findViewById(R.id.btnSubtract);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnDivide = (Button) findViewById(R.id.btnDivide);
        btnEqual = (Button) findViewById(R.id.btnEqual);
        btnClean = (Button) findViewById(R.id.btnClean);

        btnNumber0.setOnClickListener(this);
        btnNumber1.setOnClickListener(this);
        btnNumber2.setOnClickListener(this);
        btnNumber3.setOnClickListener(this);
        btnNumber4.setOnClickListener(this);
        btnNumber5.setOnClickListener(this);
        btnNumber6.setOnClickListener(this);
        btnNumber7.setOnClickListener(this);
        btnNumber8.setOnClickListener(this);
        btnNumber9.setOnClickListener(this);

        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnEqual.setOnClickListener(this);
        btnClean.setOnClickListener(this);

        // 初始化值
        inputNumber = "";
        leftNumber = "";
        rightNumber = "";
    }

    @Override
    public void onClick(View v) {
        Button btnTemp = (Button) v;
        String input = btnTemp.getText().toString();

        switch (btnTemp.getId()) {
            case R.id.btnAdd:
            case R.id.btnSubtract:
            case R.id.btnMultiply:
            case R.id.btnDivide:
                // 如果输入了运算符 处理运算符
                if (!bOperator) {
                    // bOperator设为true，可进行运算
                    bOperator = true;
                    operator = input;
                    // 可以输入右值 先把inputNumber清空
                    inputNumber = "";
                    // 如果左值未输入直接输运算符 设左值为0
                    if (leftNumber.equals("")) {
                        leftNumber = "0";
                    }
                } else {
                    // 重复输入运算符
                    if (newOperator) {
                        // 以上一次等号运算后的结果为下一次运算的左值
                        newOperator = false;
                    }

                    if (!rightNumber.equals("")) {
                        // 已经输入了右值情况下 先进行连算处理
                        calculation();
                    }
                    //更新运算符
                    operator = input;
                }
                break;

            case R.id.btnEqual:
                // 如果输入了= 进行求值
                if (!leftNumber.equals("") && !rightNumber.equals("") && bOperator) {
                    // 运算处理
                    calculation();
                    // =运算后可直接输入数字 开始新的计算
                    newOperator = true;
                }
                break;

            case R.id.btnClean:
                // 如果输入了C 清零 回到初始化状态
                cleanCaclulator();
                break;

            default:
                // 如果在=号运算后直接输入数字 开始新的计算
                if (newOperator) {
                    cleanCaclulator();
                }
                // 数字按钮 输入左右值
                if (!bOperator) {
                    leftNumber = getInputNumber(input);
                } else {
                    rightNumber = getInputNumber(input);
                }
                break;
        }

//  此段原为判断输入处理 用上面的switch语句代替
//        if (input.equals(btnAdd.getText().toString()) || input.equals(btnSubtract.getText().toString())
//                || input.equals(btnMultiply.getText().toString()) || input.equals(btnDivide.getText().toString())) {
//
//        } else if (input.equals(btnEqual.getText().toString())) {
//
//        } else if (input.equals(btnClean.getText().toString())) {
//
//        } else {
//
//        }

        System.out.println("test:" + "bOperator = " + bOperator + " newOperator = " + newOperator);
        System.out.println("test:" + leftNumber + " " + operator + " " + rightNumber + " = " + "abc");
    }

    /**
     * 获取输入的数字字符
     * @param input 输入的一个数字字符
     * @return inputNumber 返回最终的数字字符
     */
    private String getInputNumber(String input) {

        // 处理首位 0不要叠加
        if ((inputNumber.indexOf("0") == 0)) {
            if (input.equals("0")) {
                inputNumber = "0";
            } else {
                inputNumber = input;
            }
        } else {
            inputNumber += input;
        }

        tvResult.setText(inputNumber);
        return inputNumber;
    }

    // 连续运算方式
    private void calculation() {
        // 把计算结果存入左值中
        leftNumber = getOperatorResult(leftNumber, rightNumber, operator);
        tvResult.setText(leftNumber);
        // 在计算结束后重置右值
        inputNumber = "";
        rightNumber = "";
    }

    // 清零初始化 开始一个新的计算
    private void cleanCaclulator() {
        tvResult.setText("0");
        bOperator = false;
        newOperator = false;
        inputNumber = "";
        leftNumber = "";
        rightNumber = "";
    }

    /**
     * 简单的整形四则运算
     *
     * @param leftNumber  左运算值
     * @param rightNumber 右运算值
     * @param operator    运算符
     * @return 返回String型运算结果
     */
    private String getOperatorResult(String leftNumber, String rightNumber, String operator) {

        int result = 0;
        int left = Integer.parseInt(leftNumber);
        int right = Integer.parseInt(rightNumber);

        switch (operator) {
            case "+":
                result = left + right;
                break;
            case "-":
                result = left - right;
                break;
            case "*":
                result = left * right;
                break;
            case "/":
                if (right == 0) {
                    // 除数为零
                    return "除数不能为0";
                } else {
                    result = left / right;
                }
                break;
        }

        System.out.println(left + operator + right + "=" + result);
        return String.valueOf(result);
    }
}
