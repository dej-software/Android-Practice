package com.jikexueyuan.fragmentflip3d;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 提交HomeFragment
        getFragmentManager().beginTransaction().add(R.id.rootView, new HomeFragment()).commit();
    }
}
