package com.estsoft.r_subway_android.Repository.StationRepository;

import java.util.List;

/**
 * Created by estsoft on 2016-06-30.
 */
public class Route extends TtfNode {

    List<TtfNode> stationList = null;

    public Route(int conLevel, String stationId1, String stationId2) {
        super(conLevel, stationId1, stationId2);
    }

    public Route(int conLevel, String stationId1, String stationId2, List<TtfNode> stationList ){
        this(conLevel, stationId1, stationId2);
        this.stationList = stationList;
    }

    public List<TtfNode> getStationList() {
        return stationList;
    }

    public void setStationList(List<TtfNode> stationList) {
        this.stationList = stationList;
    }
}
