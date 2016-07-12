package com.yy.imagepicker.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by cyy on 2016/7/8.
 *
 *
 */
public class YTouchImageView extends ImageView implements View.OnTouchListener{

    private ScaleGestureDetector scaleGestureDetector ;
    private GestureDetector mGestureDetector;

    private double deltaDegree = 0 ;
    private double preDegree = 0 ;

    private Matrix mBaseMatrix = new Matrix(); //图片的矩阵

    private Matrix mSuppMatrix = new Matrix(); //记录上此次操作的

    private Matrix mDisplayMatrix = new Matrix();

    private boolean isSizeChanged = false;

    /////////////////////////////////////////////////
    private int viewWidth , viewHeight; //imageView 的宽高
    private int imageWidth , imagHeight;// 图片的宽高

    public YTouchImageView(Context context) {
        super(context);
        init();
    }

    public YTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YTouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if (this.getScaleType()!= ScaleType.MATRIX){
            setScaleType(ScaleType.MATRIX);
        }
        this.setOnTouchListener(this);
        mGestureDetector = new GestureDetector
                (this.getContext() , new GestureListener());
        scaleGestureDetector = new ScaleGestureDetector
                (this.getContext() , new ScaleGestureListener());
    }

    /**
     * 在改变的时候初始化他的高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isSizeChanged = true;
        print( "w == " + w + "h = "+ h);
        viewWidth = w;
        viewHeight = h;
        initImage();
    }

    private void initImage(){
        initBaseMatrix();
    }

    /**
     * 初始化base矩阵
     * 让图片居中放大显示 以宽度为基础呢
     *
     */
    private void initBaseMatrix(){
        mBaseMatrix.reset();
        float radioW = viewWidth * 1.0f / imageWidth;
        mBaseMatrix.postScale(radioW , radioW);

        float dy = viewHeight/2 - (imagHeight*radioW)/2;
        mBaseMatrix.postTranslate(0 ,dy);

        setImageMatrix(mBaseMatrix);
    }

    protected float getScale(Matrix matrix) {
        float[] mMatrixValues = new float[9];
        matrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }
    /**
     * 初始化加载的图片的宽高
     */
    private void initImageWidthAndHeight(){
        Drawable drawable = getDrawable();
        if (drawable !=null){
            imageWidth = drawable.getIntrinsicWidth();
            imagHeight = drawable.getIntrinsicHeight();
        }
        print(imageWidth +" height==" + imagHeight);
        if (isSizeChanged)initImage();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initImageWidthAndHeight();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initImageWidthAndHeight();
    }

    public Matrix getMatrix() {
        return super.getMatrix();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
    }


    /**
     * 旋转
     * @param degree 角度
     */
    private void rotate(float degree){
        Matrix matrix = new Matrix();
        matrix.setRotate( degree ,
                this.getDrawable().getIntrinsicWidth()/2 ,
                this.getDrawable().getIntrinsicHeight()/2);
        setImageMatrix(matrix);
    }

    private void scale(float scale){
        Matrix matrix = new Matrix();
        matrix.postScale(scale , scale);
    }

    private Matrix getDisplayMatrix(){
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        print(mSuppMatrix+"");
        return mDisplayMatrix;
    }

    private void print(String msg){
        Log.e("print" , msg);
    }

    /**
     * 缩放手势检测
     */
    class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float span = detector.getCurrentSpan() - detector.getPreviousSpan();
            float targetScale = getScale(mSuppMatrix) * detector.getScaleFactor();
            Log.e(">>>", detector.getScaleFactor()+"");
            if (targetScale > 10.0f){
                targetScale = 10.0f ;
            }
            if (targetScale < 0.5f){
                targetScale = 0.5f;
            }
            float delta = targetScale / getScale(mSuppMatrix);
            Log.i("/////", "postScale: " + delta + "");

            mSuppMatrix.postScale(delta , delta ,detector.getFocusX() , detector.getFocusY());

            setImageMatrix(getDisplayMatrix());



            float csx = detector.getCurrentSpanX();
            float csy = detector.getCurrentSpanY();
            double a = Math.toDegrees(Math.atan(csy/csx));

            deltaDegree += a - preDegree;

            preDegree = a;

            Log.e("deltaDegree==" , ""+(deltaDegree));
            //必须return true 否则认为该事件没有被消费 不能计算出准确的sacle
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            deltaDegree = 0;

            float csx = detector.getCurrentSpanX();
            float csy = detector.getCurrentSpanY();
            double a = Math.toDegrees(Math.atan(csy/csx));
            preDegree = a;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }

    }

    /**
     * 普通手势检测
     */
    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Matrix matrix = new Matrix();
            matrix.postScale(2f,2f);
            setImageMatrix(matrix);

            return super.onDoubleTap(e);
        }
    }


}
