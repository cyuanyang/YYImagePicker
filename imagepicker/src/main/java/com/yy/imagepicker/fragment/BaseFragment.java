package com.yy.imagepicker.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by cyy on 2016/7/4.
 * 初始化数据
 * 遍历文件夹里面的图片
 */
public class BaseFragment extends Fragment {

    /**   存放图片所在的文件夹   */


    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
