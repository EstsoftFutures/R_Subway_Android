package com.estsoft.r_subway_android.UI.StationInfo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import java.util.List;

/**
 * Created by Administrator on 2016-07-18.
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private static final String TAG = "TimeTableAdapter";

    private Context context;
    private StationTimetable stationTimetable;
    private int ordSatSun;
    OnItemClickListener mItemClickListener;
    View.OnClickListener mClickListener;

    // 0 : ord, 1 : sat, 2 : sun
    public TimetableAdapter(Context context, StationTimetable stationTimetable, int ordSatSun) {
        this.context = context;
        this.stationTimetable = stationTimetable;
        this.ordSatSun = ordSatSun;
    }

    @Override
    public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.station_info_time_table, parent, false);

        return new TimetableViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(TimetableViewHolder holder, int position) {
//        holder.upWay.setText(stationTimetable.getUpWay());
//        holder.downWay.setText(stationTimetable.getDownWay());
        switch (this.ordSatSun) {

            case 0:
                int upSize = stationTimetable.getOrdUpWayLdx()[position].size();
                int downSize = stationTimetable.getOrdDownWayLdx()[position].size();
                try {

                    holder.time1.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(0));
                    holder.time2.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(1));
                    holder.time3.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(2));
                    holder.time4.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(3));
                    holder.time5.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(4));
                    holder.time6.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(5));
                    holder.time7.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(6));
                    holder.time8.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(7));
                    holder.time9.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(8));
                    holder.time10.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(9));
                    holder.time11.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(10));
                    holder.time12.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(11));
                    holder.time13.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(12));
                    holder.time14.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(13));
                } catch (NullPointerException ex) {

                } finally {
                    break;

                }
            case 1:
                try {
                    holder.time1.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(0));
                    holder.time2.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(1));
                    holder.time3.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(2));
                    holder.time4.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(3));
                    holder.time5.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(4));
                    holder.time6.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(5));
                    holder.time7.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(6));
                    holder.time8.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(7));
                    holder.time9.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(8));
                    holder.time10.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(9));
                    holder.time11.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(10));
                    holder.time12.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(11));
                    holder.time13.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(12));
                } catch (NullPointerException ex) {

                } finally {
                    break;

                }
            case 2:
                holder.time1.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatUpWayLdx()[position].get(0));
                holder.time2.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getSatDownWayLdx()[position].get(0));
                break;

            default:
                holder.time1.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdUpWayLdx()[position].get(0));
                holder.time2.setText("" + stationTimetable.getStationHour().get(position) + ":" + stationTimetable.getOrdDownWayLdx()[position].get(0));
                break;
        }

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    public class TimetableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time1, time2, time3, time4, time5, time6, time7, time8, time9, time10, time11, time12, time13, time14;
        TextView upWay, downWay;

        public TimetableViewHolder(View view) {
            super(view);

            time1 = (TextView) view.findViewById(R.id.time1);
            time2 = (TextView) view.findViewById(R.id.time2);
            time3 = (TextView) view.findViewById(R.id.time3);
            time4 = (TextView) view.findViewById(R.id.time4);
            time5 = (TextView) view.findViewById(R.id.time5);
            time6 = (TextView) view.findViewById(R.id.time6);
            time7 = (TextView) view.findViewById(R.id.time7);
            time8 = (TextView) view.findViewById(R.id.time8);
            time9 = (TextView) view.findViewById(R.id.time9);
            time10 = (TextView) view.findViewById(R.id.time10);
            time11 = (TextView) view.findViewById(R.id.time11);
            time12 = (TextView) view.findViewById(R.id.time12);
            time13 = (TextView) view.findViewById(R.id.time13);
            time14 = (TextView) view.findViewById(R.id.time14);

            upWay = (TextView) view.findViewById(R.id.upway);
            downWay = (TextView) view.findViewById(R.id.downway);

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