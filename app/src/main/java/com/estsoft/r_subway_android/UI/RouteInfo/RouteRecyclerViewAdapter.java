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
    private final List<Car> mUserDetails = new ArrayList<>();
    OnItemClickListener mItemClickListener;
    RouteNew route;

    public RouteRecyclerViewAdapter(FragmentActivity mActivity, RouteNew route) {
        this.mActivity = mActivity;
        this.route = route;
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
                holder.routeStartStation.setText("wowwow case 0");
                break;
            case 1:
                holder.routeStartStation.setText("wowwow case 1");
                break;
            case 2:
                if (position > 1 && route.getSections().size() > position - 2) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    params.setMargins(4, 4, 4, 4);


                    LinearLayout ll1 = (LinearLayout) holder.itemView.findViewById(R.id.mother01);
                    LayoutInflater sInflater1 = LayoutInflater.from(holder.itemView.getContext());
                    sInflater1.inflate(R.layout.layout_route_first, ll1, true);
                    for (int i = 0; i < route.getSections().size(); i++) {

                        LinearLayout llchild1 = new LinearLayout(mActivity);
                        llchild1.setOrientation(LinearLayout.VERTICAL);
                        RelativeLayout llchild2 = new RelativeLayout(mActivity);


                        ImageView routeLaneStart = new ImageView(mActivity);
                        routeLaneStart.setImageResource(R.drawable.lane1);

                        TextView routeStartStation = new TextView(mActivity);
                        routeStartStation.setTextColor(Color.BLACK);
                        routeStartStation.setText("" + route.getSections().get(i).get(0).getStationName());

                        TextView routeNumStations = new TextView(mActivity);
                        routeNumStations.setTextColor(Color.BLACK);
                        routeNumStations.setText("" + route.getSections().get(i).size() + "개 역");

                        Calendar startTime = route.getSections().get(i).get(0).getArriveTime();
                        TextView routeStartTime = new TextView(mActivity);
                        routeStartTime.setTextColor(Color.BLACK);
                        routeStartTime.setText(sdf.format(startTime.getTime()));


                        llchild2.addView(routeLaneStart);
                        llchild1.addView(routeStartStation);
                        llchild1.addView(routeNumStations);
                        llchild2.addView(llchild1);
                        llchild2.addView(routeStartTime);
                        RelativeLayout.LayoutParams paramsRouteStartTime = (RelativeLayout.LayoutParams) routeStartTime.getLayoutParams();
                        paramsRouteStartTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                        routeStartStation.setLayoutParams(params);
                        routeNumStations.setLayoutParams(params);
                        ll1.addView(llchild2);
                        for (int j = 1; j < route.getSections().get(i).size() - 1; j++) {
                            RelativeLayout llchild = new RelativeLayout(mActivity);


                            ImageView throughImg = new ImageView(mActivity);
                            throughImg.setImageResource(R.drawable.lane1);


                            TextView throughStationName = new TextView(mActivity);
                            throughStationName.setText("" + route.getSections().get(i).get(j).getStationName());
                            throughStationName.setTextColor(Color.BLACK);

                            TextView throughStationTime = new TextView(mActivity);
                            Calendar throughStationArrive = route.getSections().get(i).get(j).getArriveTime();
                            throughStationTime.setTextColor(Color.BLACK);
                            Log.d("RouteRecyclerview", "arrivetime" + sdf.format(throughStationArrive.getTime()));
                            throughStationTime.setText(sdf.format(throughStationArrive.getTime()));

                            llchild.addView(throughImg);
                            llchild.addView(throughStationName);
                            llchild.addView(throughStationTime);
                            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) throughStationTime.getLayoutParams();
                            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                            ll1.addView(llchild);
                            //holder.routeThroughStation.setText(holder.routeThroughStation.getText()+route.getSections().get(position).get(i).getStationName());

                        }
                        RelativeLayout llchild3 = new RelativeLayout(mActivity);

                        TextView routeArriveStationName = new TextView(mActivity);
                        routeArriveStationName.setText("" + route.getSections().get(i).get(route.getSections().get(i).size() - 1).getStationName());
                        routeArriveStationName.setTextColor(Color.BLACK);

                        Calendar arriveTime = route.getSections().get(i).get(route.getSections().get(i).size() - 1).getArriveTime();
                        TextView routeArriveTime = new TextView(mActivity);
                        routeArriveTime.setTextColor(Color.BLACK);
                        routeArriveTime.setText(sdf.format(arriveTime.getTime()));
                        routeArriveTime.setLayoutParams(params);
                        ImageView routeArriveImageView = new ImageView(mActivity);
                        routeArriveImageView.setImageResource(R.drawable.lane1);


                        llchild3.addView(routeArriveImageView);
                        llchild3.addView(routeArriveStationName);
                        llchild3.addView(routeArriveTime);
                        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) routeArriveTime.getLayoutParams();
                        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        ll1.addView(llchild3);

                    }
                }
                break;
            case 3:
                holder.routeStartStation.setText("wowwow case 3");
                break;

        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ll;
        ImageView routeLaneStart, routeShrinked, routeExpanded;
        TextView routeStartStation, routeNumStations, routeStartTime;

        ImageView routeThroughLane;
        TextView routeThroughStation, routeThroughTime;

        ImageView routeLaneArrive;
        TextView routeArriveStation, routeArriveTime;

        public ViewHolder(View view) {
            super(view);

            ll = (LinearLayout) view.findViewById(R.id.route_info_linear);
            routeLaneStart = (ImageView) view.findViewById(R.id.route_lane);
            routeShrinked = (ImageView) view.findViewById(R.id.route_shrinked_img);
            routeExpanded = (ImageView) view.findViewById(R.id.route_expanded_img);
            routeStartStation = (TextView) view.findViewById(R.id.route_start_station);
            routeNumStations = (TextView) view.findViewById(R.id.route_num_stations);
            routeStartTime = (TextView) view.findViewById(R.id.route_start_time);

            routeThroughLane = (ImageView) view.findViewById(R.id.route_through_lane);
            routeThroughStation = (TextView) view.findViewById(R.id.route_through_station);
            routeThroughTime = (TextView) view.findViewById(R.id.route_through_time);

            routeLaneArrive = (ImageView) view.findViewById(R.id.route_arrive_lane);
            routeArriveStation = (TextView) view.findViewById(R.id.route_arrive_station);
            routeArriveTime = (TextView) view.findViewById(R.id.route_arrive_time);


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

}