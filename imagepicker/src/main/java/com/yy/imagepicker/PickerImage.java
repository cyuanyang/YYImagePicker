package com.yy.imagepicker;

import com.yy.imagepicker.model.ImageItem;

import java.util.List;

/**
 * Created by cyy on 2016/7/5.
 *
 *
 */
public class PickerImage {

    public final static int PICK_MODE_SINGLE = 0x0001;
    public final static int PICK_MODE_Multiple = 0x0002;
    public final static int PICK_MODE_CROP = 0x0003;

    public int Pick_Mode = PICK_MODE_SINGLE;

    public int Max_For_Multuple = 9;

    private static PickerImage pickerImage;
    private PickerImage(){}

    public static PickerImage getInstance(){
        if (pickerImage==null){
            synchronized (PickerImage.class){
                if (pickerImage==null){
                    pickerImage=new PickerImage();
                }
            }
        }
        return pickerImage;
    }

    public void pickedComplete(List<ImageItem> pickedItems){
        if (pickedCompleteListener!=null)pickedCompleteListener.picked(pickedItems);
    }

    private PickedCompleteListener pickedCompleteListener;
    public void setPickedCompleteListener(PickedCompleteListener l){
        this.pickedCompleteListener = l;
    }

    public interface PickedCompleteListener{
        void picked(List<ImageItem> pickedItems);
    }

}
