package com.yy.imagepicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yy.imagepicker.LoadImageDelegate;
import com.yy.imagepicker.NormalLoadImage;
import com.yy.imagepicker.R;
import com.yy.imagepicker.utils.ScreenUtils;
import com.yy.imagepicker.view.touchview.ImageViewTouch;
import com.yy.imagepicker.view.touchview.ImageViewTouchBase;

/**
 * Created by cyy on 2016/7/6.
 *
 *
 */
public class SinglePreviewImageFragment extends Fragment{

    private String url;

    private LoadImageDelegate delegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(PreviewImageFragment.PREVIEW_IMAGE_URL);

        delegate = new NormalLoadImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageViewTouch imageView = new ImageViewTouch(this.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setId(R.id.preview_image);
        imageView.setImageResource(R.mipmap.pic_default);
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        return imageView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageViewTouch imageView = (ImageViewTouch) view.findViewById(R.id.preview_image);
        ScreenUtils.Size size = ScreenUtils.getSceenSize(this.getContext());
        delegate.loadImageBigger(imageView , url , size.width ,size.height );
    }
}
