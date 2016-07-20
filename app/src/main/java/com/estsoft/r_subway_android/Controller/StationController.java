package com.estsoft.r_subway_android.Controller;

import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by estsoft on 2016-07-13.
 */
public class StationController {

    private static final String TAG = "StationController";

    private RealmResults<RealmStation> realmStationList = null;
    private Realm mRealm = null;

    private List<Station> deepCopiedStations = null;

    private List<Pair<Station, Integer>> adj[] = null;

    public StationController(Realm mRealm) {
        this.mRealm = mRealm;

        realmStationList = mRealm.where(RealmStation.class).findAll();

        deepCopyRealmStation();

        adj = new ArrayList[deepCopiedStations.size()];
        initializeAdj();
//        for(int i = 0 ; i < adj.length; i ++ ) {
//            Log.d(TAG, "StationController: " + deepCopiedStations.get(i).getStationName() + " / " + adj[i].size());
//        }

    }

    private void initializeAdj(){
        int distance = 0;
        for ( int i = 0; i < adj.length ; i ++ ) {
            adj[i] = new ArrayList<>();
            for ( Station st : deepCopiedStations.get(i).getPrevStations() ) {
                adj[i].add(new Pair<Station, Integer>( st, distance ));
            }
            for ( Station st : deepCopiedStations.get(i).getNextStations() ) {
                adj[i].add(new Pair<Station, Integer>( st, distance ));
            }
            for ( Station st : deepCopiedStations.get(i).getExStations() ) {
                adj[i].add(new Pair<Station, Integer>( st, distance ));
            }
        }
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

    //Server Communication
    private int getConLevel ( int ID ) {
        // 머신러닝 후 적용!
        return 0;
    }

}
