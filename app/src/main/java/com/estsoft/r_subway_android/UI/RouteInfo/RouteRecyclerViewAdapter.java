package com.estsoft.r_subway_android.UI.RouteInfo;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("h:mm");

    private final FragmentActivity mActivity;
    OnItemClickListener mItemClickListener;
    RouteNew[] route;
    private int mPage;

    public RouteRecyclerViewAdapter(FragmentActivity mActivity, RouteNew[] route, int mPage) {
        this.mActivity = mActivity;
        this.route = route;
        this.mPage = mPage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.route_info_item, parent, false);

        LinearLayout ll1 = (LinearLayout) sView.findViewById(R.id.mother01);
        LayoutInflater sInflater1 = LayoutInflater.from(sView.getContext());
        sInflater1.inflate(R.layout.layout_route_first, ll1, true);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (position) {
            case 0:
                holder.routeStartStation.setText("소요시간");
                Station start = route[mPage].getSections().get(0).get(0);
                Station end = route[mPage].getSections().get(route[mPage].getSections().size() - 1).get(route[mPage].getSections().get(route[mPage].getSections().size() - 1).size() - 1);
                holder.routeStationTo.setText(start.getStationName() + "~" + end.getStationName());
                Log.d("TEST", "onBindViewHolder: " + start.getStationName());

                holder.routeNumStations.setText(convertCalendar(start.getArriveTime(), end.getArriveTime()));
                holder.routeStartTime.setText("환승" + (route[mPage].getSections().size() - 1) + "회");


                holder.routeStartStation.setVisibility(View.VISIBLE);
                holder.routeNumStations.setVisibility(View.VISIBLE);
                holder.routeStationTo.setVisibility(View.VISIBLE);
                holder.routeStartTime.setVisibility(View.VISIBLE);
                holder.routeFirstMom.setVisibility(View.VISIBLE);
                break;

            case 1:

                if (position > 0 && route[mPage].getSections().size() > position - 1) {

                    LinearLayout ll1 = (LinearLayout) holder.itemView.findViewById(R.id.mother01);

                    for (int i = 0; i < route[mPage].getSections().size(); i++) {

                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp.topMargin = 10;
                        llp.leftMargin = 140;
                        llp.bottomMargin = 10;


                        LinearLayout.LayoutParams llp6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp6.leftMargin = 150;
                        llp6.bottomMargin = 10;

                        LinearLayout llchild1 = new LinearLayout(mActivity);
                        llchild1.setOrientation(LinearLayout.VERTICAL);

                        RelativeLayout llchild2 = new RelativeLayout(mActivity);

                        ImageView routeLaneStart = new ImageView(mActivity);
                        routeLaneStart.setImageResource(getStartEndLane(route[mPage].getSections().get(i).get(0).getLaneType()));

                        TextView routeStartStation = new TextView(mActivity);
                        routeStartStation.setTextColor(Color.BLACK);
                        routeStartStation.setText("" + route[mPage].getSections().get(i).get(0).getStationName());
                        routeStartStation.setTextSize(20);


                        routeStartStation.setLayoutParams(llp);

                        TextView routeNumStations = new TextView(mActivity);
                        routeNumStations.setTextColor(Color.BLACK);
                        if (route[mPage].getExpressSectionIndex().get(i)) {
                            routeNumStations.setText("" + route[mPage].getSections().get(i).size() + "개 역(급행)");
                        } else {
                            routeNumStations.setText("" + route[mPage].getSections().get(i).size() + "개 역");
                        }
                        routeNumStations.setTextSize(16);
                        routeNumStations.setLayoutParams(llp6);


                        Calendar startTime = route[mPage].getSections().get(i).get(0).getArriveTime();
                        TextView routeStartTime = new TextView(mActivity);
                        routeStartTime.setTextColor(Color.BLACK);
                        routeStartTime.setTextSize(18);
                        routeStartTime.setText(sdf.format(startTime.getTime()));

                        llchild1.addView(routeStartStation);
                        llchild1.addView(routeNumStations);

                        llchild2.addView(routeLaneStart);
                        RelativeLayout.LayoutParams paramRouteLaneStart = (RelativeLayout.LayoutParams) routeLaneStart.getLayoutParams();
                        paramRouteLaneStart.setMargins(0, 20, 20, 0);
                        paramRouteLaneStart.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        llchild2.addView(llchild1);
                        llchild2.addView(routeStartTime);

                        RelativeLayout.LayoutParams paramsRouteStartTime = (RelativeLayout.LayoutParams) routeStartTime.getLayoutParams();
                        paramsRouteStartTime.setMargins(0, 20, 0, 0);
                        paramsRouteStartTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                        ll1.addView(llchild2);
                        for (int j = 1; j < route[mPage].getSections().get(i).size() - 1; j++) {
                            RelativeLayout.LayoutParams llp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            llp2.leftMargin = 150;


                            RelativeLayout llchild = new RelativeLayout(mActivity);
                            ImageView throughImg = new ImageView(mActivity);
                            throughImg.setImageResource(getRouteLane(route[mPage].getSections().get(i).get(j).getLaneType()));


                            TextView throughStationName = new TextView(mActivity);
                            throughStationName.setText("" + route[mPage].getSections().get(i).get(j).getStationName());
                            throughStationName.setTextColor(Color.BLACK);
                            throughStationName.setTextSize(18);
                            throughStationName.setLayoutParams(llp2);

                            TextView throughStationTime = new TextView(mActivity);
                            Calendar throughStationArrive = route[mPage].getSections().get(i).get(j).getArriveTime();
                            throughStationTime.setTextColor(Color.BLACK);
                            Log.d("RouteRecyclerview", "arrivetime" + sdf.format(throughStationArrive.getTime()));
                            throughStationTime.setText(sdf.format(throughStationArrive.getTime()));
                            throughStationTime.setTextSize(18);

                            llchild.addView(throughImg);
                            llchild.addView(throughStationName);
                            llchild.addView(throughStationTime);

                            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) throughStationTime.getLayoutParams();
                            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                            ll1.addView(llchild);

                        }
                        RelativeLayout.LayoutParams llp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        llp3.setMargins(140, 0, 15, 15); // llp.setMargins(left, top, right, bottom);

//                        RelativeLayout.LayoutParams llpImg3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        llpImg3.topMargin = 20; // llp.setMargins(left, top, right, bottom);

                        RelativeLayout llchild3 = new RelativeLayout(mActivity);

                        TextView routeArriveStationName = new TextView(mActivity);
                        routeArriveStationName.setText("" + route[mPage].getSections().get(i).get(route[mPage].getSections().get(i).size() - 1).getStationName());
                        routeArriveStationName.setTextColor(Color.BLACK);
                        routeArriveStationName.setTextSize(20);

                        routeArriveStationName.setLayoutParams(llp3);
                        Calendar arriveTime = route[mPage].getSections().get(i).get(route[mPage].getSections().get(i).size() - 1).getArriveTime();
                        TextView routeArriveTime = new TextView(mActivity);
                        routeArriveTime.setTextColor(Color.BLACK);
                        routeArriveTime.setTextSize(18);
                        routeArriveTime.setText(sdf.format(arriveTime.getTime()));


                        ImageView routeArriveImageView = new ImageView(mActivity);
                        routeArriveImageView.setImageResource(getStartEndLane(route[mPage].getSections().get(i).get(route[mPage].getSections().get(i).size() - 1).getLaneType()));
                        //                       routeArriveImageView.setLayoutParams(llpImg3);

                        llchild3.addView(routeArriveImageView);
                        llchild3.addView(routeArriveStationName);
                        llchild3.addView(routeArriveTime);
                        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) routeArriveTime.getLayoutParams();
                        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        ll1.addView(llchild3);

                    }
                }
                holder.routeStartStation.setVisibility(View.GONE);
                holder.routeStationTo.setVisibility(View.GONE);
                holder.routeNumStations.setVisibility(View.GONE);
                holder.routeStartTime.setVisibility(View.GONE);
                holder.routeFirstMom.setVisibility(View.GONE);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ll, routeFirstMom;
        TextView routeStartStation, routeNumStations, routeStartTime;
        TextView routeStationTo;


        public ViewHolder(View view) {
            super(view);

            ll = (LinearLayout) view.findViewById(R.id.route_info_linear);
            routeStartStation = (TextView) view.findViewById(R.id.route_start_station);
            routeNumStations = (TextView) view.findViewById(R.id.route_num_stations);
            routeStartTime = (TextView) view.findViewById(R.id.route_start_time);
            routeStationTo = (TextView) view.findViewById(R.id.route_station_to);
            routeFirstMom = (LinearLayout) view.findViewById(R.id.route_first_mom);

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
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    private String convertCalendar(Calendar start, Calendar end) {

        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        long parsedMinute = diff / 1000 / 60;
        int hour = (int) parsedMinute / 60;
        int minute = (int) parsedMinute - hour * 60;

        if (hour == 0) return minute + "분";
        return hour + "시간 " + minute + "분";
    }

    private int getStartEndLane(int laneType) {
        switch (laneType) {
            case 1:
                return R.drawable.route_lane1;
            case 2:
                return R.drawable.route_lane2;
            case 3:
                return R.drawable.route_lane3;
            case 4:
                return R.drawable.route_lane4;
            case 5:
                return R.drawable.route_lane5;
            case 6:
                return R.drawable.route_lane6;
            case 7:
                return R.drawable.route_lane7;
            case 8:
                return R.drawable.route_lane8;
            case 9:
                return R.drawable.route_lane9;
            case 102:
                return R.drawable.lane102;
            case 109:
                return R.drawable.lane109;
            case 100:
                return R.drawable.lane100;
            case 111:
                return R.drawable.lane111;
            case 21:
                return R.drawable.lane21;
            case 108:
                return R.drawable.lane108;
            case 104:
                return R.drawable.lane104;
            case 101:
                return R.drawable.lane101;
            case 110:
                return R.drawable.lane110;
            case 107:
                return R.drawable.lane107;
            default:
                return R.drawable.route_lane1;
        }
    }

    private int getRouteLane(int laneType) {
        switch (laneType) {
            case 1:
                return R.drawable.lane_1_mid;
            case 2:
                return R.drawable.lane_2_mid;
            case 3:
                return R.drawable.lane_3_mid;
            case 4:
                return R.drawable.lane_4_mid;
            case 5:
                return R.drawable.lane_5_mid;
            case 6:
                return R.drawable.lane_6_mid;
            case 7:
                return R.drawable.lane_7_mid;
            case 8:
                return R.drawable.lane_8_mid;
            case 9:
                return R.drawable.lane_9_mid;
            case 102:
                return R.drawable.lane_102_mid;
            case 109:
                return R.drawable.lane_109_mid;
            case 100:
                return R.drawable.lane_100_mid;
            case 111:
                return R.drawable.lane_111_mid;
            case 21:
                return R.drawable.lane_21_mid;
            case 108:
                return R.drawable.lane_108_mid;
            case 104:
                return R.drawable.lane_104_mid;
            case 101:
                return R.drawable.lane_101_mid;
            case 110:
                return R.drawable.lane_110_mid;
            case 107:
                return R.drawable.lane_107_mid;
            default:
                return R.drawable.lane_1_mid;
        }
    }

}