package com.yy.imagepicker;

import com.yy.imagepicker.utils.ImageLoader;

/**
 * Created by thinkpad on 2016/7/4.
 */
public class Attach {

    public static boolean DEBUG = true;

    /**
     * 加载图片是的线程数量
     */
    public static int ThreadNumber = 2;

    /**
     * 调度方式
     */
    public static ImageLoader.Type Type = ImageLoader.Type.LIFO;
}
