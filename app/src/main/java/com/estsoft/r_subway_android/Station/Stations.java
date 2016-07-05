package com.estsoft.r_subway_android.Station;

import android.util.Log;

import com.estsoft.r_subway_android.Parser.CircleTag;
import com.estsoft.r_subway_android.Parser.TtfXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-22.
 */
public class Stations {

    private final static String TAG = "stations";

    private InputStream inputStream = null;
    private List<Station> stationList = null;
    private int mapWidth = 0;
    private int mapHeight = 0;

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Stations(InputStream inputStream) throws IOException, XmlPullParserException {
        this.inputStream = inputStream;
        this.stationList = new ArrayList<>();
        TtfXmlParser parser = new TtfXmlParser( inputStream );

        for ( CircleTag circle : parser.getCircleList() ) {
            Station station = new Station( circle.getPositionX(), circle.getPositionY(), circle.getName(), circle.getOtherFactor() );
            stationList.add( station );
        }

        mapWidth = parser.getSvgWidth();
        mapHeight = parser.getSvgHeight();

        Log.d(TAG, mapWidth + " / " + mapHeight);

    }

    public List<Station> getStationList() {
        return stationList;
    }

    public Station getStationByName(String stationName ) {
        for ( Station station : stationList ) {
            if (station.getName().equals( stationName )) return station;
        }
        return new Station(0f,0f, "nullStation", "");
    }

    @Override
    public String toString() {
        String stationListString = "";
        for ( Station station : this.stationList )  {
            stationListString = stationListString + station.toString() + " \n";
        }
        return "Stations{" +
                "stationList=" + stationListString +
                '}';
    }
}
