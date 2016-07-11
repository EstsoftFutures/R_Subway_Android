package com.estsoft.r_subway_android.Repository.StationRepository;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by estsoft on 2016-06-30.
 */
public class SemiStation {

    private String id = "";
    private int laneNumber = -1;
    private PointF position = null;
    private String name = "";

    public SemiStation(String id, int laneNumber, PointF position, String name) {
        this.id = id;
        this.laneNumber = laneNumber;
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
        Log.e("TEST", position.toString());
    }


}
