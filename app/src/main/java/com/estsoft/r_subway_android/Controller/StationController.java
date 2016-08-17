package com.estsoft.r_subway_android.Controller;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.Parser.StationTag;
import com.estsoft.r_subway_android.Parser.TtfXmlParserCost;
import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by estsoft on 2016-07-13.
 */
public class StationController {

    private static final String TAG = "StationController";

    private static final int SHORT_ROUTE_WEIGHT = 0;

    private RealmResults<RealmStation> realmStationList = null;
    private Realm mRealm = null;

    private List<Station> deepCopiedStations = null;


    private ArrayList<Pair<Station, Integer>> adj[] = null;
    private TtfXmlParserCost costParser;
    private InputStream inputStream;

    private Context mContext;

    public StationController(Realm mRealm, InputStream in, Context context) throws IOException , XmlPullParserException {
        this.mRealm = mRealm;
        this.inputStream = in;
        realmStationList = mRealm.where(RealmStation.class).findAll();

        deepCopyRealmStation();

        adj = initializeAdj(SHORT_ROUTE_WEIGHT);

        mContext = context;

    }

    private ArrayList<Pair<Station, Integer>>[] initializeAdj(int transferWeight) throws IOException , XmlPullParserException {
        if (costParser == null) {
            costParser = new TtfXmlParserCost(inputStream);
//            stationTags = costParser.getStationTags();
        }

        ArrayList<Pair<Station, Integer>> adj[] = new ArrayList[ deepCopiedStations.size() ];

        for (int i = 0; i < adj.length; i++) {

            adj[i] = new ArrayList<>();
            Station station = deepCopiedStations.get(i);
            String laneType = station.getLaneType() + "";

            StationTag stationTag = findMatchedStationTag(station.getStationID(), laneType );

//                Log.d(TAG, "initializeAdj: " + station.getLaneName() + station.getStationName());

            for (int j = 0; j < station.getPrevStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: PREV " + station.getPrevStations().get(j).getStationName());
                int cost = Integer.parseInt(stationTag.getPrevCost().get(j));
                if ( !(cost < 0) ){
                    adj[i].add(new Pair<Station, Integer>(station.getPrevStations().get(j), cost));
                }
            }
            for (int j = 0; j < station.getNextStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: NEXT " + station.getNextStations().get(j).getStationName());
                int cost = Integer.parseInt(stationTag.getNextCost().get(j));
                if (!(cost < 0) ) {
                    adj[i].add(new Pair<Station, Integer>(station.getNextStations().get(j), cost));
                }
            }
            for (int j = 0; j < station.getExStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: EX " + station.getExStations().get(j).getStationName());
                adj[i].add(new Pair<Station, Integer>(station.getExStations().get(j), transferWeight));
                Log.d(TAG, "initializeAdj: " + transferWeight);
            }

            if ( adj[i].size() == 0 ) {
                Log.d(TAG, "StationController: " + station.getStationName() );
            } else {
                Log.d(TAG, "StationController: " + adj[i].get(adj[i].size() - 1).second);
            }
        }
        return adj;
    }

    private StationTag findMatchedStationTag( int stationId, String laneType ) {
        List<StationTag> boundary = costParser.getStationTags( Integer.parseInt(laneType) );
        for ( int i = 0; i < boundary.size(); i ++ ) {
            if (stationId == Integer.parseInt( boundary.get(i).getId() )) {
                return boundary.get(i);
            }
        }
        Log.d(TAG, "initializeAdj: NULL!! " + stationId );
        return null;
    }

    public Station getStation(SemiStation semiStation) {

        // Sever Communication
        int conLevel = 0;
        // API

        //Realm
//        RealmStation matchRealmStation = null;
//        for ( RealmStation rst : realmStationList ) {
//            if ( rst.getStationName() == semiStation.getIntId() ) {
//                matchRealmStation = rst;
//                break;
//            }
//        }
//
//        Station station = new Station( matchRealmStation, semiStation.getPosition(), getConLevel(semiStation.getIntId()) );

        Station station = null;
        for ( Station ss : deepCopiedStations ) {
            if ( semiStation.getIntId() == ss.getStationID() ) {
                ss.setMapPoint(semiStation.getPosition());
                return ss;
            }
        }

        return null;

    }

//    public Station getLiteStation( SemiStation semiStation ) {
//        // Sever Communication
//        int conLevel = 0;
//        // API
//
//        //Realm
//        RealmStation matchRealmStation = null;
//        for ( RealmStation rst : realmStationList ) {
//            if ( rst.getStationName() == semiStation.getIntId() ) {
//                matchRealmStation = rst;
//                break;
//            }
//        }
//
//        Station station = new Station( matchRealmStation, getConLevel(semiStation.getIntId()) );
//
//        return  station;
//    }

//    public List<Station> getExStations( SemiStation semiStation ) {
//        List<Station> ExStations = new ArrayList<>();
//        RealmStation seedRealmStation = getRealmStationByID( semiStation.getIntId() );
//        ExStations.add( new Station(seedRealmStation, semiStation.getPosition(), getConLevel(semiStation.getIntId())) );
//        for ( RealmStation rst : seedRealmStation.getExStations() ) {
//            ExStations.add( new Station(rst, semiStation.getPosition(), getConLevel(semiStation.getIntId())) );
//        }
//
//        return ExStations;
//    }

//    public List<Station> getStationList (List<SemiStation> semiStationList) {
//        List<Station> stationList = new ArrayList<>();
//        for ( SemiStation ss : semiStationList ) {
//            stationList.add(getLiteStation( ss ));
//        }
//        return stationList;
//    }

