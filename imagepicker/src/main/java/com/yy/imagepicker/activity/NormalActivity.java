package com.yy.imagepicker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.yy.imagepicker.R;
import com.yy.imagepicker.fragment.ImagesGridFragment;

/**
 * Created by cyy on 2016/7/5.
 */
public class NormalActivity extends AppCompatActivity {

    protected FrameLayout container;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_normal);

        if (getActionBar() != null) {
            getActionBar().hide();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initView();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.container, new ImagesGridFragment());
        ft.commit();


    }

    private void initView() {
        container = (FrameLayout) findViewById(R.id.container);
    }
}
