package com.yy.imagepicker.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.yy.imagepicker.R;
import com.yy.imagepicker.view.touchview.graphics.FastBitmapDrawable;

/**
 * Created by cyy on 2016/7/6.
 */
public class ToggleView extends ImageView {

    private float max_sacle = 1.2f;

    private Drawable selectDrawable , unselectDrawable;

    private AnimationSet set;

    /***是否选中**/
    private boolean isSelected = false;

    public ToggleView(Context context) {
        this(context , null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        selectDrawable = new FastBitmapDrawable(BitmapFactory.decodeResource(this.getResources() ,R.mipmap.ic_picked));
        unselectDrawable = new FastBitmapDrawable(BitmapFactory.decodeResource(this.getResources() ,R.mipmap.ic_unpick));
    }

    private void init() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected){
                    ToggleView.this.setImageDrawable(unselectDrawable);
                    isSelected = false;
                }else{
                    ToggleView.this.setImageDrawable(selectDrawable);
                    isSelected = true;
                    biggerAnim();
                }
                if (mListener!=null)mListener.onSelectedChanged(v , isSelected);
            }
        });
    }

    public void select(boolean b){
        if (b){
            ToggleView.this.setImageResource(R.mipmap.ic_picked);
            isSelected = true;
        }else {
            ToggleView.this.setImageResource(R.mipmap.ic_unpick);
            isSelected = false;
        }
    }

    /**
     * 点击时放大动画
     */
    private void biggerAnim(){
        if (set==null){
            set = new AnimationSet(true);
            ScaleAnimation biggerAnimation = new ScaleAnimation
                    (1.0f ,max_sacle, 1.0f , max_sacle , ScaleAnimation.RELATIVE_TO_SELF ,0.5f ,ScaleAnimation.RELATIVE_TO_SELF , 0.5f );
            biggerAnimation.setDuration(100);
            set.addAnimation(biggerAnimation);

            ScaleAnimation reverseAnim = new ScaleAnimation
                    (max_sacle ,1.0f, max_sacle , 1.0f , ScaleAnimation.RELATIVE_TO_SELF ,0.5f ,ScaleAnimation.RELATIVE_TO_SELF , 0.5f );
            reverseAnim.setDuration(100);
            reverseAnim.setStartOffset(50);
            set.addAnimation(reverseAnim);

            set.setInterpolator(new AccelerateInterpolator());
            set.setStartOffset(100);
        }
        this.startAnimation(set);
    }



    private OnSelectedChangedListener mListener;
    public void setOnSelectedChangedListener(OnSelectedChangedListener listener){
        this.mListener = listener;
    }
    public interface OnSelectedChangedListener{
        void onSelectedChanged(View v , boolean select);
    }


}
