package com.estsoft.r_subway_android.listener;


import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.estsoft.r_subway_android.MainActivity;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.UI.StationInfo.SearchListAdapter;
import com.estsoft.r_subway_android.localization.KoreanTextMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-07-08.
 */
public class InteractionListener implements
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        ExpandableListView.OnChildClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    public InteractionListener(Context context, List<SemiStation> list) {

        this.host = (MainActivity) context;
        this.toolbar = host.getToolbar();
        this.semiStationList = list;

    }

    private static final String TAG = "InteractionListener";

    private final MainActivity host;

    private final Toolbar toolbar;

    private final int startStationButton = R.id.Start;
    private final int arriveStationButton = R.id.Arrive;
    private final int endInfoButton = R.id.endinfo;
    private final int toolbarNavButtonView = 0;

    private final List<SemiStation> semiStationList;

    private Menu menu;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case startStationButton:
                Log.d(TAG, "onClick: startStationButton");
                host.onStartClick(v);
                break;

            case arriveStationButton:
                Log.d(TAG, "onClick: arriveStationButton");
                host.onArriveClick(v);
                break;

            case endInfoButton:
                Log.d(TAG, "onClick: endInfoButton");
                host.onArriveClick(v);
                host.setMarkerDefault(host.ALL_MARKERS);
                host.getRouteBottomSheet().dismissSheet();
                break;

            default:
                Log.d(TAG, "onClick: default");
                break;
        }

        //툴바의 mNavButtonView 인지 더 정확히 확인해야함.
        if (v.getClass() == ImageButton.class) {
            Log.d(TAG, "onClick: toolbarNavigationImageButton");
            if (host.getDrawer().isDrawerOpen(GravityCompat.START)) {
                host.getDrawer().closeDrawer(GravityCompat.START);
                Log.d(TAG, "onClick: drawer open");

            } else {
                host.getDrawer().openDrawer(GravityCompat.START);
                host.hideSoftKeyboard(v);
                Log.d(TAG, "onClick: drawer closed");
            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Menu m = menu;
        final MenuItem searchMenu = m.findItem(R.id.menu_search);
        m.performIdentifierAction(searchMenu.getItemId(), 0);

        RecyclerView list = (RecyclerView) host.findViewById(R.id.list_test_view);
        list.setVisibility(View.VISIBLE);

        List<SemiStation> searchResult = checkChoseong(newText);


        // use a linear layout manager
        list.setLayoutManager(new LinearLayoutManager(host.getApplicationContext()));
        // semiStation 에 라인번호들 입력
//        for ( SemiStation ss : searchResult ) {
//            ss.setLaneNumbers( host.getStationController().getExNumbers(ss) );
//        }
        SearchListAdapter sla = new SearchListAdapter( searchResult, host.getLayoutInflater() );
        sla.setmSearchListAdapterListener(host);
        sla.SetOnItemClickListener(sla.getmItemClickListener());

        list.setAdapter( sla );

        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        final String selected = (String) host.getExpandableListAdapter().getChild(groupPosition, childPosition);
        Toast.makeText(host, "item selected", Toast.LENGTH_SHORT).show();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) host.findViewById(R.id.drawer_layout);

        return true;
    }

    private List<SemiStation> checkChoseong(String searchSt) {
        List<SemiStation> sl = new ArrayList<>();
        for (SemiStation st : semiStationList) {
            if (KoreanTextMatcher.isMatch(st.getName(), searchSt)) {
                sl.add(st);
            }
        }
        return sl;
    }


    public void setMenu(Menu menu) {
        this.menu = menu;
    }



}