    public Station getDeepStation ( int id ) {
        for ( Station st : deepCopiedStations ) {
            if (st.getStationID() == id) return st;
        }
        return null;
    }

    public void setSemiStationLaneNumber( List<SemiStation> ssl ){
        for ( SemiStation ss : ssl ) {
            ss.setLaneNumbers(getExNumbers( ss ));
            Log.d(TAG, "setSemiStationLaneNumber: " + ss.getName() + " // " + ss.getLaneNumbers() );
        }
    }
    public List<Integer> getExNumbers ( SemiStation semiStation ){
        List<Integer> exNumbers = new ArrayList<>();
        //아래 한줄은 XML index 를 Station Index 랑 싱크맞추면 빠름
        Station st = getDeepStation( semiStation.getIntId() );
        exNumbers.add( st.getLaneType() );
        Log.d(TAG, "getExNumbers: " + st.getStationName() + " // " + st.getLaneName());
        for ( int i = 0; i < st.getExStations().size(); i ++ ) {
            Log.d(TAG, "getExNumbers: " + st.getExStations().get(i).getStationName() + " // " + st.getExStations().get(i).getLaneName());
            exNumbers.add( st.getExStations().get(i).getLaneType() );
        }
        return exNumbers;
    }

    private RealmStation getRealmStationByID( int ID ) {
        RealmStation matchRealmStation = null;
        for ( RealmStation rst : realmStationList ) {
            Log.d(TAG, "getRealmStationByID: " + rst.getStationID());
            Log.d(TAG, "getRealmStationByID: ID = " + ID);
            if ( rst.getStationID() == ID ) {
                matchRealmStation = rst;
                break;
            }
        }
        Log.d(TAG, "getRealmStationByID: ---------------------------------------------------------- " );
        return  matchRealmStation;
    }

    private void deepCopyRealmStation(){

        deepCopiedStations = new ArrayList<>();

        for ( RealmStation rst : realmStationList ) {
            Station st = new Station( rst, null, getConLevel( rst.getStationID()) );
            deepCopiedStations.add( st );
            Log.e(TAG, "deepCopyRealmStation: ," + st.getStationID() +", " +st.getStationName() + ", " + st.getAddress() );
        }
//        for ( Station st : deepCopiedStations ) {
//            Log.d(TAG, "deepCopyRealmStation: " + st.getIndex());
//            if (st.getPrevStations().size() != 0)
//            Log.d(TAG, "deepCopyRealmStation: " +st.getStationName() + " / " +  st.getPrevStations().get(0).getIndex() );
//        }
    }


