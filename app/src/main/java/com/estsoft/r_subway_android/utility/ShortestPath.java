package com.estsoft.r_subway_android.utility;


import android.support.v4.util.Pair;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.util.ArrayList;


/**
 * Created by gangGongUi on 2016. 7. 19..
 */
public class ShortestPath
{
    static
    {
        System.loadLibrary("BstarShortestPath");
    }

    /**
     * TODO Return the shortest path as an integer array receives input the departure station and arrival station.
     * @param adj Subway station linked list representation.
     * @param start Departure station.
     * @param end Arrival station.
     * @return int[] Shortest route expressed in index.
     * */
    public static native int[] getShortestPathByIntArray(ArrayList<Pair<Station, Integer>> adj[], Station start, Station end);

    /**
     * TODO Return the minimum transfer path as an integer array receives input the departure station and arrival station.
     * @param adj Subway station linked list representation.
     * @param start Departure station.
     * @param end Arrival station.
     * @return int[] minimum transfer route expressed in index.
     * */
    public static native int[] getMinimumTransferPathByIntArray(ArrayList<Pair<Station, Integer>> adj[],  Station start, Station end);



}
