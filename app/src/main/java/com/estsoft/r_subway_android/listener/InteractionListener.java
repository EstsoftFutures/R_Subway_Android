package com.estsoft.r_subway_android.listener;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.estsoft.r_subway_android.Crawling.ServerConnectionSingle;
import com.estsoft.r_subway_android.MainActivity;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.UI.Settings.SearchSetting;
import com.estsoft.r_subway_android.UI.StationInfo.SearchListAdapter;
import com.estsoft.r_subway_android.localization.KoreanTextMatcher;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-07-08.
 */
public class InteractionListener implements
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        ExpandableListView.OnChildClickListener,
        NavigationView.OnNavigationItemSelectedListener, ExpandableListView.OnGroupClickListener, ViewPager.OnPageChangeListener,
        OnSheetDismissedListener {

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
    private final int endInfoButton = R.id.end_info;
    private final int searchTextContext = R.id.search_src_text;
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
//                host.onArriveClick(v);
                host.setMarkerDefault(host.ALL_MARKERS);
                host.getRouteBottomSheet().dismissSheet();
                break;

//            case searchTextContext:
//                ((EditText)v).setText("");
//                onQueryTextChange("");
//                break;

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

        RecyclerView listView = (RecyclerView) host.findViewById(R.id.list_test_view);

        List<SemiStation> searchResult;
        if (newText.equals("")) {
            searchResult = new ArrayList<>();
            listView.setVisibility(View.GONE);
            host.hideSoftKeyboard(host.getMapView());
        } else {
            searchResult = checkChoseong(newText);
            listView.setVisibility(View.VISIBLE);
        }



        // use a linear layout manager
        listView.setLayoutManager(new LinearLayoutManager(host.getApplicationContext()));
        // semiStation 에 라인번호들 입력
//        for ( SemiStation ss : searchResult ) {
//            ss.setLaneNumbers( host.getStationController().getExNumbers(ss) );
//        }
        SearchListAdapter sla = new SearchListAdapter(searchResult, host.getLayoutInflater());
        sla.setmSearchListAdapterListener(host);
        sla.SetOnItemClickListener(sla.getmItemClickListener());

        listView.setAdapter(sla);

        return true;
    }


    //setting창 click listener
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        ImageView check = (ImageView) v.findViewById(R.id.setting_group_check);

        //검색노선 설정은 CHECK안되도록
        if (check.getVisibility() == View.INVISIBLE && groupPosition != 1) {
            check.setVisibility(View.VISIBLE);

        } else {
            check.setVisibility(View.INVISIBLE);
        }

        switch ( groupPosition ) {
            case 0 :
                SearchSetting.setActiveExpressOnly( !SearchSetting.isActiveExpressOnly() );
                break;
            case 1 :
                /*SearchSetting.setAvoidDangerStations( !SearchSetting.isAvoidDangerStations() );
                break;
<<<<<<< HEAD
            case 2 :*/
                SearchSetting.setActiveExpressOnly( !SearchSetting.isActiveExpressOnly() );
=======
            case 2 :
                SearchSetting.setAvoidCongestStations( !SearchSetting.isAvoidCongestStations() );
>>>>>>> origin/inkiu_0803
                break;
            default:
                break;
        }
        SearchSetting.checkSettings();
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        final String selected = (String) host.getExpandableListAdapter().getChild(groupPosition, childPosition);
<<<<<<< HEAD
    //    Toast.makeText(host, "item selected", Toast.LENGTH_SHORT).show();
=======
//        Toast.makeText(host, "item selected", Toast.LENGTH_SHORT).show();
>>>>>>> origin/inkiu_0803

        ImageView check = (ImageView) v.findViewById(R.id.setting_child_check);

        if (check.getVisibility() == View.VISIBLE) {
            check.setVisibility(View.INVISIBLE);
        } else {
            check.setVisibility(View.VISIBLE);
        }


        SearchSetting.getActiveLanes().get(childPosition).setActive( !SearchSetting.getActiveLanes().get(childPosition).isActive()  );

        SearchSetting.checkSettings();


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

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch ( position ) {
                case 0 :
                    host.setCurrentRoute( position, host.SHORT_ROUTE );
                    break;
                case 1 :
                    host.setCurrentRoute( position, host.MIN_TRANSFER );
                    break;
                case 2 :
                    host.setCurrentRoute( position, host.CUSTOM_ROUTE );
                    break;
                default:
                    host.setCurrentRoute( position, host.DEFAULT_ROUTE );
                    break;
            }
            Log.d("pager","====================>"+host.getCurPage());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    @Override
    public void onDismissed(BottomSheetLayout bottomSheetLayout) {
        host.setMarkerDefault(host.ALL_MARKERS);
        host.getRouteBottomSheet().dismissSheet();
    }

    public int getSearchTextContext() {
        return searchTextContext;
    }
}
