package com.estsoft.r_subway_android.UI.StationInfo;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

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
    private static Station station = null;
    OnItemClickListener mItemClickListener;
    View.OnClickListener mClickListener;


    public RecyclerViewAdapter(FragmentActivity mActivity, Station station) {
        Log.d(TAG,"stationinadapterconstructor"+station.toString());
        this.mActivity = mActivity;
        this.station = station;
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
                // 이전역
                holder.preStation.setText("" + station.getPrevStations().get(0).getStationName());

                // 현재역
                holder.curStation.setText("" + station.getStationName());
                // 다음역
                holder.nextStation.setText("" + station.getNextStations().get(0).getStationName());
                holder.curInfo.setVisibility(View.VISIBLE);
                holder.infoName.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                break;


            case 1:
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("시간표");
                holder.stationInfo.setText("시간표정보");

                holder.stationInfo.setVisibility(View.GONE);
                break;

            case 2:
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("역혼잡도");
                holder.stationInfo.setText("역혼잡도 정보");
                break;


            case 3:
                if (station.getExStations() != null) {
                    holder.curInfo.setVisibility(View.GONE);
                    holder.infoName.setText("환승구역");
                    switch (station.getOffDoor()) {
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
                holder.infoName.setText("역내 시설정보");
                holder.stationInfo.setText("" + station.getTel() + station.getBicycleCount() + station.getHandicapCount() + station.getParkingCount() + station.getRestroom() + station.getPlatform() + station.getMeetingPlace() + station.getOffDoor() + station.getCivilCount());
                break;

            case 5:
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("급행열차");
                holder.stationInfo.setText("급행열차정보");
                break;

            case 6:
                holder.curInfo.setVisibility(View.GONE);
                holder.infoName.setText("현재 역 정보");
                holder.stationInfo.setText("역 내 정보 알림 필요시 남길 것 _ CRAWLING");
                break;

        }


    }


    @Override
    public int getItemCount() {
        if (station != null) return 7;
        return 0;
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //현재 역 bottomsheet 처음 뜨는 부분 정보
    LinearLayout curInfo;
    //현재 역 상세정보
    TextView preStation, curStation, nextStation, infoName, stationInfo;

    public ViewHolder(View view) {
        super(view);


        Log.d("station","station"+station.toString());
        curInfo = (LinearLayout) view.findViewById(R.id.cur_info);
        preStation = (TextView) view.findViewById(R.id.pre_station);
        curStation = (TextView) view.findViewById(R.id.cur_station);
        nextStation = (TextView) view.findViewById(R.id.next_station);
        infoName = (TextView) view.findViewById(R.id.info_name);
        stationInfo = (TextView) view.findViewById(R.id.info_stationinfo);
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