package com.estsoft.r_subway_android.Repository.StationRepository;

import android.content.Context;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-18.
 */
public class StationTimetable {
    private Context context;
    private int stationID;
    private String stationName;
    private String UpWay;
    private String DownWay;
    // 시간(5~25시)
    private ArrayList<Integer> stationHour = new ArrayList<>();
    private ArrayList<String>[] ordUpWayLdx = new ArrayList[20];
    private ArrayList<String>[] ordDownWayLdx = new ArrayList[20];
    private ArrayList<String>[] satUpWayLdx = new ArrayList[20];
    private ArrayList<String>[] satDownWayLdx = new ArrayList[20];
    private ArrayList<String>[] sunUpWayLdx = new ArrayList[20];
    private ArrayList<String>[] sunDownWayLdx = new ArrayList[20];

    // 시간(00행)
    private ArrayList<String> list = new ArrayList<>();

    public StationTimetable() {
        for (int i = 0; i < 20; i++) {
            stationHour.add(i + 5);
            ordUpWayLdx[i] = new ArrayList<>();
            ordDownWayLdx[i] = new ArrayList<>();

            satUpWayLdx[i] = new ArrayList<>();
            satDownWayLdx[i] = new ArrayList<>();

            sunUpWayLdx[i] = new ArrayList<>();
            sunDownWayLdx[i] = new ArrayList<>();

        }

    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getUpWay() {
        return UpWay;
    }

    public void setUpWay(String UpWay) {
        this.UpWay = UpWay;
    }

    public String getDownWay() {
        return DownWay;
    }

    public void setDownWay(String DownWay) {
        this.DownWay = DownWay;
    }


    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String>[] getOrdUpWayLdx() {
        return ordUpWayLdx;
    }

    public void setOrdUpWayLdx(ArrayList<String>[] ordUpWayLdx) {
        this.ordUpWayLdx = ordUpWayLdx;
    }

    public List<String>[] getOrdDownWayLdx() {
        return ordDownWayLdx;
    }

    public void setOrdDownWayLdx(ArrayList<String>[] ordDownWayLdx) {
        this.ordDownWayLdx = ordDownWayLdx;
    }

    public List<String>[] getSatUpWayLdx() {
        return satUpWayLdx;
    }

    public void setSatUpWayLdx(ArrayList<String>[] satUpWayLdx) {
        this.satUpWayLdx = satUpWayLdx;
    }

    public List<String>[] getSatDownWayLdx() {
        return satDownWayLdx;
    }

    public void setSatDownWayLdx(ArrayList<String>[] satDownWayLdx) {
        this.satDownWayLdx = satDownWayLdx;
    }

    public List<String>[] getSunUpWayLdx() {
        return sunUpWayLdx;
    }

    public void setSunUpWayLdx(ArrayList<String>[] sunUpWayLdx) {
        this.sunUpWayLdx = sunUpWayLdx;
    }

    public List<String>[] getSunDownWayLdx() {
        return sunDownWayLdx;
    }

    public void setSunDownWayLdx(ArrayList<String>[] sunDownWayLdx) {
        this.sunDownWayLdx = sunDownWayLdx;
    }

    public ArrayList<Integer> getStationHour() {
        return stationHour;
    }

    public void setStationHour(ArrayList<Integer> stationHour) {
        this.stationHour = stationHour;
    }
}
