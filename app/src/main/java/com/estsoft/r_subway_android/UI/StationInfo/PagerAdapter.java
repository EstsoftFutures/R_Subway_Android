package com.estsoft.r_subway_android.UI.StationInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.estsoft.r_subway_android.Repository.StationRepository.Station;

/**
 * Created by Administrator on 2016-07-04.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3", "Tab4"};

    private static Station station = null;

    public PagerAdapter(FragmentManager fm, Station station) {
        super(fm);
        this.station = station;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return StationInfoFragment.newInstance(position, station);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


