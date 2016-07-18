package com.estsoft.r_subway_android;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.estsoft.r_subway_android.Controller.RouteController;
import com.estsoft.r_subway_android.Controller.StationController;
import com.estsoft.r_subway_android.Repository.StationRepository.InitializeRealm;
import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.TtfNode;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.UI.RouteInfo.RoutePagerAdapter;
import com.estsoft.r_subway_android.UI.Settings.ExpandableListAdapter;
import com.estsoft.r_subway_android.UI.Settings.SearchSetting;
import com.estsoft.r_subway_android.UI.StationInfo.PagerAdapter;
import com.estsoft.r_subway_android.listener.SearchListAdapterListener;
import com.estsoft.r_subway_android.listener.TtfMapImageViewListener;
import com.estsoft.r_subway_android.listener.InteractionListener;
import com.facebook.stetho.Stetho;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity
        implements TtfMapImageViewListener,
        SearchListAdapterListener {

    private static final String TAG = "MainActivity";

    public final int WAIT = 1;
    public final int FULL = 2;
    public int status = WAIT;

    public static final int ALL_MARKERS = 0;
    public static final int ACTI_MARKER = 1;

    private InteractionListener interactionListener = null;

    private BottomSheetLayout stationBottomSheet = null;
    private BottomSheetLayout routeBottomSheet = null;

    private Toolbar toolbar = null;

    private DrawerLayout drawer = null;

    private SearchView mSearchView = null;

    private NavigationView navigationView = null;

    private ExpandableListAdapter expandableListAdapter = null;

    private RouteController routeController = null;
    private StationController stationController = null;
    private Station activeStation = null;
    private Station startStation = null;
    private Station endStation = null;

    private Route normalRoute = null;
    private RelativeLayout passMarkerMother = null;
    private List<ImageView> routeMarkers = null;
    private TextView markerText = null;
    private List<ImageView> markerList = null;

    private TtfMapImageView mapView = null;


    ExpandableListView expListView;
    SearchSetting searchSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //역이름 텍스트
        markerText = ((TextView) findViewById(R.id.markerText));

        // passMarkerMother Relative View reference
        passMarkerMother = (RelativeLayout) findViewById(R.id.route_mother);

        // TtfMapImageView ... mapView 의 구현
        mapView = ((TtfMapImageView) findViewById(R.id.mapView));
        mapView.setImageResource(R.drawable.linemap_naver);
        Log.e(TAG, "onCreate: " + mapView.getDrawable().getIntrinsicWidth());
        mapView.setTtfMapImageViewListener(this);

        routeController = RouteController.getInstance(mapView);
        mapView.setImageResource(R.drawable.linemap_naver);
        mapView.setTtfMapImageViewListener(this);

        interactionListener = new InteractionListener(this, mapView.getSemiStationList());


        stationBottomSheet = (BottomSheetLayout) findViewById(R.id.station_bottomSheet);
        routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        //설정창 Sliding menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //search
        toolbar.inflateMenu(R.menu.search);

        mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();

        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(interactionListener);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(interactionListener);

        searchSetting = new SearchSetting();
        expandableListAdapter = new ExpandableListAdapter(this, searchSetting.getGroupList(), searchSetting.getSettingCollection());

        expListView = (ExpandableListView) findViewById(R.id.search_setting);
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnChildClickListener(interactionListener);


        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        SharedPreferences initRealmPrefs = getSharedPreferences("initRealmPrefs", MODE_PRIVATE);
        String init = initRealmPrefs.getString("Init",null);
        if(init == null) {
            InitializeRealm initRealm = new InitializeRealm(this);
            for(int i=100, j=1; i<=20138; i++, j++) {
                String json = initRealm.getJSONFromAsset(i);
                if(json != null) {
                    initRealm.loadJSONToRealm(json, j);
                }
            }
            initRealm.connectStations();

            SharedPreferences.Editor editor = initRealmPrefs.edit();
            editor.putString("Init", "Done");
            editor.commit();
        }


        Log.d("\\\\\\\\\\\\\\", initRealmPrefs.getString("Init",null));
        Realm realm = Realm.getInstance(this);
        RealmResults<RealmStation> results = realm.where(RealmStation.class).findAll();
        for(RealmStation station : results) {
            Log.d("\\\\\\\\\\", station.getStationName() + station.getStationID() + "x : " + station.getxPos() + "y : " + station.getyPos());
        }


        stationController = new StationController( Realm.getInstance(this) );
//        mapView.setSemiStationLaneNumber( stationController );
    }


    //Search icon없이 바로뜨게
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchMenu = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchMenu.getActionView();


        interactionListener.setMenu(menu);
