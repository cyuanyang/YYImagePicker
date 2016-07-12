package com.yy.imagepicker.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yy.imagepicker.fragment.PreviewImageFragment;
import com.yy.imagepicker.fragment.SinglePreviewImageFragment;
import com.yy.imagepicker.model.ImageItem;

import java.util.List;

/**
 * Created by cyy on 2016/7/6.
 *
 *
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<ImageItem> previewImages;
    public PagerAdapter(FragmentManager fm , List<ImageItem> previewImages) {
        super(fm);
        this.previewImages = previewImages;
    }

    @Override
    public Fragment getItem(int position) {
        SinglePreviewImageFragment fragment = new SinglePreviewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PreviewImageFragment.PREVIEW_IMAGE_URL , previewImages.get(position).getImagePath());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return previewImages.size();
    }
}
