package com.jikexueyuan.simpleimagebrowser;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppPicture();
        findViewById(R.id.btnOpenImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = null;
                try {
                    InputStream is= getResources().getAssets().open("image/image.jpg");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));

                // 设置ACTION及Data、Type 隐式启动图片查看
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(getAppPicture()), "image/*");
                intent.setDataAndType(uri, "image/*");

                startActivity(intent);
            }
        });
    }

    /**
     * 把assets文件夹中的image.jpg图片拷贝到SD中
     * @return
     */
    private File getAppPicture() {
//        File file = new File("/data/data/" + getPackageName() + "/files/image.jpg"); // 会导致其他应用没权限查看照片
        File file = new File("/storage/sdcard/Pictures/image.jpg");
        if (!file.exists()) {

            try {
                InputStream is= getResources().getAssets().open("image/image.jpg");
//                Bitmap image = BitmapFactory.decodeStream(is);
                OutputStream os = new FileOutputStream(file);

                byte[] bytes = new byte[30];
                while (is.read(bytes) != -1) {
                    os.write(bytes);
                }

                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