//        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();


        if (searchMenu!= null) {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            if(searchView !=null){
                LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
                if(searchPlate != null){
                    EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
                    if(mSearchEditText != null){
                        mSearchEditText.clearFocus();     // This fixes the keyboard from popping up each time
                        mSearchEditText.setCursorVisible(false);
                    }
                }
            }
        }

        searchView.setOnQueryTextListener(interactionListener);

        ImageView closebtn = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
                EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
                RecyclerView list = (RecyclerView) findViewById(R.id.list_test_view);
                mSearchEditText.setText("");

                hideSoftKeyboard(v);
                list.setVisibility(View.GONE);

            }
        });


        LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
        EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
        mSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"searchtextview");
            }
        });

        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("역검색");

        searchMenu.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);




        return true;
    }

    public void onComposeAction(MenuItem mi) {
        // handle click here
        Log.d("123","123");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Log.d("1234534534534533","12334534534534");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
            Hide Keyboard
             */
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    //설정창 navigation items
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        return true;
//    } 수정

    /*
        Down Here - SearchListAdapter
        Implemented Listener Override Methods
    */

    @Override
    public void itemClick(SemiStation semiStation) {
        setActiveStation( semiStation );
        RecyclerView list = (RecyclerView)findViewById(R.id.list_test_view);
        list.setVisibility(View.GONE);
        mapView.moveToMapCenter( semiStation.getPosition() );

        hideSoftKeyboard(mapView);

        Log.d(TAG, "itemClick: ");
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
//            setMarkerVisibility( (ImageView)findViewById(R.id.route_image_source), false);
            activeStation = null;
            startStation = null;
            endStation = null;
            status = WAIT;

            normalRoute = null;
            if (routeMarkers != null) {
                for (ImageView view : routeMarkers) {
                    view.setVisibility(View.GONE);
                    passMarkerMother.removeView(view);
                }
            }
            routeMarkers = null;

            applyMapScaleChange();

        } else if (markerMode == ACTI_MARKER) {
            setMarkerVisibility(markerList.get(0), false);
            activeStation = null;
            if (findViewById(R.id.station_bottomSheet) != null) {
                stationBottomSheet.dismissSheet();
            }
        } else {

        }
    }

    @Override
    public void setActiveStation(SemiStation semiStation) {

        if (status != FULL) {
            activeStation = stationController.getStation(semiStation);
            Log.d(TAG, "setActiveStation: " + activeStation.getStationId1());

            boolean flag = false;
            List<Station> checkList = new ArrayList<>();
            checkList.add(startStation);
            checkList.add(endStation);
            for (Station station : checkList) {
                if (station == null) continue;
                else {
                    if (activeStation.getStationId1().equals(station.getStationId1())) {
                        activeStation = station;
                        setMarkerVisibility((ImageView) findViewById(R.id.marker), false);
                        flag = true;
                        break;
                    }
                }
            }

            if (flag == false) {
                setMarkerVisibility((ImageView) findViewById(R.id.marker), true);
                setMarkerPosition(0, null, null);
            }


//            runBottomSheet(stationController.getExStations(activeStation), null);
            runBottomSheet(activeStation, null);
            stationController.getExStations(activeStation);
        }
    }

    private void setMarkerVisibility(ImageView marker, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        marker.setVisibility(visibility);
        if (marker.getId() == R.id.marker)
            markerText.setVisibility(visibility);

    }

    @Override
    public void applyMapScaleChange() {
        // 맵뷰 스케일이 바뀔때마다 Call
        setMarkerPosition(0, null, null);

        hideSoftKeyboard(mapView);

        if (normalRoute != null) {
            setRouteMarkerPosition();
        }

    }

    private void setMarkerPosition(float markerRatio, PointF markerPosition1, String stationName1) {

        for (ImageView marker : markerList) {
            if (marker.getVisibility() == View.VISIBLE) {

                PointF markerPosition;
                switch (marker.getId()) {
                    case R.id.marker:
                        markerPosition = activeStation.getMapPoint();
                        break;
                    case R.id.startMarker:
                        markerPosition = startStation.getMapPoint();
                        break;
                    case R.id.endMarker:
                        markerPosition = endStation.getMapPoint();
                        break;
                    default:
                        markerPosition = new PointF(0, 0);
                }
                Log.d(TAG, "setMarkerPosition: " + markerPosition.toString());
                setImageMatrix(marker, mapView.getMarkerRatio(), markerPosition);

            }

        }

    }

    private void setRouteMarkerPosition() {

        if (routeMarkers == null || normalRoute == null) return;

        for (int i = 0; i < normalRoute.getStationList().size(); i++) {
            if (i == 0 || i == normalRoute.getStationList().size() - 1) continue;
            Log.d(TAG, "setRouteMarkerPosition: " + i);
            setImageMatrix(
                    routeMarkers.get(i - 1),
                    mapView.getMarkerRatio(),
                    ((Station) normalRoute.getStationList().get(i)).getMapPoint()
            );

        }

    }

    private void setImageMatrix(ImageView view, float markerRatio, PointF point) {
        if (view.getScaleType() != ImageView.ScaleType.MATRIX)
            view.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        float[] values = new float[9];
        matrix.getValues(values);
        Drawable image = view.getDrawable();
        float magnification = markerRatio / ((image.getIntrinsicWidth() + image.getIntrinsicHeight()) / 2);
        values[0] = values[4] = magnification;
        float width = image.getIntrinsicWidth() * magnification;
        float height = image.getIntrinsicHeight() * magnification;
        values[2] = point.x - width / 2;
        values[5] = point.y - height;
        matrix.setValues(values);
        view.setImageMatrix(matrix);

        if (view.getId() == R.id.marker) {
            markerText = (TextView) findViewById(R.id.markerText);
            markerText.setText(activeStation.getStationName());
            markerText.measure(0, 0);
            markerText.setX(point.x - markerText.getMeasuredWidth() / 2);
            markerText.setY(point.y - markerText.getMeasuredHeight() - height / 3);
        }


    }


    /*
    BottomSheets
    */
    public void runBottomSheet(Station station, Route route) {
        BottomSheetLayout stationBottomSheet = (BottomSheetLayout) findViewById(R.id.station_bottomSheet);
        stationBottomSheet.setPeekSheetTranslation(490);
        final BottomSheetLayout routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);
        if (status == WAIT) {         // Station 정보
            if (stationBottomSheet.isSheetShowing()) {
                LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false);
            } else {
                stationBottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false));
            }
            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), station));
