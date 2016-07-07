package com.estsoft.r_subway_android.Controller;

import android.graphics.PointF;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.TtfNode;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-28.
 */
public class RouteController {

    private static final String TAG = "RouteController";
    private static RouteController instance = null;

    private static TtfMapImageView mapView = null;

    private final int testConlevel = 0;

    public static RouteController getInstance( TtfMapImageView mapView ){
        if (instance == null) {
            instance = new RouteController( mapView );
        }
        return instance;
    }

    public static RouteController getInstance(){
        if ( mapView == null ) return null;
        if ( instance == null ) return null;
        return instance;
    }

    private RouteController( TtfMapImageView mapView ) {
        this.mapView = mapView;
    }

    private Station actriveStation = null;


    public Station getStation( SemiStation semiStation ) {

        // API, Realm, Sever Communication
        int conLevel = 0;
        PointF geoPoint = new PointF(0, 0);
        Station station = new Station(conLevel, semiStation.getId(), geoPoint);

        // stationMapping 을 추후에!!! 꼭!!!
        // 이하 두줄은 지금 꼭 필요한거 대충 만들어놓은 거임. 필수임.
        station.setMapPoint( semiStation.getPosition() );
        station.setStationName( semiStation.getId() );

        Log.d( TAG, "semiStation pointf hash : " + System.identityHashCode( semiStation.getPosition() )  );
        Log.d( TAG, "NewStation pointf hash : " + System.identityHashCode( station.getMapPoint() )  );

        return  station;

    }

    public Route getRoute( Station start, Station end ) {
        //TtfNode.... Station
        List<TtfNode> stationList = new ArrayList<>();
        stationList.add( start );

        // 알고리즘 적용
        Station station01 = new Station( 0, "600-300", null );
        station01.setStationName( "600-300" );
        station01.setMapPoint( mapView.getStationPoint( "600-300" ) );
        stationList.add( station01 );

        stationList.add( end );
        Route route = new Route( testConlevel, start.getStationId1(), end.getStationId1(), stationList);

        Log.d(TAG, "getRoute: " + stationList.size());

        return route;
    }

}
