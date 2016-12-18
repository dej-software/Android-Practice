package com.jikexueyuan.simpleimagebrowser;

import android.content.Intent;
import android.graphics.Picture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageBrowser extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);

        // 从Intent中获得data并显示到ImageView
        Intent intent = getIntent();

        System.out.println("intent.getData():" + intent.getData());
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(intent.getData());
    }
}
