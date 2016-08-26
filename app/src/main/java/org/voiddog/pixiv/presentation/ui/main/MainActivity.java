package org.voiddog.pixiv.presentation.ui.main;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.voiddog.pixiv.R;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ActivityModule;
import org.voiddog.pixiv.presentation.ui.common.activity.base.BaseMvpActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMvpActivity<MainView, MainPresenter>
        implements MainView{

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMainComponent.builder()
                .activityModule(new ActivityModule(this))
                .build().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViews(savedInstanceState);
    }

    void setupViews(Bundle savedInstanceState){
        // setup views

        // init
        getPresenter().init(savedInstanceState);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void setTabLayout(String[] titles) {
        mTabLayout.removeAllTabs();
        for (String title : titles){
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }
    }

    @Override
    public void setContentFragment(Fragment fragment, int index) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        boolean found = false;
        for(Fragment fra : fragmentList){
            if(fra == fragment){
                transaction.show(fra);
                found = true;
            }
            else{
                transaction.hide(fra);
            }
        }

        if(!found){
            transaction.add(R.id.fra_content, fragment);
        }

        transaction.commitAllowingStateLoss();
    }
}
