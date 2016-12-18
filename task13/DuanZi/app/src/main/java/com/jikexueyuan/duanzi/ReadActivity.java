package com.jikexueyuan.duanzi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 显示文章内容
 */
public class ReadActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        Joke joke = getIntent().getParcelableExtra("joke");
        tvTitle.setText(joke.getTitle());
        tvContent.setText(joke.getContent());
    }
}
