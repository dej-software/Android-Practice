package com.jikexueyuan.baiduwaimai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dej on 2016/10/5.
 */
public class MeFragment extends Fragment {

    // 用来放置“我的”相关列表 对应fragment_me.xml中的R.id.ll_layout
    private LinearLayout layout;

    private int[] images = new int[]{
            R.drawable.s_location, R.drawable.s_daijinjuan, R.drawable.s_refund,
            R.drawable.s_messages, R.drawable.s_star, R.drawable.s_comment,
            R.drawable.s_balance, R.drawable.s_nuomi, R.drawable.s_problem};
    private String[] texts = new String[]{
            "我的送餐地址", "我的代金券", "我的退款",
            "我的消息", "我的收藏", "我的评论",
            "百度钱包", "百度糯米", "常见问题"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);

        layout = (LinearLayout) rootView.findViewById(R.id.ll_layout);

        initScrollView();

        return rootView;
    }

    // 初始化ScrollView
    private void initScrollView() {
        for (int i = 0; i < images.length; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.me_list_cell, layout, false);
            ImageView iv = (ImageView) view.findViewById(R.id.ivMeList);
            iv.setImageResource(images[i]);

            TextView tv = (TextView) view.findViewById(R.id.tvMeList);
            tv.setText(texts[i]);

            if (i == 3 || i == 6 || i == 8) {
                View spaceView = LayoutInflater.from(getActivity()).inflate(R.layout.me_list_space, layout, false);
                layout.addView(spaceView);
            }

            layout.addView(view);
        }
        layout.addView(LayoutInflater.from(getActivity()).inflate(R.layout.me_list_space, layout, false));
    }
}
