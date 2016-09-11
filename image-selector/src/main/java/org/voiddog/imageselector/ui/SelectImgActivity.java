package org.voiddog.imageselector.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.voiddog.imageselector.R;
import org.voiddog.imageselector.data.AlbumItemData;
import org.voiddog.imageselector.ui.adapter.AlbumAdapter;
import org.voiddog.imageselector.ui.adapter.AlbumViewHolder;
import org.voiddog.imageselector.ui.adapter.SelectImgAdapter;
import org.voiddog.imageselector.ui.layoutmanager.BounceGridLayoutManager;
import org.voiddog.lib.mvp.MvpActivity;
import org.voiddog.lib.ui.DrawableItemDecoration;
import org.voiddog.lib.util.SizeUtil;
import org.voiddog.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器
 * Created by qigengxin on 16/9/7.
 */
public class SelectImgActivity extends MvpActivity<SelectImgMvpView, SelectImgPresenter>
        implements SelectImgMvpView, SelectImgAdapter.OnSelectChangedListener{
    /**
     * return the select images
     * Type: {@link String}
     */
    public static final String SELECT_IMGS = "select_imgs";
    public static final String MAX_SELECT_COUNT = "max_select_count";

    public static Intent getStartIntent(Context context, int maxSelectCount){
        Intent intent = new Intent(context, SelectImgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MAX_SELECT_COUNT, maxSelectCount);
        return intent;
    }

    /**
     * 解析返回由当前activity on result的时候的数据
     * @param data on result 的时候的数据
     * @return 选择的图片数据列表 没有找到则返回null
     */
    public static List<String> getImageListFromIntent(Intent data){
        return data.getStringArrayListExtra(SELECT_IMGS);
    }

    // 当前文件夹
    TextView tvFolder;
    // 继续
    TextView tvContinue;
    RecyclerView rvImg, rvAlbum;
    Toolbar toolbar;

    SelectImgAdapter selectImageAdapter;
    AlbumAdapter albumAdapter;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;

    // Extra
    // 最多可以选择数
    int maxSelectCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        parseExtra();
        createLayout();
        initView();
        getPresenter().startScanImgs();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseExtra();
    }

    void parseExtra(){
        Intent intent = getIntent();
        maxSelectCount = intent.getIntExtra(MAX_SELECT_COUNT, maxSelectCount);
    }

    void createLayout(){
        setContentView(R.layout.dl_is_activity_select_img);
    }

    void initView(){
        tvFolder = (TextView) findViewById(R.id.dl_is_tv_folder);
        tvContinue = (TextView) findViewById(R.id.dl_is_tv_continue);
        rvImg = (RecyclerView) findViewById(R.id.dl_is_rv_img);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        selectImageAdapter = new SelectImgAdapter();
        selectImageAdapter.setOnSelectChangedListener(this);
        rvImg.setLayoutManager(new BounceGridLayoutManager(this, 4));
        rvImg.setAdapter(selectImageAdapter);
        DrawableItemDecoration itemDecoration = new DrawableItemDecoration(
                new ColorDrawable(Color.WHITE), DrawableItemDecoration.HORIZONTAL|DrawableItemDecoration.VERTICAL
        );
        itemDecoration.setWidth(SizeUtil.dp2px(this, 1));
        itemDecoration.setHeight(SizeUtil.dp2px(this, 1));
        rvImg.addItemDecoration(itemDecoration);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFinish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().showAlbumList();
            }
        });
    }

    void initAlbumDialog(){
        if(rvAlbum != null){
            return;
        }

        rvAlbum = new RecyclerView(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(rvAlbum, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        albumAdapter = new AlbumAdapter();
        rvAlbum.setAdapter(albumAdapter);
        rvAlbum.setLayoutManager(new LinearLayoutManager(this));
        albumAdapter.setOnSelectCallback(new AlbumViewHolder.OnSelectAlbumCallback() {
            @Override
            public void onSelectAlbum(AlbumItemData data) {
                bottomSheetDialog.dismiss();
                getPresenter().showAlbum(data);
            }
        });
    }

    @NonNull
    @Override
    public SelectImgPresenter createPresenter() {
        return new SelectImgPresenter(this);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.toastShort(this, msg);
    }

    @Override
    public void showImages(String title, List<String> imagePath) {
        tvFolder.setText(title);
        selectImageAdapter.setData(imagePath);
    }

    @Override
    public void showAlbums(List<AlbumItemData> albumItemDataList) {
        initAlbumDialog();
        albumAdapter.setData(albumItemDataList);
        bottomSheetDialog.show();
    }

    @Override
    public boolean selected(String path) {
        if(maxSelectCount == 1){
            selectImageAdapter.clearSelect();
            return true;
        }
        return selectImageAdapter.getSelectImages().size() < maxSelectCount;
    }

    @Override
    public boolean unSelected(String path) {
        return true;
    }

    /**
     * 选择结束 返回结果
     */
    void selectFinish(){
        ArrayList<String> selectImgList = new ArrayList<>();
        selectImgList.addAll(selectImageAdapter.getSelectImages());
        Intent data = new Intent();
        data.putStringArrayListExtra(SELECT_IMGS, selectImgList);
        setResult(RESULT_OK, data);
        finish();
    }
}
