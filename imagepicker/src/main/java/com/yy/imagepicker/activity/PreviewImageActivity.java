package com.yy.imagepicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.imagepicker.PickerImage;
import com.yy.imagepicker.R;
import com.yy.imagepicker.fragment.PreviewImageFragment;
import com.yy.imagepicker.model.ImageItem;
import com.yy.imagepicker.view.ToggleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreviewImageActivity extends AppCompatActivity implements
        View.OnClickListener, ToggleView.OnSelectedChangedListener,
        PreviewImageFragment.PagerChangedDelegate ,PreviewImageFragment.OnFragmentViewCreated{

    public final static String PREVIEW_IMAGES = "PreviewImageActivity.PREVIEW_IMAGES";
    public final static String CURRENT_PREVIEW_POSITION = "PreviewImageActivity.CURRENT_PREVIEW_POSITION";
    public final static String PREVIEW_SELECTED_IMAGES = "PreviewImageActivity.PREVIEW_SELECTED_IMAGES";

    protected FrameLayout previewContainer;
    protected ImageButton backBtn;
    protected TextView positionView;
    protected ToggleView toggleView;
    protected Button completeAction;

    /*****----------------------------------********/
    private int imageCount; //在fragment加载完毕后初始化这个值
    private int currentPosition = 0 ;
    /** position **/
    private Set<Integer> pickedPositionSet = new HashSet<>(PickerImage.getInstance().Max_For_Multuple);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_preview_image);

        initView();

        Bundle bundle = getIntent().getExtras();
//        currentPosition = bundle.getInt(CURRENT_PREVIEW_POSITION);
        ArrayList<Integer> pickedImgaeList = bundle.getIntegerArrayList(PREVIEW_SELECTED_IMAGES);
        pickedPositionSet.addAll(pickedImgaeList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setPagerChangedDelegate(this);
        fragment.setCallback(this);
        fragment.setArguments(bundle);
        transaction.add(R.id.preview_container, fragment);
        transaction.commit();
    }

    private void initView() {
        previewContainer = (FrameLayout) findViewById(R.id.preview_container);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        positionView = (TextView) findViewById(R.id.position_view);
        toggleView = (ToggleView) findViewById(R.id.toggle_view);
        toggleView.setOnSelectedChangedListener(this);
        completeAction = (Button) findViewById(R.id.complete_action);
        completeAction.setOnClickListener(PreviewImageActivity.this);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {

        } else if (view.getId() == R.id.complete_action) {
            ArrayList<Integer> res = new ArrayList<>(pickedPositionSet);
            Intent intent = new Intent();
            intent.putIntegerArrayListExtra(PREVIEW_SELECTED_IMAGES , res);
            this.setResult(RESULT_OK , intent);
            this.finish();
        }
    }

    @Override
    public void onSelectedChanged(View v, boolean select) {
        if (select){
            if (pickedPositionSet.size() >= PickerImage.getInstance().Max_For_Multuple){
                Toast.makeText(this , "最多添加"+PickerImage.getInstance().Max_For_Multuple+"张" ,Toast.LENGTH_SHORT).show();
                toggleView.select(false);
                return;
            }
            pickedPositionSet.add(currentPosition);
        }else {
            if (pickedPositionSet.contains(currentPosition))
            pickedPositionSet.remove(currentPosition);
        }
    }

    @Override
    public void onPageSelected(int position , ImageItem imageItem) {
        currentPosition = position;
        setImagePosView();
        if (pickedPositionSet.contains(currentPosition)){
            toggleView.select(true);
        }else {
            toggleView.select(false);
        }
//        toggleView.select(imageItem.isSelect());
    }

    @Override
    public void onFragmentViewCreated(PreviewImageFragment fragment) {
        currentPosition = fragment.getCurrentPosition();
        toggleView.select(fragment.getCurrentImageItem().isSelect());
        imageCount = fragment.getImageCount();
        setImagePosView();
    }

    private void setImagePosView(){
        positionView.setText((currentPosition+1)+"/"+imageCount);
    }
}
