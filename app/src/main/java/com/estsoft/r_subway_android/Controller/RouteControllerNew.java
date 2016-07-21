package com.estsoft.r_subway_android.Controller;

import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
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

    private RouteNew lastRouteNew;
    private Calendar calendar;

    public RouteControllerNew(StationController stationController, TtfMapImageView mapView) {
        this.stationController = stationController;
        this.mapView = mapView;
    }

    public RouteNew getRouteNew( Station start, Station end ) {

        calendar = new GregorianCalendar();

        int path[] = ShortestPath.getShortestPathByIntArray( stationController.getAdj(), start, end );
        List<List<Station>> totalList = getListOfStationList( path );
        List<List<Station>> processedList = new ArrayList<>();
        List<Boolean> expressSectionIndex = new ArrayList<>();

        for ( List<Station> list : totalList ) {
            if ( checkExpressLane(list) ) { // && checkFastestTimeIsExpress
                processedList.add(removeNotExpressStation(list));
                expressSectionIndex.add(true);
            } else {
                processedList.add(list);
                expressSectionIndex.add(false);
            }
            setMapPoint(list);
        }

        for ( List<Station> list : processedList ) {
            for ( Station st : list ) {
                Log.d(TAG, "getRouteNew: " + st.getStationName() + " / " + st.getStationID() + " / " + sdf.format(st.getArriveTime().getTime()) );
            }
            Log.d(TAG, "getRouteNew: Transfer. ");
        }

        lastRouteNew = new RouteNew( processedList, expressSectionIndex );


        return lastRouteNew;
    }

    private List<List<Station>> getListOfStationList( int[] path ) {
        List<List<Station>> returnList = new ArrayList<>();
        int startIndex = 0;
        for ( int i = 0; i < path.length - 1; i ++ ) {
            Station curSt = stationController.getStation( path[i] );
            Station nextSt = stationController.getStation( path[i + 1] );
            if ( i != 0 && curSt.getStationName().equals( nextSt.getStationName() )) {
                returnList.add( getStationListBeforeTransfer( path, startIndex, i ) );
                startIndex = i + 1;
            } else if ( i == 0 && curSt.getStationName().equals( nextSt.getStationName() ) ){
                startIndex = i + 1;
            } else if ( i == path.length - 2 && curSt.getStationName().equals( nextSt.getStationName() )){
                returnList.add( getStationListBeforeTransfer(path, startIndex, path.length - 2 ) );
                return returnList;
            } else if ( i == path.length - 2 ){
                returnList.add( getStationListBeforeTransfer(path, startIndex, path.length - 1) );
                return returnList;
            }
        }
        return returnList;
    }
    private List<Station> getStationListBeforeTransfer( int[] path, int start, int end ) {
        List<Station> list = new ArrayList<>();
        for (int i = start; i <= end; i ++ ) {
            Station st = stationController.getStation(path[i]);
            // setting times...
            if ( i == start ) {
                calendar = getRealTimeAPI(st.getStationID());
            } else {
                calendar = getTimeGap(path[i - 1], path[i]);
            }
            st.setArriveTime(calendar);
            // end of setting times..
            list.add( st );
        }
        // adding Transfer time..
        calendar = getTransferTime( end );

        return list;
    }
    private Calendar getTimeGap( int stationIndex, int nextStationIndex ) {
        ArrayList<Pair<Station, Integer>>[] adj = stationController.getAdj();
        ArrayList<Pair<Station, Integer>> pairs = adj[stationIndex];

        Calendar cal = (Calendar)calendar.clone();
        for ( Pair<Station, Integer> pair : pairs ) {
            if (pair.first.getIndex() == nextStationIndex ) {
                int edgeCost = pair.second;
                cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) +  Math.round(edgeCost / 6) );
                return cal;
            }
        }
        return cal;
    }
    private Calendar getTransferTime( int stationIndex ) {
        Calendar cal = (Calendar)calendar.clone();
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 5);
        return cal;
    }
    private Calendar getRealTimeAPI( int stationID ) {
        return (Calendar)calendar.clone();
    }

    private boolean checkExpressLane( List<Station> list ) {
        if (RouteNew.isExpressStation(list.get(0).getStationID())
                && RouteNew.isExpressStation(list.get(list.size() - 1).getStationID())) {
            return true;
        }
        return false;
    }
    private List<Station> removeNotExpressStation( List<Station> list ) {
        List<Station> removed = new ArrayList<>();
        for ( Station st : list ) {
            if ( RouteNew.isExpressStation(st.getStationID()) ) removed.add( st );
        }
        return removed;
    }

    private void setMapPoint( List<Station> list ) {
        for ( Station st : list ) {
            //MapPoint Setting...
            if (st.getMapPoint() == null ) {
                st.setMapPoint(mapView.getStationPointByName( st.getStationName() ));
            }
        }
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
