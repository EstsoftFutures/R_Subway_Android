package com.estsoft.r_subway_android;


import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.estsoft.r_subway_android.Controller.RouteController;
import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.UI.RouteInfo.RoutePagerAdapter;
import com.estsoft.r_subway_android.UI.Settings.ExpandableListAdapter;
import com.estsoft.r_subway_android.UI.Settings.SearchSetting;
import com.estsoft.r_subway_android.UI.StationInfo.PagerAdapter;
import com.estsoft.r_subway_android.listener.TtfMapImageViewListener;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TtfMapImageViewListener, SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";

    private final int WAIT = 1;
    private final int FULL = 2;
    private int status = WAIT;

    private static final int ALL_MARKERS = 0;
    private static final int ACTI_MARKER = 1;


    private RouteController routeController = null;
    private Station activeStation = null;
    private Station startStation = null;
    private Station endStation = null;


    private TtfMapImageView mapView = null;

    private List<ImageView> markerList = null;
    ExpandableListView expListView;
    SearchSetting searchSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!설정창 Sliding menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //search
        toolbar.inflateMenu(R.menu.search);

        SearchView mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        /////////////////////////////////////////////////////////////////

        setSupportActionBar(toolbar);



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {

                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchSetting = new SearchSetting();
        expListView = (ExpandableListView) findViewById(R.id.search_setting);
        final ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, searchSetting.getGroupList(), searchSetting.getSettingCollection());
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) expandableListAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getBaseContext(), "item selected", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!BottomSheet
////        Button stationinfoButton = (Button) findViewById(R.id.stationinfoB);
////        Button stationinfoButton2 = (Button) findViewById(R.id.stationinfoB2);
//        stationinfoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runBottomsheet();
//            }
//            });
//
//        stationinfoButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runBottomsheet();
//            }
//        });

        // TtfMapImageView ... mapView 의 구현
        mapView = ((TtfMapImageView) findViewById(R.id.mapView));
        mapView.setImageResource(R.drawable.example_curve_62kb_1200x600);
        mapView.setTtfMapImageViewListener(this);

    }

    //Search icon없이 바로뜨게
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchMenu = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("역검색");
        searchMenu.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

           searchView.onActionViewExpanded();
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    //설정창 navigation items
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        return true;
    }



     /*
        Down Here - TtfMapImageView
        Implemented Listener Override Methods
    */

    @Override
    public void setMarkerDefault(int markerMode) {
        if (markerList == null) {
            markerList = new ArrayList<>();
            markerList.add((ImageView) findViewById(R.id.marker));
            markerList.add((ImageView) findViewById(R.id.startMarker));
            markerList.add((ImageView) findViewById(R.id.endMarker));
        }
        if (markerMode == ALL_MARKERS) {
            for (ImageView marker : markerList) {
                setMarkerVisibility(marker, false);
            }
            activeStation = null;
            startStation = null;
            endStation = null;

            status = WAIT;

        } else {
            setMarkerVisibility(markerList.get(0), false);
            activeStation = null;
            ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        }
    }

    @Override
    public void setActiveStation(SemiStation semiStation) {
        if (status != FULL) {
            activeStation = RouteController.getInstance().getStation(semiStation);
            setMarkerVisibility((ImageView) findViewById(R.id.marker), true);
            setMarkerPosition(0, null, null);
            runBottomSheet(null, null);
        }
//        runBottomSheet( activeStation, null);

    }

    private void setMarkerVisibility(ImageView marker, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        marker.setVisibility(visibility);
        if (marker.getId() == R.id.marker)
            ((TextView) findViewById(R.id.markerText)).setVisibility(visibility);

    }

    @Override
    public void applyMapScaleChange() {
        // 맵뷰 스케일이 바뀔때마다 Call
        setMarkerPosition(0, null, null);
    }

    public void setMarkerPosition(float markerRatio, PointF markerPosition1, String stationName1) {

        for (ImageView marker : markerList) {
            if (marker.getVisibility() == View.VISIBLE) {

                PointF markerPosition;
                String stationName = "";
                switch (marker.getId()) {
                    case R.id.marker:
                        markerPosition = activeStation.getMapPoint();
                        stationName = activeStation.getStationName();
                        break;
                    case R.id.startMarker:
                        markerPosition = startStation.getMapPoint();
                        stationName = startStation.getStationName();
                        break;
                    case R.id.endMarker:
                        markerPosition = endStation.getMapPoint();
                        stationName = endStation.getStationName();
                        break;
                    default:
                        markerPosition = new PointF(0, 0);
                }


                Matrix markerMatrix = new Matrix();
                float[] values = new float[9];
                markerMatrix.getValues(values);

                Drawable markerImage = marker.getDrawable();
                float markerMagnification = mapView.getMarkerRatio() / ((markerImage.getIntrinsicWidth() + markerImage.getIntrinsicHeight()) / 2);
                values[0] = values[4] = markerMagnification;
                float markerWidth = markerImage.getIntrinsicWidth() * markerMagnification;
                float markerHeight = markerImage.getIntrinsicHeight() * markerMagnification;
                values[2] = markerPosition.x - markerWidth / 2;
                values[5] = markerPosition.y - markerHeight;

                markerMatrix.setValues(values);
                marker.setImageMatrix(markerMatrix);

                // StationName Set
                if (marker.getId() == R.id.marker) {
                    TextView markerText = (TextView) findViewById(R.id.markerText);
                    markerText.setText(stationName);
                    markerText.measure(0, 0);
                    markerText.setX(markerPosition.x - markerText.getMeasuredWidth() / 2);
                    markerText.setY(markerPosition.y - markerText.getMeasuredHeight() - markerWidth / 2);
                    Log.d("main", (markerText.getMeasuredWidth() / 2) + " / " + (markerText.getMeasuredHeight() - markerWidth / 2));

                }
            }

        }

    }


    /*
    Bottomsheets
    */
    public void runBottomSheet(Station station, Route route) {
        BottomSheetLayout stationBottomSheet = (BottomSheetLayout) findViewById(R.id.station_bottomSheet);
        BottomSheetLayout routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);
        if (status == WAIT) {         // Station 정보
            if (stationBottomSheet.isSheetShowing()) {
                LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false);
            } else {
                stationBottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false));
            }
            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//        viewPager.setOffscreenPageLimit(3);
            Log.d("pager", "------------->" + viewPager.toString());
            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabsStrip.setTabPaddingLeftRight(25);

            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

            stationBottomSheet.setShouldDimContentView(false);
            stationBottomSheet.setInterceptContentTouch(false);

            TextView start = (TextView) findViewById(R.id.Start);
            TextView arrive = (TextView) findViewById(R.id.Arrive);
