package com.jikexueyuan.fragmentflip3d;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dej on 2016/10/11.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        rootView.findViewById(R.id.btnGoNext).setOnClickListener(this);
        return rootView;
    }

    /**
     * 下一个Fragment 对setCustomAnimations进行配置 使用XML设置Fragment切换时的属性动画
     * @param v
     */
    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.another_enter, R.animator.home_exit, R.animator.home_enter, R.animator.another_exit)
                .addToBackStack("AnotherFragment")
                .replace(R.id.rootView, new AnotherFragment())
                .commit();
    }
}
