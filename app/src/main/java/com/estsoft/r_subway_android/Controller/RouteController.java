package com.estsoft.r_subway_android.Controller;

import android.graphics.PointF;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

/**
 * Created by estsoft on 2016-06-28.
 */
public class RouteController {

    private static final String TAG = "RouteController";
    private static RouteController instance = null;

    public static RouteController getInstance(){
        if (instance == null) {
            instance = new RouteController();
        }
        return instance;
    }

    private RouteController(  ) {    }

    private Station actriveStation = null;


    public Station getStation(SemiStation semiStation ) {

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

}
