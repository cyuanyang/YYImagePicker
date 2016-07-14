package com.yy.imagepicker.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
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

    private final static int SCALEING = 1; //正在缩放
    private final static int NORMAL = 0;//啥也没干
    private final static int TRABSLATE =2;//正在平移

    private ScaleGestureDetector mScaleGestureDetector ;
    private GestureDetector mGestureDetector;

    private float deltaDegree = 0 ;
    private float preDegree = 0 ;

    private Matrix mBaseMatrix = new Matrix(); //图片的矩阵

    private Matrix mSuppMatrix = new Matrix(); //记录上此次操作的

    private Matrix mDisplayMatrix = new Matrix();

    private boolean isSizeChanged = false;

    private int mState = NORMAL;//状态
    /////////////////////////////////////////////////
    private int viewWidth , viewHeight; //imageView 的宽高
    private int imageWidth , imagHeight;// 图片的宽高

    ////////////////////////////////////////////////
    private float doubleTapScale = 4.0f;

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
        mScaleGestureDetector = new ScaleGestureDetector
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

    protected float getTranslateX(Matrix matrix){
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MTRANS_X];
    }

    protected float getTranslateY(Matrix matrix){
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MTRANS_Y];
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
        mScaleGestureDetector.onTouchEvent(event);
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

    private void zoomTo(float targetScale , float centerX , float centerY ){
        if (targetScale > 10.0f){
            targetScale = 10.0f ;
        }
        if (targetScale < 0.5f){
            targetScale = 0.5f;
        }
        float delta = targetScale / getScale(mSuppMatrix);
        Log.i("/////", "postScale: " + delta + "");
        mSuppMatrix.postScale(delta , delta ,centerX , centerY);
        setImageMatrix(getDisplayMatrix());
    }

    private void translateTo(float deltaX , float deltaY){
        Matrix matrix = getDisplayMatrix();
        RectF rectF = getImageRect();
        if (deltaX >= 0){
            //向右移动
            if (deltaY >= 0){
                if (rectF.top >=0)deltaY = 0;
            }
            if (deltaY < 0){
                if (rectF.bottom <= viewHeight)deltaY = 0;
            }
            if (rectF.left>=0)deltaX = 0;
            mSuppMatrix.postTranslate(deltaX , deltaY);
            setImageMatrix(getDisplayMatrix());

            //移动之后矫正
            RectF rectFend = getImageRect();
            if (rectFend.left >= 0 ){
                mSuppMatrix.postTranslate(-rectFend.left ,0);
                setImageMatrix(getDisplayMatrix());
            }
        }
        //左滑
        if (deltaX < 0){
            if (deltaY >= 0){
                if (rectF.top >=0)deltaY = 0;
            }
            if (deltaY < 0){
                if (rectF.bottom < viewHeight) deltaY = 0;
            }
            if (rectF.right <= viewWidth)deltaX = 0;
            mSuppMatrix.postTranslate(deltaX , deltaY);
            setImageMatrix(getDisplayMatrix());
            RectF rectFend = getImageRect();
            if (rectFend.right <= viewWidth){
                mSuppMatrix.postTranslate(viewWidth-rectFend.right,0);
                setImageMatrix(getDisplayMatrix());
            }
        }
    }

    private void rotateTo(float degree , float centerX , float centerY){
        mSuppMatrix.postRotate(degree ,viewWidth/2 , viewHeight/2);
        setImageMatrix(getDisplayMatrix());
    }
    /**
     * 得到Bitmap的Rect
     */
    private RectF getImageRect(){
        Drawable drawable = getDrawable();
        if (drawable==null)return new RectF(0 , 0 , 0 ,0 );
        RectF rectF = new RectF(0 , 0 ,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
        Matrix m = getDisplayMatrix();
        m.mapRect(rectF);
        print("getImageRect==" + rectF);
        return rectF;
    }

    /**
     * 平移到中间
     */
    private void center(){

    }

    private Matrix getDisplayMatrix(){
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
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
            float targetScale = getScale(mSuppMatrix) * detector.getScaleFactor();
            zoomTo(targetScale , detector.getFocusX() , detector.getFocusY());
            center();

            float csx = detector.getCurrentSpanX();
            float csy = detector.getCurrentSpanY();
            float ca = (float) Math.toDegrees(Math.atan(csy/csx));

            float psx = detector.getPreviousSpanX();
            float psy = detector.getPreviousSpanY();
            float pa = (float) Math.toDegrees(Math.atan(psy/psx));

            print(ca +" pa==  " + pa);
            deltaDegree = ca - pa;


            Log.e("deltaDegree==" , ""+(deltaDegree));
//            if (Math.abs(deltaDegree) > 1){
                rotateTo(-deltaDegree , detector.getFocusX() , detector.getFocusY());
//            }
            //必须return true 否则认为该事件没有被消费 不能计算出准确的scale
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            deltaDegree = 0;
            mState = SCALEING;
            float csx = detector.getCurrentSpanX();
            float csy = detector.getCurrentSpanY();
            float a = (float) Math.toDegrees(Math.atan(csy/csx));
            preDegree = a;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mState = NORMAL;
            super.onScaleEnd(detector);
        }

    }

    /**
     * 普通手势检测
     */
    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float currentScale = getScale(mSuppMatrix);
            if (currentScale >= 10){
                zoomTo(1, e.getX() , e.getY());
            }else {
                zoomTo(doubleTapScale+currentScale, e.getX() , e.getY());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            print("onScroll");
            //非两个手指时执行
            if (e1==null || e2 == null)
                return false;
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1){
                return false;
            }
            if (mScaleGestureDetector.isInProgress())
                return false;
            translateTo(-distanceX , -distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }

}
