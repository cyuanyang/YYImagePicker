package com.yy.imagepicker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.yy.imagepicker.FileFilter;
import com.yy.imagepicker.NormalLoadImage;
import com.yy.imagepicker.PickerImage;
import com.yy.imagepicker.R;
import com.yy.imagepicker.activity.CropActivity;
import com.yy.imagepicker.activity.PreviewImageActivity;
import com.yy.imagepicker.adapter.GridAdapter;
import com.yy.imagepicker.model.DirPopModel;
import com.yy.imagepicker.model.ImageItem;
import com.yy.imagepicker.utils.SearchImageHelper;
import com.yy.imagepicker.utils.ToastHelper;
import com.yy.imagepicker.view.BottomView;
import com.yy.imagepicker.view.DirPopView;
import com.yy.imagepicker.view.NavigationBar;
import com.yy.imagepicker.view.ToggleView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cyy on 2016/7/4.
 *
 */
public class ImagesGridFragment extends BaseFragment implements
        SearchImageHelper.CallBack , AdapterView.OnItemClickListener
         ,GridAdapter.OnSelectedChagedDelegate , View.OnClickListener ,
        NavigationBar.OnTitleActionListener , DirPopView.OnDirChangedDelegate{

    private static final int PREVIEW_IMAGE_REQUEST_CODE = 0x1221;

    /**所有包含图片的文件夹*/
    protected List<DirPopModel> mImagesDir = new ArrayList<>();
    /** grid view 当前显示的图片所在的文件夹  默认显示图片数量最多的*/
    protected File currentImageDir;
    /** currentImageDir 对应的数量 */
    protected int imageCount;

    private NavigationBar navigationBar;
    private GridView mainGridView;
    private BottomView bottomLayout;
    private Button completeAction;
    private DirPopView dirPopView;

    private GridAdapter adapter;
    private List<ImageItem> imagesItem;
    private List<ImageItem> tempList ;

    /**选择的图片的数量 切换文件夹后也是这个 这个不变 ***/
    private Set<ImageItem> pickedImageItem = new HashSet<>(PickerImage.getInstance().Max_For_Multuple); //选择的images
    /***key > 文件夹的绝对路径  value 选择的image在数组中的位置***/
    private HashMap<String , Set<Integer>> pickedDirMap;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                //设置文件夹列表的数据
                date2DirPopView();
                date2View();
            }
        }
    };

    private void date2DirPopView(){
        dirPopView.setListViewData(mImagesDir);
        dirPopView.setOnDirChangedDelegate(this);
    }

    private void countSelectNumInDir(){
        dirPopView.selectCountChanged(pickedDirMap);
    }

    private void date2View(){
        if (imagesItem == null)imagesItem  = new ArrayList<>();
        imagesItem.clear();
        imagesItem.addAll(tempList);
        tempList.clear();
        //查数据 切换文件夹时的
        if (imagesItem.size() >0 && pickedDirMap!=null){
            Set<Integer> obj = pickedDirMap.get(imagesItem.get(0).getParentDir());
            if (obj!=null){
                for (Integer i : obj){
                    imagesItem.get(i).setSelect(true);
                }
            }
        }
        if (adapter == null){
            adapter = new GridAdapter
                    (ImagesGridFragment.this.getContext() , imagesItem , new NormalLoadImage());
            mainGridView.setAdapter(adapter);
            adapter.setPickDelegate(this);
        }else {
            adapter.notifyDataSetChanged();
        }
        //底部数字
        bottomLayout.setImageCount(imageCount);
        //当前文件夹的名字
        String imgDir = currentImageDir.getAbsolutePath();
        String dirName = imgDir.substring(imgDir.lastIndexOf("/")+1, imgDir.length());
        navigationBar.setTitle(dirName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.grid_layout, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainGridView = (GridView) view.findViewById(R.id.main_gridView);
        bottomLayout = (BottomView) view.findViewById(R.id.bottom_layout);
        completeAction = (Button) view.findViewById(R.id.complete_action);
        navigationBar = (NavigationBar) view.findViewById(R.id.navigation_bar);
        navigationBar.setmListener(this);
        dirPopView = (DirPopView) view.findViewById(R.id.pop_container);
        if (PickerImage.getInstance().Pick_Mode == PickerImage.PICK_MODE_Multiple){
            completeAction.setOnClickListener(this);
        }else {
            completeAction.setVisibility(View.GONE);
        }

        dealSGridScroll();

        SearchImageHelper.newInstance(this).search(this.getContext());

        mainGridView.setOnItemClickListener(this);
    }

    /**
     * 处理grid view的滚动事件
     */
    private void dealSGridScroll(){
        mainGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    //加载图片
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 将数据放入临时的list
     * @param currentDir File
     */
    private void setCurrentImageDir(File currentDir){
        this.currentImageDir = currentDir;
        ///根据这个文件夹所包含的图片文件初始化model
        if (tempList == null)tempList = new ArrayList<>();
        tempList.clear();
        File[] files  = currentImageDir.listFiles(new FileFilter());
        for (File file : files) {
            ImageItem imageItem = new ImageItem();
            imageItem.setImageFile(file);
            tempList.add(imageItem);
        }
        //排序
        Collections.sort(tempList);
    }

    private void removePickedImage(ImageItem imageItem , int pos){
        pickedImageItem.remove(imageItem);
        //移除map中的位置
        if (pickedDirMap !=null){
            String dirPath = imageItem.getParentDir();
            if (pickedDirMap.containsKey(dirPath)){
                Set<Integer> pickedPos = pickedDirMap.get(dirPath);
                if (pickedPos.contains(pos)){
                    pickedPos.remove(pos);
                }
            }
        }
    }
    /**
     * 添加或者移除选择的图片的位置 注意：只是一个位置参数 这个位置是在数组@Link(imagesItem)中的位置
     * @param pos imagesItem  在这个数组中的位置
     * @param select 选择或者移除
     */
    public void pickChanged(Integer pos , boolean select){
        if (select){
            //selected
            pickedImageItem.add(imagesItem.get(pos));
        }else {
            //当前文件夹
            if (pickedImageItem.contains(imagesItem.get(pos))){
                removePickedImage(imagesItem.get(pos) , pos);
            }else{
                ///这个地方逻辑有点复杂
                //先确定当前文件夹有没有选择照片 还有可能切换后 虽然位置一样但是实例不一样 选择比较名字
                if (imagesItem.size() >0 && pickedDirMap!=null){
                    Set<Integer> obj = pickedDirMap.get(imagesItem.get(0).getParentDir());
                    if (obj!=null){//当前文件夹选择了照片
                        //遍历选择的image 确定是否有名字相同的 则标记为相同的image
                        ImageItem equipImageItem = null;
                        for (ImageItem imageItem : pickedImageItem){
                            if (imageItem.getImageName().equals(imagesItem.get(pos).getImageName())){
                                equipImageItem = imageItem;
                                break;
                            }
                        }
                        if (equipImageItem!=null){
                            removePickedImage(equipImageItem , pos);
                        }
                    }
                }
            }
        }
        if (pickedImageItem.size()==0){
            completeAction.setText("完成");
        }else
        completeAction.setText("完成("+pickedImageItem.size()+")");
    }

    public ArrayList<ImageItem> getPickedListByPos(int pos){
        ArrayList<ImageItem> pickedList = new ArrayList<>();
        pickedList.add(imagesItem.get(pos));
        return pickedList;
    }

    /**
     * 得到选择的图片在当前数据源的位置
     */
    public ArrayList<Integer> getPickPos(){
        Set<Integer> positions = new HashSet<>(pickedImageItem.size());
        for (ImageItem imageItem:pickedImageItem){
            if (imagesItem.contains(imageItem))
            positions.add(imagesItem.indexOf(imageItem));
        }
        //map 文件夹中的
        if (pickedDirMap!=null && imagesItem.size()>0){
            String dirPath = imagesItem.get(0).getParentDir();
            if (pickedDirMap.containsKey(dirPath)){
                positions.addAll(pickedDirMap.get(dirPath));
            }
        }
        return new ArrayList<>(positions);
    }

    @Override
    public void externalStorageError() {
        ToastHelper.show("外部 存状态不可用" , this.getActivity());
    }


    /**
     * 图片数据加载完毕后调用
     * Callback by SearchImageHelper
     */
    @Override
    public void complete(List<DirPopModel> imagesDir, int max, File maxDir) {
        //加载完成后回调 默认把图片数量最多的一个文件夹设置为当前要显示图片的文件夹
        setCurrentImageDir(maxDir);
        imageCount = max;
        mImagesDir.addAll(imagesDir);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (PickerImage.getInstance().Pick_Mode == PickerImage.PICK_MODE_SINGLE){
            PickerImage.getInstance().pickedComplete(getPickedListByPos(position));
            this.getActivity().finish();
        }else if(PickerImage.getInstance().Pick_Mode == PickerImage.PICK_MODE_CROP){
            //裁剪
            Intent intent = new Intent(this.getActivity() , CropActivity.class);
            intent.putExtra(CropFragment.CROP_IMAGE_PATH ,imagesItem.get(position).getImagePath() );
            startActivity(intent);
        }
        else {
            //跳转到预览
            Bundle bundle = new Bundle();
            //将数据源传入
            bundle.putSerializable(PreviewImageActivity.PREVIEW_IMAGES , (Serializable)imagesItem);
            //当前选择的位置
            bundle.putInt(PreviewImageActivity.CURRENT_PREVIEW_POSITION ,position);
            //选择的图片
            bundle.putIntegerArrayList(PreviewImageActivity.PREVIEW_SELECTED_IMAGES ,getPickPos());
            Intent intent = new Intent(this.getActivity() , PreviewImageActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent ,PREVIEW_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PREVIEW_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //
            if (data!=null){
                //最新选取的先把以前的前部清除
                List<ImageItem> containItem = new ArrayList<>();
                for (ImageItem imageItem:pickedImageItem){
                    if (imagesItem.contains(imageItem)){
                        imageItem.setSelect(false);
                        containItem.add(imageItem);
                    }
                }
                //遍历当前文件夹map中的item
                if (pickedDirMap!=null&&imagesItem.size()>0){
                    if (pickedDirMap.containsKey(imagesItem.get(0).getParentDir())){
                        Set<Integer> poss = pickedDirMap.get(imagesItem.get(0).getParentDir());
                        for (Integer i:poss){
                            String imageName = imagesItem.get(i).getImageName();
                            imagesItem.get(i).setSelect(false);
                            for (ImageItem image:pickedImageItem){
                                if (image.getImageName().equals(imageName)){
                                    containItem.add(image);
                                }
                            }
                        }
                    }
                }
                for (ImageItem imageItem :containItem ){
                    removePickedImage(imageItem , imagesItem.indexOf(imageItem));
                }

                ArrayList<Integer> tempImages = data.getIntegerArrayListExtra(PreviewImageActivity.PREVIEW_SELECTED_IMAGES);
                for (Integer i : tempImages){
                    ImageItem imageItem = imagesItem.get(i);
                    imageItem.setSelect(true);
                    pickedImageItem.add(imageItem);
                }
                adapter.notifyDataSetChanged();

                if (pickedImageItem.size()==0){
                    completeAction.setText("完成");
                }else
                    completeAction.setText("完成("+pickedImageItem.size()+")");
            }
        }
    }

    @Override
    public void onPickedChanged(ToggleView toggleView ,ImageItem imageItem, int position , boolean select) {
        if (select && pickedImageItem.size() >= PickerImage.getInstance().Max_For_Multuple){
            Toast.makeText(this.getContext() , "最多选取"+PickerImage.getInstance().Max_For_Multuple+"张" , Toast.LENGTH_SHORT).show();
            toggleView.select(false);
            return;
        }
        imageItem.setSelect(select);
        adapter.notifyDataSetChanged();
        pickChanged(position , select);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.complete_action){
            PickerImage.getInstance().pickedComplete(new ArrayList<>(pickedImageItem));
            this.getActivity().setResult(Activity.RESULT_OK);
            this.getActivity().finish();
        }
    }

    @Override
    public void onTitleAction(View view) {
        if (dirPopView.isShow()){
            dirPopView.dissmiss();
        }else {
            dirPopView.show();
        }
    }

    /**
     * 当切换的图片的文件夹目录是调用
     * Callback by DirPopView
     * @param model DirPopModel
     */
    @Override
    public void onDirChanged(DirPopModel model) {
        //
        if (pickedDirMap == null)pickedDirMap = new HashMap<>();
        for (ImageItem imageItem : pickedImageItem){
            if (!imagesItem.contains(imageItem))
                continue;

            String parentPath = imageItem.getParentDir();
            Set<Integer> obj = pickedDirMap.get(parentPath) == null ? new HashSet<Integer>():pickedDirMap.get(parentPath);
            obj.add(imagesItem.indexOf(imageItem));
            pickedDirMap.put(parentPath , obj);
        }
        //替换数据
        setCurrentImageDir(new File(model.getImagePath()).getParentFile());
        date2View();

        countSelectNumInDir();
    }
}
