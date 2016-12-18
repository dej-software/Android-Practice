package com.jikexueyuan.movebutton;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnXMLAnimation, btnCodeAnimation;
    private Button btnXMLAnimator, btnCodeAnimator;
    private ImageView ivMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        btnXMLAnimation = (Button) findViewById(R.id.btnXMLAnimation);
        btnCodeAnimation = (Button) findViewById(R.id.btnCodeAnimation);
        btnXMLAnimator = (Button) findViewById(R.id.btnXMLAnimator);
        btnCodeAnimator = (Button) findViewById(R.id.btnCodeAnimator);

        ivMove = (ImageView) findViewById(R.id.ivMove);

        btnXMLAnimation.setOnClickListener(this);
        btnCodeAnimation.setOnClickListener(this);
        btnXMLAnimator.setOnClickListener(this);
        btnCodeAnimator.setOnClickListener(this);

        ivMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 使用XML视图动画
            case R.id.btnXMLAnimation:
                AnimationSet setXML = (AnimationSet) AnimationUtils.loadAnimation(MainActivity.this, R.anim.animation_set);
                v.startAnimation(setXML);
                break;
            // 使用Code视图动画
            case R.id.btnCodeAnimation:
                AnimationSet setCode = new AnimationSet(true);
                TranslateAnimation taRight = new TranslateAnimation(0, 100, 0, 0);
                TranslateAnimation taDown = new TranslateAnimation(0, 0, 0, 100);

                taRight.setDuration(1000);
                taDown.setStartOffset(1000);
                taDown.setDuration(1000);
                setCode.addAnimation(taRight);
                setCode.addAnimation(taDown);

                v.startAnimation(setCode);
                break;
            // 使用XML属性动画
            case R.id.btnXMLAnimator:
                AnimatorSet animatorSetXML = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.animator_set);
                animatorSetXML.setTarget(v);
                animatorSetXML.start();
                break;
            // 使用Code属性动画
            case R.id.btnCodeAnimator:
                AnimatorSet animatorSetCode = new AnimatorSet();
                animatorSetCode.setDuration(1000);
                animatorSetCode.playSequentially(ObjectAnimator.ofFloat(v, "translationX", 0, 100),
                        ObjectAnimator.ofFloat(v, "translationY", 0, 100),
                        ObjectAnimator.ofFloat(v, "translationY", 100, 0),
                        ObjectAnimator.ofFloat(v, "translationX", 100, 0));
                animatorSetCode.start();
                break;
            case R.id.ivMove:
                AnimatorSet imgAnimatorSet = new AnimatorSet();
                imgAnimatorSet.setDuration(1000);
                imgAnimatorSet.setStartDelay(1000);
                imgAnimatorSet.playSequentially(ObjectAnimator.ofFloat(v, "rotationY", 0, 180),
                        ObjectAnimator.ofFloat(v, "rotationY", 180, 0));
                imgAnimatorSet.start();
                break;
        }
    }
}
