package com.estsoft.r_subway_android.Controller;

import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Parser.StationTag;
import com.estsoft.r_subway_android.Parser.TtfXmlParserCost;
import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by estsoft on 2016-07-13.
 */
public class StationController {

    private static final String TAG = "StationController";

    private static final int SHORT_ROUTE_WEIGHT = 0;
    private static final int MINIMUM_TRANSFER_WEIGHT = 500;
    private int USING_WEIGHT;

    private RealmResults<RealmStation> realmStationList = null;
    private Realm mRealm = null;

    private List<Station> deepCopiedStations = null;


    private ArrayList<Pair<Station, Integer>> shortestPathAdj[] = null;
    private ArrayList<Pair<Station, Integer>> minTransferAdj[] = null;
    private ArrayList<Pair<Station, Integer>> customAdj[] = null;
    private TtfXmlParserCost costParser;
    private InputStream inputStream;

    public StationController(Realm mRealm, InputStream in) throws IOException , XmlPullParserException {
        this.mRealm = mRealm;
        this.inputStream = in;
        realmStationList = mRealm.where(RealmStation.class).findAll();

        deepCopyRealmStation();

        shortestPathAdj = initializeAdj(SHORT_ROUTE_WEIGHT);
        minTransferAdj = initializeAdj(MINIMUM_TRANSFER_WEIGHT);
        customAdj = initializeAdj(SHORT_ROUTE_WEIGHT);

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
            }

            if ( adj[i].size() == 0 ) {
                Log.d(TAG, "StationController: " + station.getStationName() );
            }
//            Log.d(TAG, "StationController: " + adj[i].get(adj[i].size() - 1).second);
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
        return exStations;
    }
    public List<Station> getExStations( SemiStation semiStation ) {
        return null;
    }

    public ArrayList<Pair<Station, Integer>>[] getShortestPathAdj() {
        return shortestPathAdj;
    }

    public ArrayList<Pair<Station, Integer>>[] getMinTransferAdj() {
        return minTransferAdj;
    }

    public ArrayList<Pair<Station, Integer>>[] getCustomAdj() {
        return customAdj;
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
