package org.voiddog.imageselector.ui;

import org.voiddog.imageselector.R;
import org.voiddog.imageselector.ui.view.crop.CropDrawee;
import org.voiddog.lib.util.StringUtil;

import java.util.List;

/**
 *
 * Created by qigengxin on 16/9/8.
 */
public class SelectAndSquareCropActivity extends SelectImgActivity{

    CropDrawee dvHead;

    @Override
    void parseExtra() {
        super.parseExtra();
        maxSelectCount = 1;
    }

    @Override
    void createLayout() {
        setContentView(R.layout.dl_is_activity_select_square_crop);
    }

    @Override
    void initView() {
        super.initView();
        dvHead = (CropDrawee) findViewById(R.id.dl_is_dv_head);
    }

    @Override
    public void showImages(String title, List<String> imagePath) {
        if(imagePath != null && imagePath.size() > 0) {
            selectImageAdapter.select(imagePath.get(0));
            dvHead.setImageURI(StringUtil.getUriFromFilePath(imagePath.get(0)));
        }
        super.showImages(title, imagePath);
    }

    @Override
    public boolean selected(String path) {
        dvHead.setImageURI(StringUtil.getUriFromFilePath(path));
        return super.selected(path);
    }
}
