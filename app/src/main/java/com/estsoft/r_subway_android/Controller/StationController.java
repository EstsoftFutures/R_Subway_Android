package com.estsoft.r_subway_android.Controller;

import android.app.Application;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by estsoft on 2016-07-13.
 */
public class StationController {

    private static final String TAG = "StationController";

    private RealmResults<RealmStation> realmStationList = null;
    private Realm mRealm = null;

    public StationController(Realm mRealm) {
        this.mRealm = mRealm;

        realmStationList = mRealm.where(RealmStation.class).findAll();
    }

//    public RealmStation getStation() {
//
//    }

}
