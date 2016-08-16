package com.estsoft.r_subway_android.UI.StationInfo;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.Crawling.InternetManager;
import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;
import com.estsoft.r_subway_android.listener.ServerConnectionListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private int expandedPosition = -1;
    private final FragmentActivity mActivity;
    private List<String> StationInfo;
    private static List<Station> stations = null;
    OnItemClickListener mItemClickListener;
    View.OnClickListener mClickListener;
    private int page;    //pager page ; page에 맞게 수정할 예정

    //인규 - SERVER CONNECTION
    private ViewHolder congestionHolder = null;
    private static int ERROR = -1;
    private static int ACCIDENT_TRUE = 10;
    private static int ACCIDENT_FALSE = 11;
    private static int SERVER_CONNECTION_FAILED = 12;
    private static int INTERNET_DISCONNECTED = 13;

    public void setStationStatus( int status ) {

        String msg = "";
        if ( status == ACCIDENT_TRUE ) {
            msg = "사고가 났어요!";
        } else if ( status == ACCIDENT_FALSE ) {
            msg = "혼잡도가 들어갈 예정";
        } else if ( status == SERVER_CONNECTION_FAILED ) {
            msg = "서버와 통신이 어렵습니다!";
        } else if ( status == INTERNET_DISCONNECTED){
            msg = "인터넷 끊김";
        } else if ( status == ERROR ) {
            msg = "알 수 없는 에러";
        } else {
            msg = "STATUS NOT INITIALIZED";
        }
        congestionHolder.stationInfo.setText(msg);
    }


    public RecyclerViewAdapter(FragmentActivity mActivity, List<Station> stations1, int page) {
        this.mActivity = mActivity;
        stations = stations1;
        this.page = page;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.statioin_info_item, parent, false);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Calendar curTime = new GregorianCalendar();
        switch (position) {
            case 0:
                //이전역
                if (stations.get(page).getPrevStations().size() != 0) {
                    holder.preStation.setText("" + stations.get(page).getPrevStations().get(0).getStationName());
                    Map prevTime = getTime(stations.get(page),true,curTime);
                    Map prevTime2 = getTime(stations.get(page),true,(Calendar)prevTime.get("time"));
                    int gapTime1 = ((Calendar)prevTime.get("time")).get(Calendar.MINUTE)-curTime.get(Calendar.MINUTE);
                    if(gapTime1<0) gapTime1 +=60;
                    int gapTime2 = ((Calendar)prevTime2.get("time")).get(Calendar.MINUTE)-curTime.get(Calendar.MINUTE);
                    if(gapTime2<0) gapTime2 +=60;
                    holder.preTime1.setText(prevTime.get("terminalName")+"행 "+gapTime1+"분 후");
                    holder.preTime2.setText(prevTime2.get("terminalName")+"행 "+gapTime2+"분 후");
                } else {
                    holder.preStation.setText("");
                    holder.preTime1.setText("-");
                    holder.preTime2.setText("-");
                }

                // 현재역
                holder.curStation.setText("" + stations.get(page).getStationName());

                // 다음역
                if (stations.get(page).getNextStations().size() != 0) {
                    holder.nextStation.setText("" + stations.get(page).getNextStations().get(0).getStationName());
                    Map nextTime = getTime(stations.get(page),false,curTime);
                    Map nextTime2 = getTime(stations.get(page),false,(Calendar) nextTime.get("time"));

                    int nextGapTime1 = ((Calendar)nextTime.get("time")).get(Calendar.MINUTE)-curTime.get(Calendar.MINUTE);
                    if(nextGapTime1<0) nextGapTime1 +=60;
                    int nextGapTime2 = ((Calendar)nextTime2.get("time")).get(Calendar.MINUTE)-curTime.get(Calendar.MINUTE);
                    if(nextGapTime2<0) nextGapTime2 +=60;
                    holder.nextTime1.setText(nextTime.get("terminalName")+"행 "+nextGapTime1+"분 후");
                    holder.nextTime2.setText(nextTime2.get("terminalName")+"행 "+nextGapTime2+"분 후");
                } else {
                    holder.nextStation.setText("");
                    holder.nextTime1.setText("-");
                    holder.nextTime2.setText("-");
                }

                holder.curInfo.setVisibility(View.VISIBLE);
                holder.infoName.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                break;


            case 1:

                holder.infoName.setText("시간표");
                holder.goToTimetable.setVisibility(View.VISIBLE);

                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                break;

            case 2:

                holder.infoName.setText("역혼잡도");

                if (stations.get(page).getPrevStations().size() == 0 || stations.get(page).getNextStations().size() == 0) {
                    holder.stationInfo.setText("서버와 연결중!");
                } else {
                    holder.stationInfo.setText("서버와 연결중!");
                }

                if (!InternetManager.getInstance().checkNetwork()) holder.stationInfo.setText("인터넷 연결 끊김");

                holder.goToTimetable.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);

                //인규 - AsyncTask 용
                congestionHolder = holder;

                // getting Server AccidentInfo

                break;


            case 3:

                holder.infoName.setText("역 기본 정보");

                switch (stations.get(page).getPlatform()) {
                    case 1:
                        holder.Platform.setText("플랫폼 위치: 중앙");
                        break;
                    case 2:
                        holder.Platform.setText("플랫폼 위치: 양쪽");
                        break;
                    case 3:
                        holder.Platform.setText("플랫폼 위치: 복선(국철)");
                        break;
                    case 4:
                        holder.Platform.setText("플랫폼 위치: 일방향");
                        break;
                    default:
                        holder.Platform.setText("플랫폼 위치: 기타");
                        break;
                }
                switch (stations.get(page).getOffDoor()) {
                    case 1:
                        holder.OffDoor.setText("내리는 문: 오른쪽");
                        break;
                    case 2:
                        holder.OffDoor.setText("내리는 문: 양쪽");
                        break;
                    default:
                        holder.OffDoor.setText("내리는 문: 왼쪽");
                        break;
                }


                if (stations.get(page).getCrossOver() == 0) {
                    holder.CrossOver.setText("반대편 횡단: 불가능");

                } else {
                    holder.CrossOver.setText("반대편 횡단: 가능");

                }

                holder.stationDefaultInfo.setVisibility(View.VISIBLE);
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                break;

            case 4:

                holder.infoName.setText("역내 시설정보");

                switch (stations.get(page).getRestroom()) {
                    case 1:
                        holder.Restroom.setText("화장실: 안쪽");
                        break;
                    case 2:
                        holder.Restroom.setText("화장실: 바깥쪽");
                        break;
                    case 3:
                        holder.Restroom.setText("화장실: 환승역 연결");
                        break;
                    case 4:
                        holder.Restroom.setText("화장실: 안쪽, 바깥쪽");
                        break;
                    default:
                        holder.Restroom.setText("화장실: 없음");
                        break;
                }

                if (stations.get(page).getHandicapCount() == 0) {
                    holder.HandicapCount.setText("장애인 편의시설: 없음");
                } else {
                    holder.HandicapCount.setText("장애인 편의시설: 있음");
                }

                if (stations.get(page).getMeetingPlace() == 0) {
                    holder.MeetingPlace.setText("만남의 장소: 없음 ");
                } else {
                    holder.MeetingPlace.setText("만남의 장소: 있음");
                }
                if (stations.get(page).getCivilCount() == 0) {
                    holder.CivilCount.setText("민원 안내: 없음");
                } else {
                    holder.CivilCount.setText("민원 안내: 있음");
                }

                if (stations.get(page).getBicycleCount() == 0) {
                    holder.BicycleCount.setText("자전거 보관소: 없음");
                } else {
                    holder.BicycleCount.setText("자전거 보관소: 있음");
                }
                if (stations.get(page).getParkingCount() == 0) {
                    holder.ParkingCount.setText("환승 주차장: 없음");

                } else {
                    holder.ParkingCount.setText("환승 주차장: 있음");

                }

                if (stations.get(page).getPublicPlace() == 0) {
                    holder.PublicPlace.setText("현장사무소: 없음");
                } else {
                    holder.PublicPlace.setText("현장사무소: 있음");
                }
                holder.Tel.setText(stations.get(page).getTel());
                holder.Address.setText(stations.get(page).getAddress());

                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.VISIBLE);
                break;


            case 5:
                holder.infoName.setText("실시간 현재 역 정보");
                holder.stationInfo.setText("현재 서울역 공사로 인해 경의중앙선 이용불가");
                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                break;

        }


    }


    @Override
    public int getItemCount() {
        if (stations.get(page) != null) return 6;
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //현재 역 bottomsheet 처음 뜨는 부분 정보
        LinearLayout curInfo;
        //현재 역 상세정보
        TextView preStation, curStation, nextStation, infoName, stationInfo;
        TextView preTime1, preTime2, nextTime1, nextTime2;
        ImageView goToTimetable;
        GridLayout useInfo, stationDefaultInfo;
        TextView Platform, MeetingPlace, Restroom, OffDoor, CrossOver, HandicapCount, ParkingCount, BicycleCount, CivilCount, Tel, Address, PublicPlace;

        public ViewHolder(View view) {
            super(view);


            Log.d("station", "station" + stations.get(page).toString());
            curInfo = (LinearLayout) view.findViewById(R.id.cur_info);
            preStation = (TextView) view.findViewById(R.id.pre_station);
            curStation = (TextView) view.findViewById(R.id.cur_station);
            nextStation = (TextView) view.findViewById(R.id.next_station);
            infoName = (TextView) view.findViewById(R.id.info_name);
            stationInfo = (TextView) view.findViewById(R.id.info_stationinfo);

            preTime1 = (TextView) view.findViewById(R.id.pre_time1);
            preTime2 = (TextView) view.findViewById(R.id.pre_time2);
            nextTime1 = (TextView) view.findViewById(R.id.next_time1);
            nextTime2 = (TextView) view.findViewById(R.id.next_time2);


            goToTimetable = (ImageView) view.findViewById(R.id.timetable_next);

            stationDefaultInfo = (GridLayout) view.findViewById(R.id.station_default_info);
            Platform = (TextView) view.findViewById(R.id.platform);
            OffDoor = (TextView) view.findViewById(R.id.offdoor);
            CrossOver = (TextView) view.findViewById(R.id.crossover);

            useInfo = (GridLayout) view.findViewById(R.id.station_useinfo);
            Restroom = (TextView) view.findViewById(R.id.restroom);
            HandicapCount = (TextView) view.findViewById(R.id.handicap_count);
            MeetingPlace = (TextView) view.findViewById(R.id.meeting_place);
            CivilCount = (TextView) view.findViewById(R.id.civil_count);
            ParkingCount = (TextView) view.findViewById(R.id.parking_count);
            BicycleCount = (TextView) view.findViewById(R.id.bicycle_count);
            Tel = (TextView) view.findViewById(R.id.tel);
            PublicPlace = (TextView) view.findViewById(R.id.public_place);
            Address = (TextView) view.findViewById(R.id.address);


            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

public Map getTime(Station curStation,boolean isPrevWay, Calendar time){
    Calendar newCal = (Calendar)time.clone();
    Calendar compareCal = (Calendar)time.clone();
    Map<String, Object> returnTime = new HashMap<>();
    String terminalName = "";
    JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(mActivity ,curStation.getStationID());
    StationTimetable stt = jsonTimetableParser.getStationTimetable();

    ArrayList<HashMap<String, Object>>[] timeTable;
    String key;
    int day = newCal.get(Calendar.DAY_OF_WEEK);
    switch ( day ) {
        case 1 :
            if (isPrevWay) {
                timeTable = stt.getSunUpWayLdx();
                key = "sunUpWayLdx";
            }
            else {
                timeTable = stt.getSunDownWayLdx();
                key = "sunDownWayLdx";
            }
            break;
        case 7 :
            if (isPrevWay) {
                timeTable = stt.getSatUpWayLdx();
                key = "satUpWayLdx";
            }
            else{
                timeTable = stt.getSatDownWayLdx();
                key = "satDownWayLdx";
            }
            break;
        default:
            if (isPrevWay){
                timeTable = stt.getOrdUpWayLdx();
                key = "ordUpWayLdx";
            }
            else {
                timeTable = stt.getOrdDownWayLdx();
                key = "ordDownWayLdx";
            }
            break;
    }
    int hour = newCal.get(Calendar.HOUR_OF_DAY);
    int hourIndex = hour - 5 < 0 ? hour - 5 + 19 : hour - 5;
    ArrayList<HashMap<String, Object>> timeList = timeTable[hourIndex];

    int minute = newCal.get(Calendar.MINUTE);

//        Log.d(TAG, "getTimeTable: " + timeList.size());
    for ( int i = 0; i < timeList.size(); i ++ ) {
        HashMap<String, Object> timeMap = timeList.get(i);
        String timeString[] = ((String)timeMap.get( key )).split("\\(");
        int timeMinute = Integer.parseInt(timeString[0]);
        Log.d(TAG, "getTimeTable: " + timeMinute);
        terminalName = timeString[1].replace(")", "");
        Log.d(TAG, "getTimeTable: " + terminalName );
        Log.d(TAG, "checkTerminalName: " + curStation.getStationName());

            if ( timeMinute > minute ) {
                newCal.set(Calendar.MINUTE, timeMinute);
                // 전역변수 isExpress
                break;
            }
    }

    if (!newCal.equals(compareCal)){
        returnTime.put("terminalName", terminalName);
        returnTime.put("time",newCal);
        return returnTime;
    }else {
        newCal.set( Calendar.MINUTE, 60 );
        return getTime(curStation,isPrevWay, newCal);
    }
}

}