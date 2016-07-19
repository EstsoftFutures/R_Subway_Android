package com.estsoft.r_subway_android.Controller;

import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.TtfNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-07-18.
 */
public class RouteControllerNew {

    private static final String TAG = "RouteControllerNew";

    StationController stationController = null;

    private Route lastRoute ;

    public RouteControllerNew( StationController stationController ) {
        this.stationController = stationController;
    }

    public Route getRoute( Station start, Station end ) {
        // Route Congestion level is -1
        lastRoute = new Route(-1, start.getStationId1(), end.getStationId1() );

        List<TtfNode> routeList = new ArrayList<>();
        routeList.add(start);

        //route Station Adding...

        routeList.add(end);

        lastRoute.setStationList(routeList);

        return lastRoute ;

    }

}
