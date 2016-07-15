package com.yy.yyimagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yy.imagepicker.PickerImage;
import com.yy.imagepicker.activity.NormalActivity;
import com.yy.imagepicker.model.ImageItem;
import com.yy.imagepicker.utils.ImageUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, PickerImage.PickedCompleteListener {

    protected Button singleBtn;
    protected LinearLayout selectedImagesContainer;
    protected Button multipleBtn;
    protected Button cropBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        PickerImage.getInstance().setPickedCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.single_btn) {
            PickerImage.getInstance().Pick_Mode = PickerImage.PICK_MODE_SINGLE;
            startActivity(new Intent(this, NormalActivity.class));
        } else if (view.getId() == R.id.multiple_btn) {
            PickerImage.getInstance().Pick_Mode = PickerImage.PICK_MODE_Multiple;
            startActivity(new Intent(this, NormalActivity.class));
        } else if (view.getId() == R.id.crop_btn) {
            PickerImage.getInstance().Pick_Mode = PickerImage.PICK_MODE_CROP;
            startActivity(new Intent(this, NormalActivity.class));
        }
    }

    private void initView() {
        singleBtn = (Button) findViewById(R.id.single_btn);
        singleBtn.setOnClickListener(this);
        selectedImagesContainer = (LinearLayout) findViewById(R.id.selected_images_container);
        multipleBtn = (Button) findViewById(R.id.multiple_btn);
        multipleBtn.setOnClickListener(this);
        cropBtn = (Button) findViewById(R.id.crop_btn);
        cropBtn.setOnClickListener(MainActivity.this);
    }

    private void addImageView(String path) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Bitmap bitmap = ImageUtils.decodeSampleImageFromUri(path, 200, 200);
        imageView.setImageBitmap(bitmap);
        selectedImagesContainer.addView(imageView, lp);
    }

    @Override
    public void picked(List<ImageItem> pickedItems) {
        for (ImageItem imageItem : pickedItems) {
            addImageView(imageItem.getImagePath());
        }
    }


    @Override
    protected void onDestroy() {
        PickerImage.getInstance().setPickedCompleteListener(null);
        super.onDestroy();
    }
}
