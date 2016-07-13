package com.estsoft.r_subway_android.Repository.StationRepository;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.estsoft.r_subway_android.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

/**
 * Created by estsoft on 2016-07-13.
 */
public class InitializeRealm {
    private Context context;
    private Realm realm;

    public InitializeRealm(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public String getJSONFromAsset(int stationID) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("stationInfo/" + String.valueOf(stationID) + ".json");
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

    public void loadJSONToRealm(String json, int index) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        JsonObject result = jsonObject.getAsJsonObject("result");
        JsonPrimitive stationID = result.getAsJsonPrimitive("stationID");
        JsonPrimitive stationName = result.getAsJsonPrimitive("stationName");
        JsonPrimitive laneType = result.getAsJsonPrimitive("type");
        JsonPrimitive laneName = result.getAsJsonPrimitive("laneName");

        JsonObject defaultInfo = result.getAsJsonObject("defaultInfo");
        JsonPrimitive address = defaultInfo.getAsJsonPrimitive("address");
        JsonPrimitive tel = defaultInfo.getAsJsonPrimitive("tel");

        JsonObject useInfo = result.getAsJsonObject("useInfo");
        JsonPrimitive platform = useInfo.getAsJsonPrimitive("platform");
        JsonPrimitive meetingPlace = useInfo.getAsJsonPrimitive("meetingPlace");
        JsonPrimitive restroom = useInfo.getAsJsonPrimitive("restroom");
        JsonPrimitive offDoor = useInfo.getAsJsonPrimitive("offDoor");
        JsonPrimitive crossOver = useInfo.getAsJsonPrimitive("crossOver");
        JsonPrimitive publicPlace = useInfo.getAsJsonPrimitive("publicPlace");
        JsonPrimitive handicapCount = useInfo.getAsJsonPrimitive("handicapCount");
        JsonPrimitive parkingCount = useInfo.getAsJsonPrimitive("parkingCount");
        JsonPrimitive bicycleCount = useInfo.getAsJsonPrimitive("bicycleCount");
        JsonPrimitive civilCount = useInfo.getAsJsonPrimitive("civilCount");

//        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmStation station = realm.createObject(RealmStation.class);
        station.setIndex(index);
        station.setStationID(stationID.getAsInt());
        station.setStationName(stationName.getAsString());
        station.setLaneType(laneType.getAsInt());
        station.setLaneName(laneName.getAsString());
        station.setAddress(address.getAsString());
        station.setTel(tel.getAsString());
        station.setPlatform(platform.getAsInt());
        station.setMeetingPlace(meetingPlace.getAsInt());
        station.setRestroom(restroom.getAsInt());
        station.setOffDoor(offDoor.getAsInt());
        station.setCrossOver(crossOver.getAsInt());
        station.setPublicPlace(publicPlace.getAsInt());
        station.setHandicapCount(handicapCount.getAsInt());
        station.setParkingCount(parkingCount.getAsInt());
        station.setBicycleCount(bicycleCount.getAsInt());
        station.setCivilCount(civilCount.getAsInt());
        realm.commitTransaction();
    }

    public void connectStations() {
        for(int i=100; i<=20138; i++) {
            String json = getJSONFromAsset(i);
            if (json != null) {
//                realm = Realm.getDefaultInstance();
                RealmStation station = realm.where(RealmStation.class).equalTo("stationID", i).findFirst();
                Log.d("\\\\", String.valueOf(station.getStationID()));
                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                JsonObject result = jsonObject.getAsJsonObject("result");
                JsonObject exOBJ = result.getAsJsonObject("exOBJ");
                if (exOBJ != null) {
                    JsonArray exStations = exOBJ.getAsJsonArray("station");
                    for (JsonElement ex : exStations) {
                        int exStationID = ex.getAsJsonObject().getAsJsonPrimitive("stationID").getAsInt();
                        RealmStation exStation = realm.where(RealmStation.class).equalTo("stationID", exStationID).findFirst();
                        Log.d("\\\\", String.valueOf(exStation.getStationID()));

                        realm.beginTransaction();
                        station.getExStations().add(exStation);
                        realm.commitTransaction();

                    }
                }

                JsonObject prevOBJ = result.getAsJsonObject("prevOBJ");
                if(prevOBJ != null) {
                    JsonArray prevStations = prevOBJ.getAsJsonArray("station");
                    for(JsonElement prev : prevStations) {
                        int prevStationID = prev.getAsJsonObject().getAsJsonPrimitive("stationID").getAsInt();
                        RealmStation prevStation = realm.where(RealmStation.class).equalTo("stationID", prevStationID).findFirst();
                        Log.d("\\\\", String.valueOf(prevStation.getStationID()));

                        realm.beginTransaction();
                        station.getPrevStations().add(prevStation);
                        realm.commitTransaction();
                    }
                }

                JsonObject nextOBJ = result.getAsJsonObject("nextOBJ");
                if(nextOBJ != null) {
                    JsonArray nextStations = nextOBJ.getAsJsonArray("station");
                    for(JsonElement next : nextStations) {
                        int nextStationID = next.getAsJsonObject().getAsJsonPrimitive("stationID").getAsInt();
                        RealmStation nextStation = realm.where(RealmStation.class).equalTo("stationID", nextStationID).findFirst();
                        Log.d("\\\\", String.valueOf(nextStation.getStationID()));

                        realm.beginTransaction();
                        station.getNextStations().add(nextStation);
                        realm.commitTransaction();
                    }
                }
            }
        }
    }
}
