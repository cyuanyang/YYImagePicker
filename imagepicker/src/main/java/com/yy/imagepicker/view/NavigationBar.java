package com.yy.imagepicker.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.imagepicker.R;

/**
 * Created by cyy on 2016/7/5.
 *
 * 导航栏
 */
public class NavigationBar extends RelativeLayout implements View.OnClickListener{

    private TextView titleView;
    private ImageButton backBtn;
    private LinearLayout titleLayout;

    public NavigationBar(Context context) {
        this(context , null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.navi_layout , this);

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        titleView = (TextView) findViewById(R.id.title_view);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        titleLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_btn ){
            Context c = this.getContext();
            if (c instanceof Activity){
                ((Activity)c).finish();
            }
        }else if(v.getId() == R.id.title_layout){
            if (mListener !=null)mListener.onTitleAction(v);
        }
    }

    private OnTitleActionListener mListener;
    public void setmListener(OnTitleActionListener l){
        mListener = l;
    }
    public interface OnTitleActionListener{
        void onTitleAction(View view);
    }
}
