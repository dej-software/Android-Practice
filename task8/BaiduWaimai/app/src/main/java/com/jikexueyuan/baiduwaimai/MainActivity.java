package com.jikexueyuan.baiduwaimai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jikexueyuan.baiduwaimai.gradientUI.GradientIconView;
import com.jikexueyuan.baiduwaimai.gradientUI.GradientTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;

    // Icon、Text列表 GradientIconView、GradientTextView实现滑动时颜色的渐变
    private List<GradientIconView> tabIconList = new ArrayList<>();
    private List<GradientTextView> tabTextList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTabs();
    }

    /**
     * 初始化View ：Fragment相关
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        // 三个页面
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new DingdanFragment();
                    case 2:
                        return new MeFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化底部的图标
     */
    private void initTabs() {
        // 底部的图标项
        GradientIconView iconHome = (GradientIconView) findViewById(R.id.iconHome);
        GradientIconView iconIndent = (GradientIconView) findViewById(R.id.iconIndent);
        GradientIconView iconMe = (GradientIconView) findViewById(R.id.iconMe);
        // 底部的字符项
        GradientTextView textHome = (GradientTextView) findViewById(R.id.tvHome);
        GradientTextView textIndent = (GradientTextView) findViewById(R.id.tvIndent);
        GradientTextView textMe = (GradientTextView) findViewById(R.id.tvMe);

        // 添加至List
        tabIconList.add(iconHome);
        tabIconList.add(iconIndent);
        tabIconList.add(iconMe);

        tabTextList.add(textHome);
        tabTextList.add(textIndent);
        tabTextList.add(textMe);

        // 设置点击监听
        iconHome.setOnClickListener(this);
        iconIndent.setOnClickListener(this);
        iconMe.setOnClickListener(this);

        textHome.setOnClickListener(this);
        textIndent.setOnClickListener(this);
        textMe.setOnClickListener(this);

        // 设置Home页为选中
        iconHome.setIconAlpha(1.0f);
        textHome.setTextViewAlpha(1.0f);
    }

    /**
     * 重置底部Tab的颜色
     */
    public void resetTabs() {
        for (int i = 0; i < tabIconList.size(); i++) {
            tabIconList.get(i).setIconAlpha(0);
        }
        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextViewAlpha(0);
        }
    }

    /**
     * Tab的点击处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        resetTabs();
        switch (v.getId()) {
            case R.id.iconHome:
            case R.id.tvHome:
                tabIconList.get(0).setIconAlpha(1.0f);
                tabTextList.get(0).setTextViewAlpha(1.0f);
                viewPager.setCurrentItem(0, false);
                break;

            case R.id.iconIndent:
            case R.id.tvIndent:
                tabIconList.get(1).setIconAlpha(1.0f);
                tabTextList.get(1).setTextViewAlpha(1.0f);
                viewPager.setCurrentItem(1, false);
                break;

            case R.id.iconMe:
            case R.id.tvMe:
                tabIconList.get(2).setIconAlpha(1.0f);
                tabTextList.get(2).setTextViewAlpha(1.0f);
                viewPager.setCurrentItem(2, false);
                break;
        }
    }

    /**
     * viewPager 页面的滑动处理
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            GradientIconView iconLeft = tabIconList.get(position);
            GradientIconView iconRight = tabIconList.get(position + 1);

            GradientTextView textLeft = tabTextList.get(position);
            GradientTextView textRight = tabTextList.get(position + 1);

            iconLeft.setIconAlpha(1 - positionOffset);
            textLeft.setTextViewAlpha(1 - positionOffset);
            iconRight.setIconAlpha(positionOffset);
            textRight.setTextViewAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
