package com.estsoft.r_subway_android.Parser;

import android.content.Context;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2016-07-18.
 */
public class JSONTimetableParser {

    private String TAG = "JSONTimetableParser: ";
    private Context context;
    private int stationID;
    private StationTimetable stationTimetable;
    // 시간(5~25시)

    private static ArrayList<String>[] ordUpWayLdx = new ArrayList[20];
    private static ArrayList<String>[] ordDownWayLdx = new ArrayList[20];
    private static ArrayList<String>[] satUpWayLdx = new ArrayList[20];
    private static ArrayList<String>[] satDownWayLdx = new ArrayList[20];
    private static ArrayList<String>[] sunUpWayLdx = new ArrayList[20];
    private static ArrayList<String>[] sunDownWayLdx = new ArrayList[20];




    public JSONTimetableParser(Context context, int stationID) {
        this.context = context;
        this.stationID = stationID;
        String json = getJSONFromAsset(stationID);
        Log.d(TAG, "" + json);

        for(int i=0;i<20;i++){
            ordUpWayLdx[i] = new ArrayList<>();
            ordDownWayLdx[i] = new ArrayList<>();
            satUpWayLdx[i] = new ArrayList<>();
            satDownWayLdx[i] = new ArrayList<>();
            sunUpWayLdx[i] = new ArrayList<>();
            sunDownWayLdx[i] = new ArrayList<>();
        }

        this.stationTimetable = loadJSONTOVariable(json);

        Log.d(TAG,""+stationTimetable.getOrdDownWayLdx().toString());


    }


    public StationTimetable getStationTimetable() {
        return stationTimetable;
    }

    public String getJSONFromAsset(int stationID) {
        String json = null;
        Log.d(TAG, "stationID: " + stationID);
        Log.d(TAG, "Location: " + "stationTimeList/" + String.valueOf(stationID) + ".json");
        try {
            InputStream is = context.getAssets().open("stationTimeList/" + String.valueOf(stationID) + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public StationTimetable loadJSONTOVariable(String json) {

        StationTimetable tempStationTimetable = new StationTimetable();
        Log.d(TAG, "LoadMethod: " + json);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();


        JsonObject result = jsonObject.getAsJsonObject("result");
        JsonPrimitive Name = result.getAsJsonPrimitive("Name");
//        JsonPrimitive LaneName = result.getAsJsonPrimitive("LaneName");
        JsonPrimitive UpWay = result.getAsJsonPrimitive("UpWay");
        JsonPrimitive DownWay = result.getAsJsonPrimitive("DownWay");

        Log.d(TAG, "Upway" + UpWay.toString());
        Log.d(TAG, "Upway" + UpWay.getAsString());

        tempStationTimetable.setUpWay(UpWay.getAsString());
        tempStationTimetable.setDownWay(DownWay.getAsString());
        tempStationTimetable.setStationName(Name.getAsString());

        //평일
        JsonObject ord = result.getAsJsonObject("Ord");
        JsonArray ordTimetable = ord.getAsJsonArray("TimeTable");

        for (int i = 0; i < 20; i++) {
            JsonObject ordTimeTableObject = ordTimetable.get(i).getAsJsonObject();
            // 00시
            JsonObject ordUpList = ordTimeTableObject.get("UPList").getAsJsonObject();
            Log.d(TAG, "ordUpList:trueisnull " + ordUpList.isJsonNull());
            JsonArray ordUpListValue = ordUpList.get("Value").getAsJsonArray();
            Log.d(TAG, "ordUpListValue[0]: " + ordUpListValue.get(0));
            JsonObject ordDownList = ordTimeTableObject.get("DOWNList").getAsJsonObject();
            JsonArray ordDownListValue = ordDownList.get("Value").getAsJsonArray();
            // 00분
            for (int j = 0; j < ordUpListValue.size(); j++) {
                JsonPrimitive ordUpTimeValue = ordUpListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"ordListValue"+ordUpTimeValue.getAsString());
//                Log.d("newone",""+ordUpWayLdx[i].toString());
                Log.d("newone",""+ordUpWayLdx[i]);
               ordUpWayLdx[i].add(ordUpTimeValue.getAsString());
            }

            for (int j = 0; j < ordDownListValue.size(); j++) {
                JsonPrimitive ordDownTimeValue = ordDownListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"ordListValue"+ordDownTimeValue.getAsString());
                ordDownWayLdx[i].add(ordDownTimeValue.getAsString());
            }

            Log.d(TAG,"ordupwayLDX: "+ ordUpWayLdx[i].toString());

        }

        Log.d(TAG," "+ ordUpWayLdx.toString());
        tempStationTimetable.setOrdUpWayLdx(ordUpWayLdx);
        tempStationTimetable.setOrdDownWayLdx(ordDownWayLdx);
        Log.d(TAG,"upwayldxinstation: "+tempStationTimetable.getOrdUpWayLdx()[0].toString());


        //토요일
        JsonObject sat = result.getAsJsonObject("Sat");
        JsonArray satTimetable = sat.getAsJsonArray("TimeTable");

        for (int i = 0; i < 20; i++) {
            JsonObject satTimeTableObject = satTimetable.get(i).getAsJsonObject();
            // 00시
            JsonObject satUpList = satTimeTableObject.get("UPList").getAsJsonObject();
            JsonArray satUpListValue = satUpList.get("Value").getAsJsonArray();
            JsonObject satDownList = satTimeTableObject.get("DOWNList").getAsJsonObject();
            JsonArray satDownListValue = satDownList.get("Value").getAsJsonArray();
            // 00분
            for (int j = 0; j < satUpListValue.size(); j++) {
                JsonPrimitive satUpTimeValue = satUpListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"satListValue"+satUpTimeValue.getAsString());
                satUpWayLdx[i].add(satUpTimeValue.getAsString());
            }

            for (int j = 0; j < satDownListValue.size(); j++) {
                JsonPrimitive satDownTimeValue = satDownListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"satListValue"+satDownTimeValue.getAsString());
               satDownWayLdx[i].add(satDownTimeValue.getAsString());
            }

        }

