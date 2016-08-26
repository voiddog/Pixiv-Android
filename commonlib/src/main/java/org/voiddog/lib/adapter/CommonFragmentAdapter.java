package org.voiddog.lib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Author: lirui(root@lirui.in)
 * Date: 2015-07-17
 * Time: 18:03
 * Description:通用fragment适配器
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;
    private String[] mTitles;

    public CommonFragmentAdapter(FragmentManager fm, Fragment[] mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    public CommonFragmentAdapter(FragmentManager fm, Fragment[] mFragments, String[] titles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles == null || position >= mTitles.length){
            return null;
        }
        return mTitles[position];
    }
}
