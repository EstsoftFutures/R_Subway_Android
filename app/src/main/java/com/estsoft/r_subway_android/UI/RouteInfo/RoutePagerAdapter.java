package com.estsoft.r_subway_android.UI.RouteInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RoutePagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"최단경로", "최소환승", "Custom",};
    private RouteNew route;
    public RoutePagerAdapter(FragmentManager fm, RouteNew route) {
        super(fm);
        this.route = route;

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return RouteInfoFragment.newInstance(position, route);
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
