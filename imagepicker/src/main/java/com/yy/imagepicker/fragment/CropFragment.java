package com.yy.imagepicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yy.imagepicker.LoadImageDelegate;
import com.yy.imagepicker.NormalLoadImage;
import com.yy.imagepicker.R;
import com.yy.imagepicker.utils.ScreenUtils;
import com.yy.imagepicker.view.ScaleImageView;
import com.yy.imagepicker.view.YTouchImageView;

/**
 * Created by cyy on 2016/7/8.
 *
 */
public class CropFragment extends BaseFragment {

    public final static String CROP_IMAGE_PATH = "CropFragment.CROP_IMAGE_PATH";

    protected YTouchImageView originalImageView;
    private String imagePath;

    private LoadImageDelegate loadImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePath = getArguments() != null ? getArguments().getString(CROP_IMAGE_PATH) : null;
        loadImage = new NormalLoadImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.crop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        loadImage.loadImageBigger(
                originalImageView ,
                imagePath ,
                ScreenUtils.getSceenSize(this.getContext()).width ,
                ScreenUtils.getSceenSize(this.getContext()).height);
    }

    private void initView(View view) {
        originalImageView = (YTouchImageView) view.findViewById(R.id.original_imageView);
    }
}
