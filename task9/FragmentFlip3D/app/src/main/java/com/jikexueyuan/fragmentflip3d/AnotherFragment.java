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
public class AnotherFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_another, null);
        rootView.findViewById(R.id.btnBack).setOnClickListener(this);
        return rootView;
    }

    /**
     * 返回键
     * @param v
     */
    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}
