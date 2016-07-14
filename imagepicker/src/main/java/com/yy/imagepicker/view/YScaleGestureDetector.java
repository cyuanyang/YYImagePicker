package com.yy.imagepicker.view;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by chenyuanyang on 7/14/16.
 *
 * 继承 ScaleGestureDetector 处理缩放
 * 拿到触摸的两个点
 */
public class YScaleGestureDetector extends ScaleGestureDetector {

    private boolean isCockwise ;

    private Point preA , preB; //a 是x小的

    private boolean firstMove = true;

    YScaleGestureListener mListener;

    public YScaleGestureDetector(Context context, YScaleGestureListener listener) {
        super(context, listener);

        this.mListener = listener;
    }


    /**
     * 只是旋转的时候用
     */
    public static class YScaleGestureListener extends SimpleOnScaleGestureListener{


        public void onDown(Point a , Point b){

        }

        public void onMove(MotionEvent event){

        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return super.onScale(detector);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_POINTER_DOWN:
                //只检测两个手指
                if (event.getActionIndex() > 1)break;
                //确定x较小的
                if (event.getX(0) > event.getX(1)){
                    preA = new Point((int)event.getX(1) , (int)event.getY(1));
                     preB = new Point((int)event.getX(0) , (int)event.getY(0));
                }else {
                    preA = new Point((int)event.getX(0) , (int)event.getY(0));
                    preB = new Point((int)event.getX(1) , (int)event.getY(1));
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (isInProgress() && firstMove){
                    Point ca ,cb;
                    if (event.getX(0) > event.getX(1)){
                        ca = new Point((int)event.getX(1) , (int)event.getY(1));
                        cb = new Point((int)event.getX(0) , (int)event.getY(0));
                    }else {
                        ca = new Point((int)event.getX(0) , (int)event.getY(0));
                        cb = new Point((int)event.getX(1) , (int)event.getY(1));
                    }
                    int downd = preA.y - preB.y;
                    int cd = ca.y - cb.y;

                    if (cd < downd){
                        isCockwise = true;
                        Log.e("ff" , "shu");
                    }else {
                        isCockwise = false;
                        Log.e("ff" , "ni");
                    }

                    preA = ca;
                    preB = cb;
                }

                break;

        }

        return super.onTouchEvent(event);


    }

    public boolean getIsClockWise(){
        return isCockwise;
    }
}
