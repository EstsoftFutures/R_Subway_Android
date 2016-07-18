package com.estsoft.r_subway_android.UI.StationInfo;

import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-04.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int PAGE_COUNT;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3", "Tab4"};
    private static Station station = null;

    public PagerAdapter(FragmentManager fm, Station station) {
        super(fm);
        this.station = station;

    }

    @Override
    public int getCount() {

        //page 개수 넘기기_ 환승역 line수+1(자기자신)+(다음정류장수-1) 다음정류장이 2개인곳, 성수
        PAGE_COUNT = station.getExStations().size() + station.getNextStations().size();

          //신도림 !!!!!!!!!!!!!!!!!!!!!!해결필요함
/*        if(station.getExStations().size()>0) {
            for (Station s : station.getExStations()) {
                if (s.getNextStations().size() > 1) {
                    PAGE_COUNT += 1;
                }
            }
        }*/
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return StationInfoFragment.newInstance(position, station);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position

        tabTitles[0] = station.getLaneName();
        if (station.getExStations().size() != 0) {
            for (int i = 1; i < station.getExStations().size() + 1; i++) {
                tabTitles[i] = station.getExStations().get(i - 1).getLaneName();
            }
        }
        if(station.getNextStations().size()>1){
            Log.d("station","second next station"+station.getNextStations().get(1).getLaneName());
            tabTitles[station.getExStations().size()+1] = station.getNextStations().get(1).getLaneName();
        }

        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


