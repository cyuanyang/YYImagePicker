package com.yy.imagepicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yy.imagepicker.R;
import com.yy.imagepicker.activity.PreviewImageActivity;
import com.yy.imagepicker.adapter.PagerAdapter;
import com.yy.imagepicker.model.ImageItem;

import java.util.List;

/**
 * Created by cyy on 2016/7/6.
 *
 */
public class PreviewImageFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    public final static String PREVIEW_IMAGE_URL = "PreviewImageFragment.PREVIEW_IMAGE_URL";

    protected ViewPager viewPager;

    private int currentPosition ;
    private List<ImageItem> previewImages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previewImages = (List<ImageItem>) getArguments().getSerializable(PreviewImageActivity.PREVIEW_IMAGES);
        currentPosition = getArguments().getInt(PreviewImageActivity.CURRENT_PREVIEW_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager() , previewImages);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);

        if (callback!=null)callback.onFragmentViewCreated(this);
    }

    private void initView(View rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
    }

    private PagerChangedDelegate pagerChangedDelegate;
    public void setPagerChangedDelegate(PagerChangedDelegate delegate){
        this.pagerChangedDelegate = delegate;
    }
    public interface PagerChangedDelegate{
        void onPageSelected(int position , ImageItem imageItem);
    }

    private OnFragmentViewCreated callback;
    public void setCallback(OnFragmentViewCreated c){
        this.callback = c;
    }
    public interface OnFragmentViewCreated{
        void onFragmentViewCreated(PreviewImageFragment fragment);
    }

    @Override
    public void onDestroy() {
        viewPager.addOnPageChangeListener(null);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (this.pagerChangedDelegate != null)
            pagerChangedDelegate.onPageSelected(position , previewImages.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public ImageItem getCurrentImageItem(){
        return previewImages.get(currentPosition);
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public int getImageCount(){
        return previewImages.size();
    }

    public List<ImageItem> getPreviewImages(){
        return previewImages;
    }
}