//        viewPager.setOffscreenPageLimit(3);
            Log.d("pager", "------------->" + viewPager.toString());
            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabsStrip.setTabPaddingLeftRight(25);

            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

            stationBottomSheet.setShouldDimContentView(false);
            stationBottomSheet.setInterceptContentTouch(false);

            Log.d(TAG, "runBottomSheet: " + ((LinearLayout) stationBottomSheet.getSheetView()).getChildAt(0).getClass());

            TextView start = (TextView) findViewById(R.id.Start);
            TextView arrive = (TextView) findViewById(R.id.Arrive);
            start.setOnClickListener(interactionListener);
            arrive.setOnClickListener(interactionListener);

//            Log.d("----------->", start.getText().toString());
//            Log.d("----------->", arrive.getText().toString());

            // 리스너로 감
            /*start.setOnClickListener(new View.OnClickListener() {
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
            });*/


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


            routeBottomSheet.findViewById(R.id.end_info).setOnClickListener(interactionListener);
            // 리스너로 감
            /*routeBottomSheet.findViewById(R.id.endinfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMarkerDefault( ALL_MARKERS );
                    routeBottomSheet.dismissSheet();
                }
            });*/

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
            //RouteBottomSheet Call
//            runBottomSheet(null, Route);
            runBottomSheet(null, null);
            //MainActivity make Route Drawing
            normalRoute = routeController.getRoute(startStation, endStation);
            for (TtfNode station : normalRoute.getStationList()) {
                Log.d("RouteTest", "getRoute: " + ((Station) station).getStationName());
                Log.d("RouteTest", "getRoute: " + ((Station) station).getMapPoint().toString());
            }
            inflateRoute(normalRoute);

        } else {
            status = WAIT;
        }
    }

    private void inflateRoute(Route route) {
        if (routeMarkers == null) routeMarkers = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < route.getStationList().size(); i++) {
            if (i == 0 || i == route.getStationList().size() - 1) continue;


            Log.d(TAG, "inflateRoute: ");
            ImageView marker = (ImageView) inflater.inflate(R.layout.content_main_route, null);
            marker.setId(3000 + i);
            if (passMarkerMother != null) {
                passMarkerMother.addView(marker);
                marker.setVisibility(View.VISIBLE);
                routeMarkers.add(marker);
                //marker set Layout width, height using startMarker's LayoutParam
                marker.setLayoutParams(markerList.get(0).getLayoutParams());

            }
        }
        setRouteMarkerPosition();

    }



    /*
    getters
    */

    public BottomSheetLayout getRouteBottomSheet() {
        return routeBottomSheet;
    }

    public BottomSheetLayout getStationBottomSheet() {
        return stationBottomSheet;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        return expandableListAdapter;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public StationController getStationController() {
        return stationController;
    }
}
