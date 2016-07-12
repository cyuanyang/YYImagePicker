package com.yy.imagepicker.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by cyy on 2016/7/5.
 * model for grid view
 */
public class ImageItem implements Comparable<ImageItem> , Serializable{
    private File file;
    /** 图片的名字*/
    private String imageName;
//    /** 这张图片所在的文件夹目录*/
    private String parentDir;

    private String imagePath;

    private long lastModified = -1;
    /***图片占用内存大小 单位KB**/
    private long imageSize = -1;

    private boolean select = false;

    /**
     * 设置图片文件
     * @param imageFile 图片文件
     */
    public void setImageFile(File imageFile){
        this.file = imageFile;
        this.lastModified = imageFile.lastModified();
    }

    public String getImagePath() {
        if (imagePath == null)imagePath = file.getAbsolutePath();
        return imagePath;
    }

    public String getParentDir(){
        if (parentDir==null)parentDir = file.getParent();
        return parentDir;
    }

    public String getImageName() {
        if (imageName == null)imageName = file.getName();
        return imageName;
    }

    public long getImageSize(){
        if (imageSize == -1)imageSize = file.length()/1024;
        return imageSize;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int compareTo(ImageItem another) {
        if (this.lastModified > another.lastModified){
            return -1;
        }else if(this.lastModified < another.lastModified){
            return 1;
        }
        return 0;
    }

}
