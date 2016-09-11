package org.voiddog.pixiv.presentation.ui.common.activity.base;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.voiddog.imageselector.ui.SelectAndSquareCropActivity;
import org.voiddog.imageselector.ui.SelectImgActivity;
import org.voiddog.lib.mvp.MvpActivity;
import org.voiddog.lib.ui.BlurView;
import org.voiddog.pixiv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基础MvpActivity
 * Created by qigengxin on 16/8/25.
 */
public abstract class BaseMvpActivity<V extends BaseActivityMvpView, P extends BaseActivityMvpPresenter<V>>
        extends MvpActivity<V, P> implements BaseActivityMvpView{

    class ViewHolder{
        @BindView(R.id.tv_name)
        public TextView tvName;

        @BindView(R.id.dv_user_head)
        public SimpleDraweeView dvUserHead;

        @BindView(R.id.drawer_layout)
        public DrawerLayout drawerLayout;

        @BindView(R.id.nv_user)
        public NavigationView nvUser;

        public BlurView blurView;

        public ViewHolder(ViewGroup rootView){
            // 插进去
            ViewGroup vp = (ViewGroup) getWindow().getDecorView();
            View subView = vp.getChildAt(0);
            subView.setBackgroundResource(R.color.window_bg_color);
            vp.removeView(subView);
            rootView.addView(subView, 0);
            vp.addView(rootView);

            // 插入 blur view
            blurView = new BlurView(rootView.getContext());
            blurView.setContentView(subView);
            rootView.addView(blurView, 1, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ));


            View headView = View.inflate(BaseMvpActivity.this, R.layout.view_navigation_head, null);
            rootView.addView(headView);

            ButterKnife.bind(this, rootView);
            rootView.removeView(headView);
            nvUser.addHeaderView(headView);
            nvUser.setBackgroundColor(0xaaffffff);

            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if(slideOffset == 0){
                        blurView.setVisibility(View.GONE);
                    }
                    else {
                        blurView.setVisibility(View.VISIBLE);
                        blurView.setAlpha(Math.min(1, 5 * slideOffset));
                        blurView.updateBitmap(Math.max(1, 12 * slideOffset));
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {}

                @Override
                public void onDrawerClosed(View drawerView) {}

                @Override
                public void onDrawerStateChanged(int newState) {}
            });

            // 修改状态栏
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                LinearLayout ll = (LinearLayout) subView;
                ll.addView(createStatusBg(
                        getResources().getColor(R.color.colorPrimary)
                ), 0);
            }

            dvUserHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseMvpActivity.this, SelectAndSquareCropActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup navView = (ViewGroup) View.inflate(this, R.layout.view_user_navigation, null);

        mViewHolder = new ViewHolder(navView);
    }

    View createStatusBg(int color){
        ImageView iv = new ImageView(this);
        iv.setImageDrawable(new ColorDrawable(color));
        iv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
        ));
        return iv;
    }
    /**
     * 获得状态栏高度
     */
    protected int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

}
