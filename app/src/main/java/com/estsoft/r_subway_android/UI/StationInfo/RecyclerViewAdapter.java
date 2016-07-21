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

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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


    public RecyclerViewAdapter(FragmentActivity mActivity, List<Station> stations1, int page) {
//        Log.d(TAG, "stationinadapterconstructor" + stations1.toString());
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

        switch (position) {
            case 0:

                //이전역
                if (stations.get(page).getPrevStations().size() != 0) {
                    holder.preStation.setText("" + stations.get(page).getPrevStations().get(0).getStationName());
                    holder.preTime1.setText("00행 00분");
                    holder.preTime2.setText("00행 00분");
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
                    holder.nextTime1.setText("00행 00분");
                    holder.nextTime2.setText("00행 00분");
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
                break;


            case 1:
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("시간표");
                holder.goToTimetable.setVisibility(View.VISIBLE);

                holder.stationInfo.setVisibility(View.GONE);
                break;

            case 2:
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("역혼잡도");

                if (stations.get(page).getPrevStations().size() == 0 || stations.get(page).getNextStations().size() == 0) {

                    holder.stationInfo.setText("-");
                } else {
                    holder.stationInfo.setText("역혼잡도 정보");
                }
                holder.goToTimetable.setVisibility(View.GONE);
                break;


            case 3:
                holder.useInfo.setVisibility(View.GONE);
                if (stations.get(page).getExStations() != null) {
                    holder.curInfo.setVisibility(View.GONE);
                    holder.goToTimetable.setVisibility(View.GONE);
                    holder.infoName.setText("환승구역");
                    switch (stations.get(page).getOffDoor()) {
                        case 0:
                            holder.stationInfo.setText("출입문 왼쪽");
                            break;
                        case 1:
                            holder.stationInfo.setText("출입문 오른쪽");
                            break;
                        case 2:
                            holder.stationInfo.setText("출입문 양쪽");
                            break;
                    }
                }

                break;

            case 4:
                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);

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

                if (stations.get(page).getBicycleCount() == 0) {
                    holder.BicycleCount.setText("자전거 보관소: 없음");
                } else {
                    holder.BicycleCount.setText("자전거 보관소: 있음");
                }
                if (stations.get(page).getHandicapCount() == 0) {
                    holder.HandicapCount.setText("장애인 편의시설: 없음");
                } else {
                    holder.HandicapCount.setText("장애인 편의시설: 있음");
                }
                if (stations.get(page).getParkingCount() == 0) {
                    holder.ParkingCount.setText("환승 주차장: 없음");

                } else {
                    holder.ParkingCount.setText("환승 주차장: 있음");

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
                if (stations.get(page).getCrossOver() == 0) {
                    holder.CrossOver.setText("반대편 횡단: 불가능");

                } else {
                    holder.CrossOver.setText("반대편 횡단: 가능");

                }

                holder.Tel.setText(stations.get(page).getTel());
                holder.Address.setText(stations.get(page).getAddress());

                holder.useInfo.setVisibility(View.VISIBLE);
                break;

            case 5:

                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.infoName.setText("급행열차");
                holder.stationInfo.setText("급행열차정보");
                break;

            case 6:
                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.infoName.setText("현재 역 정보");
                holder.stationInfo.setText("역 내 정보 알림 필요시 남길 것 _ CRAWLING");
                break;

        }


    }


    @Override
    public int getItemCount() {
        if (stations.get(page) != null) return 7;
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //현재 역 bottomsheet 처음 뜨는 부분 정보
        LinearLayout curInfo;
        //현재 역 상세정보
        TextView preStation, curStation, nextStation, infoName, stationInfo;
        TextView preTime1, preTime2, nextTime1, nextTime2;
        ImageView goToTimetable;
        GridLayout useInfo;
        TextView Platform, MeetingPlace, Restroom, OffDoor, CrossOver, HandicapCount, ParkingCount, BicycleCount, CivilCount, Tel, Address;

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

            useInfo = (GridLayout) view.findViewById(R.id.station_useinfo);
            Platform = (TextView) view.findViewById(R.id.platform);
            MeetingPlace = (TextView) view.findViewById(R.id.meeting_place);
            Restroom = (TextView) view.findViewById(R.id.restroom);
            OffDoor = (TextView) view.findViewById(R.id.offdoor);
            CrossOver = (TextView) view.findViewById(R.id.crossover);
            HandicapCount = (TextView) view.findViewById(R.id.handicap_count);
            ParkingCount = (TextView) view.findViewById(R.id.parking_count);
            BicycleCount = (TextView) view.findViewById(R.id.bicycle_count);
            CivilCount = (TextView) view.findViewById(R.id.civil_count);
            Tel = (TextView) view.findViewById(R.id.tel);
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


}