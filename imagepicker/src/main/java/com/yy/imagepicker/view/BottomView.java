package com.yy.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.imagepicker.R;

/**
 * Created by cyy on 2016/7/5.
 */
public class BottomView extends RelativeLayout {
    protected TextView imageSizeView;

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.bottom_layout, this);
        imageSizeView = (TextView) findViewById(R.id.image_size_view);
    }

    public void setImageCount(int count){
        imageSizeView.setText(String.valueOf(count));
    }

}
