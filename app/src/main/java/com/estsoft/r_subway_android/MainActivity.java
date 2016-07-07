package com.estsoft.r_subway_android;


import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.estsoft.r_subway_android.Controller.RouteController;
import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.TtfNode;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.UI.RouteInfo.RoutePagerAdapter;
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

    private Route normalRoute = null;
    private List<ImageView> routeMarkers = null;

    private RelativeLayout passMarkerMother = null;

    private TtfMapImageView mapView = null;

    private List<ImageView> markerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        // passMarkerMother Relative View reference
        passMarkerMother = (RelativeLayout) findViewById(R.id.route_mother);

        // TtfMapImageView ... mapView 의 구현
        mapView = ((TtfMapImageView) findViewById(R.id.mapView));
        mapView.setImageResource(R.drawable.example_curve_62kb_1200x600);
        mapView.setTtfMapImageViewListener(this);

        routeController = RouteController.getInstance( mapView );

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

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/


    //설정창 navigation items
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //     drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
        down here
        Implements Override Mettomsheet() {



/*                ///////////////////////////////////////////////////////////////
                TextView Stv = (TextView) findViewById(R.id.Start);
                TextView Etv = (TextView) findViewById(R.id.End);

                // we can use these listeners to make start/end pts
                Stv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Start clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                Etv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "End clicked", Toast.LENGTH_SHORT).show();
                    }
                });
//이건 탭뷰에 있어야하
                Button btn = (Button) findViewById(R.id.SubwayTime);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SubwayTimeActivity.class);
                        startActivity(intent);
                    }
                });

                    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }*/


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

        } else {
            setMarkerVisibility(markerList.get(0), false);
            activeStation = null;
            ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        }
    }

    @Override
    public void setActiveStation(SemiStation semiStation) {
        if (status != FULL) {
            activeStation = routeController.getStation(semiStation);
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

        if (normalRoute != null) {
            for (TtfNode station : normalRoute.getStationList()) {
//                Log.d("RouteTest", "getRoute: " + ((Station) station).getStationName());
//                Log.d("RouteTest", "getRoute: " + ((Station) station).getMapPoint().toString());
                setRouteMarkerPosition();
            }
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


                setImageMatrix( marker, mapView.getMarkerRatio(), markerPosition );

            }

        }

    }

    private void setRouteMarkerPosition(){

        if (routeMarkers == null || normalRoute == null) return;

        for (int i = 0; i < normalRoute.getStationList().size(); i ++ ) {
            if ( i == 0 || i == normalRoute.getStationList().size() - 1 ) continue;

            setImageMatrix(
                    routeMarkers.get(i - 1),
                    mapView.getMarkerRatio(),
                    ((Station)normalRoute.getStationList().get(i)).getMapPoint()
            );
//            Log.d(TAG, "setRouteMarkerPosition: " + markerMatrix.toString());
//            Log.d(TAG, "setRouteMarkerPosition: " + passMarker.getMatrix().toString());

        }

    }

    private void setImageMatrix( ImageView view, float markerRatio, PointF point){
        if ( view.getScaleType() != ImageView.ScaleType.MATRIX ) view.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        float[] values = new float[9];
        matrix.getValues( values );
        Drawable image = view.getDrawable();
        float magnification = markerRatio / ((image.getIntrinsicWidth() + image.getIntrinsicHeight()) / 2);
        values[0] = values[4] = magnification;
        float width = image.getIntrinsicWidth() * magnification;
        float height = image.getIntrinsicHeight() * magnification;
        values[2] = point.x - width / 2;
        values[5] = point.y - height;
        matrix.setValues(values);
        view.setImageMatrix(matrix);

        if ( view.getId() == R.id.marker ) {
            TextView markerText = (TextView) findViewById(R.id.markerText);
            markerText.setText(activeStation.getStationName());
            markerText.measure(0, 0);
            markerText.setX(point.x - markerText.getMeasuredWidth() / 2);
            markerText.setY(point.y - markerText.getMeasuredHeight() - width / 2);
        }


    }

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
            //RouteBottomSheet Call
            runBottomSheet(null, null);
            //MainActivity make Route Drawing
            normalRoute = routeController.getRoute( startStation, endStation );
            for ( TtfNode station : normalRoute.getStationList() ) {
                Log.d("RouteTest", "getRoute: " + ((Station)station).getStationName() );
                Log.d("RouteTest", "getRoute: " + ((Station)station).getMapPoint().toString() );
            }
            inflateRoute(normalRoute);

        } else {
            status = WAIT;
        }
    }

    private void inflateRoute( Route route ){
        if (routeMarkers == null) routeMarkers = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE );

        for (int i = 0; i < route.getStationList().size(); i ++ ){
            if ( i == 0 || i == route.getStationList().size() - 1) continue;

            ImageView marker = (ImageView)inflater.inflate( R.layout.content_main_route, null );
            marker.setId( 3000 + i );
            if (passMarkerMother != null) {
                passMarkerMother.addView( marker );
                marker.setVisibility(View.VISIBLE);
                routeMarkers.add(marker);
                //marker set Layout width, height using startMarker's LayoutParam
                marker.setLayoutParams( markerList.get(0).getLayoutParams());
                Log.d(TAG, "inflateRoute: max : " + marker.getMaxWidth() + " / " + marker.getMaxHeight());
            }
        }
        setRouteMarkerPosition();

    }

}