        tempStationTimetable.setSatUpWayLdx(satUpWayLdx);
        tempStationTimetable.setSatDownWayLdx(satDownWayLdx);


        //일요일
        JsonObject sun = result.getAsJsonObject("Sun");
        JsonArray sunTimetable = sun.getAsJsonArray("TimeTable");

        for (int i = 0; i < 20; i++) {
            JsonObject sunTimeTableObject = sunTimetable.get(i).getAsJsonObject();
            // 00시
            JsonObject sunUpList = sunTimeTableObject.get("UPList").getAsJsonObject();
            JsonArray sunUpListValue = sunUpList.get("Value").getAsJsonArray();
            JsonObject sunDownList = sunTimeTableObject.get("DOWNList").getAsJsonObject();
            JsonArray sunDownListValue = sunDownList.get("Value").getAsJsonArray();
            // 00분
            for (int j = 0; j < sunUpListValue.size(); j++) {
                JsonPrimitive sunUpTimeValue = sunUpListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"sunListValue"+sunUpTimeValue.getAsString());
                sunUpWayLdx[i].add(sunUpTimeValue.getAsString());
            }

            for (int j = 0; j < sunDownListValue.size(); j++) {
                JsonPrimitive sunDownTimeValue = sunDownListValue.get(j).getAsJsonPrimitive();
                Log.d(TAG,"sunDownListValue"+sunDownTimeValue.getAsString());
               sunDownWayLdx[i].add(sunDownTimeValue.getAsString());
            }
        }

        tempStationTimetable.setSunUpWayLdx(sunUpWayLdx);
        tempStationTimetable.setSunDownWayLdx(sunDownWayLdx);

        return tempStationTimetable;


    }



}