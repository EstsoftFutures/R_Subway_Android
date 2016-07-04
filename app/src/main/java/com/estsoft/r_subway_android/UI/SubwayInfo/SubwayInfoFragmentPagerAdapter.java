package com.estsoft.r_subway_android.UI.SubwayInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2016-06-27.
 */
public class SubwayInfoFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3", "Tab4"};

    public SubwayInfoFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return SubwayInfoFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

