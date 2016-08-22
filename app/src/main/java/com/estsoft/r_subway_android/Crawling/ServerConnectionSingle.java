package com.estsoft.r_subway_android.Crawling;

import android.os.AsyncTask;

import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.estsoft.r_subway_android.UI.StationInfo.RecyclerViewAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by estsoft on 2016-08-18.
 */
public class ServerConnectionSingle {
    private static ServerConnectionSingle ourInstance = new ServerConnectionSingle();

    public static ServerConnectionSingle getInstance() {
        return ourInstance;
    }

    public ServerConnectionSingle() {
    }

    private static final String TAG = "callAdapter";

    public static List<AsyncTask> tasks = new ArrayList<>();

    public static int INTERNET_DISCONNECTED = -1000;
    public static int INTERNET_GOOD = -1006;

    public static int ACCIDENT_TRUE = -1001;
    public static int ACCIDENT_FALSE = -1002;
    public static int SERVER_CONNECTION_FAILED = -1003;

    public static int NONE_EXIST_STATION = -1004;

    public static int UNIDENTIFIED_ERROR = -1005;

    public static int CON_RED = 1000;
    public static int CON_YELLOW = 1001;
    public static int CON_GREEN = 1002;

    private Integer accidentStatus = null;
    private Integer congestionStatus = null;
    private Integer congestionColor = null;

    private Station mStation = null;
    private RecyclerViewAdapter mAdapter = null;

    public void setStationInfo(Station station, RecyclerViewAdapter adapter) {
        mStation = station;
        mAdapter = adapter;
        getAccidentInfo();
    }

    private void setUserInterface() {
        congestionColor = getRYGByCongestion(congestionStatus);
        callAdapter(INTERNET_GOOD);
    }

    private int getRYGByCongestion(int congestion) {
        if (congestion != NONE_EXIST_STATION) {
            //!!!!!!!!!!!!!!!!!!!!!!!!!hour error
            int movePerson = congestion / (mStation.getTrainsPerHour() * 10 * 4);
            // return Red
            if (movePerson > 12) return CON_RED;
                // return Green
            else if (movePerson < 7) return CON_GREEN;
                // return Yellow
            else return CON_YELLOW;
        }
        return CON_GREEN;
    }

    private void getAccidentInfo() {
        AccidentTask at = new AccidentTask();
        if (InternetManager.getInstance().checkNetwork()) {
            at.execute();
            tasks.add(at);
        } else callAdapter(INTERNET_DISCONNECTED);
    }

    private void getCongestionInfo() {
        CongestionTask ct = new CongestionTask();
        if (InternetManager.getInstance().checkNetwork()) {
            ct.execute();
            tasks.add(ct);
        } else callAdapter(INTERNET_DISCONNECTED);
    }

    public static void killThread() {
        while (tasks.size() > 0) {
            tasks.get(0).cancel(true);
            tasks.remove(0);
            Log.d(TAG, "killThread: TreadKilled");
        }
    }

    private void callAdapter(int internetStatus) {
        if (internetStatus == INTERNET_GOOD) {
            mAdapter.setStationStatus(internetStatus, accidentStatus, congestionStatus, congestionColor, mStation);
            Log.d(TAG, "callAdapter: " + mStation.getStationID() + " / " + mStation.getLaneName() + " / " + mStation.getStationName() + " accident : " + accidentStatus + " congestionStatus : " + congestionStatus);
        } else {
            mAdapter.setStationStatus(internetStatus, 0, 0, 0, mStation);
            Log.d(TAG, "callAdapter: INTERNET_DISCONNECTED");
        }

    }


    private class CongestionTask extends AsyncTask<Void, Void, Integer> {
        private static final String TAG = "CongestionTask";

        @Override
        protected Integer doInBackground(Void... params) {

            String[] dateInfo = DateManager.getInstance().getDayAndTime().split("-");

            Log.d(TAG, "doInBackground: " + dateInfo[0] + " _ " + dateInfo[1] + " _ " + dateInfo[2]);

            try {
                MongoClientURI mongoUri = new MongoClientURI("mongodb://222.239.250.207:11012");
                MongoClient mongoClient = new MongoClient(mongoUri);
                MongoDatabase db = mongoClient.getDatabase("congestion");
                MongoCollection<Document> collection = db.getCollection("data");

                Document myDoc = collection.find(Filters.and(Filters.eq("date", dateInfo[0]), Filters.eq("isRedDay", dateInfo[1]), Filters.eq("stationID", String.valueOf(mStation.getStationID())))).first();
//                Document myDoc = collection.find(Filters.and(Filters.eq("date", "0"), Filters.eq("isRedDay", "0"), Filters.eq("stationID", String.valueOf(mStation.getStationID()) ))).first();

                Log.d(TAG, "doInBackground: " + myDoc.toJson());
                JSONObject jsonObject = new JSONObject(myDoc.toJson());
                return Integer.parseInt((String) jsonObject.get(dateInfo[2]));
//                return Integer.parseInt((String)jsonObject.get("14"));
            } catch (JSONException ex) {
                ex.printStackTrace();
                return NONE_EXIST_STATION;
            } catch (Exception ex) {
                ex.printStackTrace();
                return NONE_EXIST_STATION;
            }
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            congestionStatus = integer;
            setUserInterface();
            this.cancel(true);
            tasks.remove(this);
        }


    }

    private class AccidentTask extends AsyncTask<Void, Void, Integer> {
        private static final String TAG = "AccidentTask";
        private String serverAddr = "222.239.250.207";
        private int port = 11011;

        @Override
        protected Integer doInBackground(Void... params) {
            Socket socket = null;
            String accidentInfo = null;
            try {
                socket = new Socket(serverAddr, port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF(mStation.getStationName());
                outputStream.flush();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                accidentInfo = input.readLine();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                accidentInfo = "Server Connection Fail";
            } catch (Exception ex) {
                ex.printStackTrace();
                accidentInfo = "Server Connection Fail";
            }
            if (accidentInfo.equals("T")) return ACCIDENT_TRUE;
            else if (accidentInfo.equals("F")) return ACCIDENT_FALSE;
            return SERVER_CONNECTION_FAILED;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            accidentStatus = integer;
            Log.d(TAG, "onPostExecute: " + integer);
            getCongestionInfo();
            this.cancel(true);
            tasks.remove(this);
        }
    }


}
