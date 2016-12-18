package com.jikexueyuan.scaleimage;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView ivScale;
    private int initWidth;
    private float startDistance = -1, endDistance = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 一些初始化
     */
    private void initView() {
        ivScale = (ImageView) findViewById(R.id.ivScale);
        ivScale.setOnTouchListener(this);

        // 得到初始宽度 用于限制图片的缩放比例
        ivScale.post(new Runnable() {
            @Override
            public void run() {
                initWidth = ivScale.getWidth();
                System.out.println("initWidth:" + initWidth);
            }
        });
    }

    /**
     * 触摸的监听处理
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView imageView = (ImageView) v;

        int actionTag = MotionEventCompat.getActionMasked(event);
        switch (actionTag) {
            // 单点触摸
            case MotionEvent.ACTION_DOWN:
//                System.out.println("ACTION_DOWN");
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
//                System.out.println("ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // 手指离开时
                startDistance = -1;
                break;
            // 手指滑动时处理缩放
            case MotionEvent.ACTION_MOVE:
//                System.out.println("ACTION_MOVE");
                // 只处理两点触摸的情况
                if (MotionEventCompat.getPointerCount(event) == 2) {
                    if (startDistance == -1) {
                        startDistance = pointDistance(event);
                    } else {
                        endDistance = pointDistance(event);
                        scaleView(v, startDistance, endDistance);
                        // 实现手指不离开时动态滑动
                        startDistance = endDistance;
                    }
                }
                break;
        }

        return true;
    }

    /**
     * 缩放一个View
     *
     * @param view
     * @param startDistance
     * @param endDistance
     */
    private void scaleView(View view, float startDistance, float endDistance) {
        float diff = endDistance - startDistance;
        System.out.println("startDistance:" + startDistance + " endDistance:" + endDistance + " diff:" + diff);
        // 两个距离大于5时才缩放
        if (Math.abs(diff) > 5) {
            // LayoutParams类型要与view所在布局类型一致 本程序里ImageView所在布局为FrameLayout(使用FrameLayout才能无限放大)
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.width = (int) (view.getWidth() + diff * 1.2);
            lp.height = (int) (view.getHeight() + diff * 1.2);

            // 超过一定缩放大小后 不进行缩放
            if ((diff < 0) && (lp.width < initWidth / 3.0f)) {
                return;
            }
            if ((diff > 0) && (lp.height > initWidth * 4.0f)) {
                return;
            }

            view.setLayoutParams(lp);
        }
    }

    /**
     * 获取两个触点的距离
     *
     * @param event
     * @return
     */
    private float pointDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 获取两个触点的中点
     *
     * @param event
     * @return
     */
    private PointF pointMiddle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);

        return new PointF(x, y);
    }
}
