package com.yy.imagepicker.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.yy.imagepicker.R;
import com.yy.imagepicker.fragment.CropFragment;

public class CropActivity extends AppCompatActivity {

    protected FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_crop);
        initView();

        CropFragment fragment = new CropFragment();
        fragment.setArguments(getIntent().getExtras());
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.container ,fragment ).commit();
    }

    private void initView() {
        container = (FrameLayout) findViewById(R.id.container);
    }
}
