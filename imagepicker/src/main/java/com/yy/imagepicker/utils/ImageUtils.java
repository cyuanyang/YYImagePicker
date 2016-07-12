package com.yy.imagepicker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by cyy on 2016/7/4.
 * 主要是对大图片处理成需要的小图片
 */
public class ImageUtils {

    public static Bitmap decodeSampleImageFromUri(String path , int reqWidth , int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path , options);
        options.inSampleSize = calculateSampleImageSize(options , reqWidth , reqHeight);
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(path , options);
    }

    /**
     * 大图到小图的尺寸
     * @return f
     */
    public static int calculateSampleImageSize(BitmapFactory.Options options , int reqWidth , int reqHeight){
        int width = options.outWidth ;
        int height = options.outHeight;
        int inSampleScale = 1;
        if (width > reqWidth && height > reqHeight){
            int widthRadio = Math.round(width*1.0f/reqHeight);
            int heightRadio = Math.round(height*1.0f/reqHeight);
            inSampleScale = Math.max(widthRadio , heightRadio);
        }
        return inSampleScale;
    }


}
