package com.estsoft.r_subway_android.Controller;

import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.TtfNode;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.utility.ShortestPath;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by estsoft on 2016-07-18.
 */
public class RouteControllerNew {

    private static final String TAG = "RouteControllerNew";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    StationController stationController = null;
    TtfMapImageView mapView = null;

    private Route lastRoute ;
    private Station lastStation;
    private Calendar calendar;

    public RouteControllerNew(StationController stationController, TtfMapImageView mapView) {
        this.stationController = stationController;
        this.mapView = mapView;
    }

    public Route getRoute( Station start, Station end ) {
        // Route Congestion level is -1
        lastRoute = new Route(-1, start.getStationId1(), end.getStationId1() );

        List<TtfNode> routeList = new ArrayList<>();
        List<TtfNode> transList = new ArrayList<>();
        List<Calendar> timeList = new ArrayList<>();

        calendar = new GregorianCalendar();

        //route Station Adding...




        int[] path = ShortestPath.getShortestPathByIntArray( stationController.getAdj(), start, end );

        Log.d(TAG, "getRoute123: " + start.getIndex() + " / " + end.getIndex());

        for (int i = 0; i < path.length; i ++ ) {
            Station station = stationController.getStation(path[i]);
            if (station.getMapPoint() == null ) {
                station.setMapPoint(mapView.getStationPointByName( station.getStationName() ));
            }

            if ( lastStation == null
                    || station.getStationName().equals(lastStation.getStationName())) {

                if ( lastStation == null ) calendar =  getRealTimeAPI( calendar, true );
                else calendar = getRealTimeAPI( calendar, false);

                if (i != path.length -1) {
                    transList.add( station );
                    timeList.add( calendar );
                }

            } else {
                if (i != path.length -1) {
                    calendar = getAddedTime(calendar, stationController.getStation(path[i - 1]), station);
                    timeList.add(calendar);
                }
            }

            if ( lastStation != null
                    && station.getStationName().equals(lastStation.getStationName())
                    && i == path.length - 1  ) {
            } else {
                routeList.add(station);
            }

            lastStation = station;
        }


        Log.d(TAG, "getRoute: timeList = " + timeList.size());
        for ( Calendar cal : timeList ) {
            Log.d(TAG, "getRoute: " + sdf.format(cal.getTime()));
        }
        Log.d(TAG, "getRoute: routeList = " + routeList.size());
        for ( TtfNode station : routeList ) {
            Log.d(TAG, "getRoute: routeList, " + ((Station)station).getStationName() + " id =" +  ((Station)station).getStationID());
        }
        Log.d(TAG, "getRoute: transList = " + transList.size());
        for ( TtfNode station : transList ) {
            Log.d(TAG, "getRoute: transList, " + ((Station)station).getStationName() + " id =" +  ((Station)station).getStationID());
        }

        lastRoute.setStationList(routeList);
        lastRoute.setTransferStations(transList);

        lastStation = null;

        return lastRoute ;

    }

    private Calendar getRealTimeAPI( Calendar cal, boolean startStation ) {
        //환승역일때는 5분 추가!
        Calendar newCal = (Calendar) cal.clone();
        if (!startStation) {
         newCal.set(Calendar.MINUTE, newCal.get(Calendar.MINUTE) + 5);
        }
        return newCal;
    }

    private Calendar getAddedTime( Calendar cal, Station station, Station next ) {
        ArrayList<Pair<Station, Integer>>[] adj = stationController.getAdj();
        ArrayList<Pair<Station, Integer>> pairs = adj[station.getIndex()];

        Calendar newCal = (Calendar)cal.clone();

        for ( Pair<Station, Integer> pair : pairs ) {
            if ( pair.first.getStationID() == next.getStationID() ) {
                Log.d(TAG, "getAddedTime: before " + newCal.get( Calendar.MINUTE ));
                int edgeCost = pair.second;
                newCal.set(Calendar.MINUTE, newCal.get(Calendar.MINUTE) +  Math.round(edgeCost / 6) );
                Log.d(TAG, "getAddedTime: after " + newCal.get( Calendar.MINUTE ));
                return newCal;
            }
        }
        Log.d(TAG, "getAddedTime: NULL");
        return newCal;
    }
}