//            Log.d("----------->", start.getText().toString());
//            Log.d("----------->", arrive.getText().toString());
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartClick(v);
                }
            });
            arrive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArriveClick(v);
                }
            });
        } else if (status == FULL) {          // Route 정보
//            if(bottomSheet!= null) bottomSheet.dismissSheet();
            routeBottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_routeinfo_bottomsheet, stationBottomSheet, false));
            // Get the ViewPager and set it's RoutePagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.route_viewpager);
            viewPager.setAdapter(new RoutePagerAdapter(getSupportFragmentManager()));
//        viewPager.setOffscreenPageLimit(3);
            Log.d("pager", "------------->" + viewPager.toString());
            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.route_tabs);
            tabsStrip.setTabPaddingLeftRight(25);

            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);
            routeBottomSheet.setShouldDimContentView(false);
            routeBottomSheet.setInterceptContentTouch(false);

            stationBottomSheet.dismissSheet();

        }

    }

    /*
        Down Here - BottomSheet
        Implemented Listener Override Methods
    */

    public void onStartClick(View v) {
        ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        startStation = activeStation;
//        activeStation = null;
        // 0 :defaultMarker, 1 : startMarker, 2 : endMarker
        ImageView startMarker = markerList.get(1);
        setMarkerVisibility(startMarker, true);
        setMarkerVisibility(markerList.get(0), false);
        setMarkerPosition(0, null, null);
        setStatus();
    }

    public void onArriveClick(View v) {
        ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        endStation = activeStation;
//        activeStation = null;
        // 0 :defaultMarker, 1 : startMarker, 2 : endMarker
        ImageView startMarker = markerList.get(2);
        setMarkerVisibility(startMarker, true);
        setMarkerVisibility(markerList.get(0), false);
        setMarkerPosition(0, null, null);
        setStatus();
    }

    private void setStatus() {
        if (startStation != null && endStation != null) {
            status = FULL;
            runBottomSheet(null, null);
        } else {
            status = WAIT;
        }
    }

}
