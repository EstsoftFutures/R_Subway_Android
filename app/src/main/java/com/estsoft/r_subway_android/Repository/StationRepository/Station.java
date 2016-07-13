package com.estsoft.r_subway_android.Repository.StationRepository;

import android.graphics.PointF;

/**
 * Created by estsoft on 2016-06-30.
 */
public class Station extends TtfNode {


    private PointF geoPoint = null;
    private PointF mapPoint = null;
    private String stationName = "";
    private int stationId;

    private Station(int conLevel, String stationId1, String stationId2) {
        super(conLevel, stationId1, stationId2);
    }

    public Station( int conLevel, String stationId1, PointF geoPoint ) {
        this( conLevel, stationId1, (String)null );
        this.geoPoint = geoPoint;
    }

    public PointF getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(PointF geoPoint) {
        this.geoPoint = geoPoint;
    }

    public PointF getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(PointF mapPoint) {
        this.mapPoint = mapPoint;
    }

    public String getStationName() {        return stationName;    }

    public void setStationName(String stationName) {        this.stationName = stationName;    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }
}