    public List<Station> getExStations( Station station ) {
        int debugCount = 0;
        List<Station> exStations = new ArrayList<>();
        exStations.add(station);
        List<Integer> stationIDs = station.getExStationIDs();
        for ( Station st : deepCopiedStations ) {
            for ( Integer exStID : stationIDs ) {
                debugCount ++;
                if (st.getStationID() == exStID ) {
                    exStations.add( st );
                    break;
                }
            }
        }
        Log.d(TAG, "getExStations: for count = " + debugCount);

        for ( Station stationForTime : exStations ) {
            stationForTime.setTimeStringList(getPrevNextStationTime( stationForTime ));
        }

        return exStations;
    }
    public List<String> getPrevNextStationTime( Station station ) {
        List<String> result = new ArrayList<>();

        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(mContext, station.getStationID());
        StationTimetable stt = jsonTimetableParser.getStationTimetable();

        //지금 시간 세팅
        Calendar newCal = new GregorianCalendar();
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        String prevKey, nextKey;
        ArrayList<HashMap<String, Object>>[] prevTimeTable, nextTimeTable;
        switch ( day ) {
            case 1 :
                prevTimeTable =  stt.getSunUpWayLdx(); nextTimeTable = stt.getSunDownWayLdx();
                prevKey = "sunUpWayLdx"; nextKey = "sunDownWayLdx"; break;
            case 7 :
                prevTimeTable =  stt.getSatUpWayLdx(); nextTimeTable = stt.getSatDownWayLdx();
                prevKey = "satUpWayLdx"; nextKey = "satDownWayLdx"; break;
            default :
                prevTimeTable =  stt.getOrdUpWayLdx(); nextTimeTable = stt.getOrdDownWayLdx();
                prevKey = "ordUpWayLdx"; nextKey = "ordDownWayLdx"; break;
        }

        int hour = newCal.get(Calendar.HOUR_OF_DAY);
        int hourIndex = hour - 5 < 0 ? hour - 5 + 19 : hour - 5;
        ArrayList<HashMap<String, Object>> prevTimeList = prevTimeTable[hourIndex];
        ArrayList<HashMap<String, Object>> nextTimeList = nextTimeTable[hourIndex];

        if (station.getPrevStationIDs().size() > 0) {
            Map prevFirst = getCalendar(prevTimeList, prevKey, (Calendar) newCal.clone());
            Calendar prevCalendarFirst = (Calendar) prevFirst.get("calendar");
            String prevTerminalFirst = (String) prevFirst.get("terminal");

            Map prevSecond = getCalendar(prevTimeList, prevKey, (Calendar) prevCalendarFirst.clone());
            Calendar prevCalendarSecond = (Calendar) prevSecond.get("calendar");
            String prevTerminalSecond = (String) prevSecond.get("terminal");

            int timeGapFirst = prevCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) timeGapFirst += 60;
            result.add( prevTerminalFirst +"행 " + timeGapFirst +"분 후 ");

            int timeGapSecond = prevCalendarSecond.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapSecond < 0) timeGapSecond += 60;
            result.add( prevTerminalSecond +"행 " + timeGapSecond +"분 후 ");

//            Log.d(TAG, "getPrevNextStationTime: " + prevCalendarFirst.get(Calendar.HOUR) + ":" + prevCalendarFirst.get(Calendar.MINUTE) + " to " + prevTerminalFirst );
//            Log.d(TAG, "getPrevNextStationTime: " + prevCalendarSecond.get(Calendar.HOUR) + ":" + prevCalendarSecond.get(Calendar.MINUTE) + " to " + prevTerminalSecond );
        } else {
            result.add("-"); result.add("-");
        }

        if (station.getNextStationIDs().size() > 0) {
            Map nextFirst = getCalendar(nextTimeList, nextKey, (Calendar) newCal.clone());
            Calendar nextCalendarFirst = (Calendar) nextFirst.get("calendar");
            String nextTerminalFirst = (String) nextFirst.get("terminal");

            Map nextSecond = getCalendar(nextTimeList, nextKey, (Calendar) nextCalendarFirst.clone());
            Calendar nextCalendarSecond = (Calendar) nextSecond.get("calendar");
            String nextTerminalSecond = (String) nextSecond.get("terminal");

            int timeGapFirst = nextCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) timeGapFirst += 60;
            result.add( nextTerminalFirst +"행 " + timeGapFirst +"분 후 ");

            int timeGapSecond = nextCalendarSecond.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapSecond < 0) timeGapSecond += 60;
            result.add( nextTerminalSecond +"행 " + timeGapSecond +"분 후 ");

//            Log.d(TAG, "getPrevNextStationTime: " + nextCalendarFirst.get(Calendar.HOUR) + ":" + nextCalendarFirst.get(Calendar.MINUTE) + " to " + nextTerminalFirst );
//            Log.d(TAG, "getPrevNextStationTime: " + nextCalendarSecond.get(Calendar.HOUR) + ":" + nextCalendarSecond.get(Calendar.MINUTE) + " to " + nextTerminalSecond );
        }

        return result;
    }
    private Map getCalendar( ArrayList<HashMap<String, Object>> timeList, String key, Calendar cal ) {
        Log.d(TAG, "getCalendar: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
        Map< String, Object > result = new HashMap<>();
        String terminalName = "";
        int timeMinute = 0;
        boolean flag = false ;
        int minute = cal.get(Calendar.MINUTE);
        Log.d(TAG, "getCalendar: " + timeList.size());
        for ( int i = 0; i < timeList.size(); i ++ ) {
            HashMap<String, Object> timeMap = timeList.get(i);
            String timeString[] =  ((String) timeMap.get(key)).split("\\(");
            timeMinute = Integer.parseInt(timeString[0]);
            Log.d( TAG, "getCalendar: " + i + "//// Size " + timeList.size() + " / " + timeMinute + " / " + minute );
            if ( timeMinute > minute ) {
                terminalName = timeString[1].replace(")", "");
                cal.set(Calendar.MINUTE, timeMinute);
                flag = true;
                break;
            }
        }

        if ( flag ) {
            cal.set(Calendar.MINUTE, timeMinute);
            result.put("calendar", cal);
            result.put("terminal", terminalName);
            return result;
        }
        else {
            cal.set(Calendar.MINUTE, 60);
//            cal.set(Calendar.MINUTE, 0);
            return getCalendar( timeList, key, cal );
        }

    }

    public ArrayList<Pair<Station, Integer>>[] getAdj() {
        return adj;
    }

    public Station getStation(int index ) {
        return deepCopiedStations.get(index);
    }



    //Server Communication
    private int getConLevel ( int ID ) {
        // 머신러닝 후 적용!
        return 0;
    }



}
