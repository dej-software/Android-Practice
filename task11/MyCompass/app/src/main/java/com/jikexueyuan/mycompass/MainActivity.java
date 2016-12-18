package com.jikexueyuan.mycompass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private ImageView ivCompass;
    private float curDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    // 初始化
    private void initView() {
        ivCompass = (ImageView) findViewById(R.id.ivCompass);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册方向传感器
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 注销方向传感器
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];
            System.out.println("指南针（方向）传感器：" + String.format("value=%f\tcurDegree = %f", degree, curDegree));

            // 旋转图片 指示方向
//            RotateAnimation ra = new RotateAnimation(curDegree, degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            ra.setDuration(200);
//            ivCompass.startAnimation(ra);
//
//            curDegree = degree;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

            Matrix matrix = new Matrix();
            matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(bitmap, matrix, paint);

            ivCompass.setImageBitmap(newBitmap);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
