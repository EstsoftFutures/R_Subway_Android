package com.estsoft.r_subway_android.UI.StationInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TimeTableActivity extends AppCompatActivity {
    String TAG = "TimeTableActivity";
    StationTimetable stationTimetable;
    ArrayList<Integer> stationIDs;
    int page;
    int curStationID;
    TimetableAdapter timetableAdapter;
    RecyclerView timetableRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // StationInfoFragment로부터 stationID와 현재 page받기
        stationIDs = getIntent().getIntegerArrayListExtra("stationIDs");
        Log.d(TAG,"stationIDs: "+stationIDs.toString());
        page = getIntent().getIntExtra("page", 0);
        Log.d(TAG,"page: "+page);

        //현재 page의 id 받기
        curStationID = stationIDs.get(page);
        Log.d(TAG,"curStationID: "+curStationID);

        //timetable 정보 받기
        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(this,curStationID);
        stationTimetable = jsonTimetableParser.getStationTimetable();
        Log.d(TAG,"stationTimetable_stationName: "+stationTimetable.getStationName());
        Log.d(TAG,"stationTimetable_Upway: "+stationTimetable.getUpWay());
        Log.d(TAG,"stationTimetable_Downway: "+stationTimetable.getDownWay());
        Log.d(TAG,"stationTimetable_OrdUp: "+stationTimetable.getOrdUpWayLdx()[0]);
        Log.d(TAG,"stationTimetable_OrdDown: "+stationTimetable.getOrdDownWayLdx().length);
        Log.d(TAG,"stationTimetable_SatUp: "+stationTimetable.getSatUpWayLdx().length);
        Log.d(TAG,"stationTimetable_SatDown: "+stationTimetable.getSatDownWayLdx().length);
        Log.d(TAG,"stationTimetable_SunUp: "+stationTimetable.getSunUpWayLdx().length);
        Log.d(TAG,"stationTimetable_SunDown: "+stationTimetable.getSunDownWayLdx().length);
        Log.d(TAG,"stationTimetable_stationhour"+stationTimetable.getStationHour().size());
        Log.d(TAG,"stationTimetable_stationminute"+":"+stationTimetable.getOrdUpWayLdx().length);


        TextView ord = (TextView) findViewById(R.id.ord);
        TextView sat = (TextView) findViewById(R.id.sat);
        TextView sun = (TextView) findViewById(R.id.sun);
        final ImageView ordUnCheck =(ImageView) findViewById(R.id.ord_unchecked);
        final ImageView ordCheck = (ImageView) findViewById(R.id.ord_checked);
        final ImageView satUnCheck =(ImageView) findViewById(R.id.sat_unchecked);
        final ImageView satCheck = (ImageView) findViewById(R.id.sat_checked);
        final ImageView sunUnCheck =(ImageView) findViewById(R.id.sun_unchecked);
        final ImageView sunCheck = (ImageView) findViewById(R.id.sun_checked);

        //default: ord

        timetableAdapter = new TimetableAdapter(getApplicationContext(),stationTimetable,0);
        timetableRecyclerView = (RecyclerView) findViewById(R.id.timetable);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        timetableRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        timetableRecyclerView.setAdapter(timetableAdapter);


        ordUnCheck.setVisibility(View.GONE);
        ordCheck.setVisibility(View.VISIBLE);
        satUnCheck.setVisibility(View.VISIBLE);
        satCheck.setVisibility(View.GONE);
        sunUnCheck.setVisibility(View.VISIBLE);
        sunCheck.setVisibility(View.GONE);

        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.GONE);
                ordCheck.setVisibility(View.VISIBLE);
                satUnCheck.setVisibility(View.VISIBLE);
                satCheck.setVisibility(View.GONE);
                sunUnCheck.setVisibility(View.VISIBLE);
                sunCheck.setVisibility(View.GONE);
                timetableAdapter = new TimetableAdapter(getApplicationContext(),stationTimetable,0);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.VISIBLE);
                ordCheck.setVisibility(View.GONE);
                satUnCheck.setVisibility(View.GONE);
                satCheck.setVisibility(View.VISIBLE);
                sunUnCheck.setVisibility(View.VISIBLE);
                sunCheck.setVisibility(View.GONE);
                timetableAdapter = new TimetableAdapter(getApplicationContext(),stationTimetable,1);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.VISIBLE);
                ordCheck.setVisibility(View.GONE);
                satUnCheck.setVisibility(View.VISIBLE);
                satCheck.setVisibility(View.GONE);
                sunUnCheck.setVisibility(View.GONE);
                sunCheck.setVisibility(View.VISIBLE);
                timetableAdapter = new TimetableAdapter(getApplicationContext(),stationTimetable,2);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });

    }
}
