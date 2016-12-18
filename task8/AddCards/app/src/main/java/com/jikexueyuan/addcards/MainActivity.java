package com.jikexueyuan.addcards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    // 布局
    private LinearLayout layout;
    private LinearLayout[] layouts;
    // 代表卡片
    private Button button;
    // 卡片数字、行数、列数
    private int count, row, column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        addCards();
    }

    /**
     * 初始化
     */
    private void initView() {

        // 初始化数值、行列
        count = 1;
        row = 5;
        column = 4;

        // 每行用一个LinearLayout布局
        layouts = new LinearLayout[row];

        // 主布局
        layout = new LinearLayout(this);
        setContentView(layout);

        // 设置主布局参数 宽、高、开始的位置
        layout.post(new Runnable() {
            @Override
            public void run() {
                int width = layout.getWidth();
                int height = layout.getHeight();
                System.out.println("layout getWidth:" + width);
                System.out.println("layout getHeight:" + height);
                ViewGroup.LayoutParams lp = layout.getLayoutParams();
                lp.width = width;
                lp.height = width / column * row; // 让控件长宽均匀
                layout.setLayoutParams(lp);
                layout.setY((height - width / column * row) / 2); // 显示在中间
            }
        });
    }

    /**
     * 添加卡片
     */
    private void addCards() {
        for (int i = 0; i < row; i++) {
            layouts[i] = new LinearLayout(this);
            for (int j = 0; j < column; j++) {
                button = new Button(this);
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(Color.RED);
                button.setTextSize(30);
                button.setText(count + "");
                count++;
                LinearLayout.LayoutParams lpButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lpButton.leftMargin = 10;
                lpButton.rightMargin = 10;
                lpButton.topMargin = 10;
                lpButton.bottomMargin = 10;
                layouts[i].addView(button, lpButton);
            }

            LinearLayout.LayoutParams lpLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            layouts[i].setBackgroundColor(Color.BLUE);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(layouts[i], lpLayout);
        }
    }
}
