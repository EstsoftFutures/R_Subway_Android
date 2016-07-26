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
                holder.routeStartStation.setText("소요시간");
                holder.routeStationTo.setText(route.getSections().get(0).get(0).getStationName()+"~"+route.getSections().get(route.getSections().size()-1).get(route.getSections().get(route.getSections().size()-1).size()-1).getStationName());
                holder.routeNumStations.setText("1시간21분");
                holder.routeStartTime.setText("환승"+route.getSections().size()+"회");
                holder.routeFirstMom.setVisibility(View.VISIBLE);
                break;
            case 1:

                holder.routeStartStation.setText("아마도 여긴 시간설정?");
                holder.routeStationTo.setVisibility(View.GONE);
                holder.routeNumStations.setVisibility(View.GONE);
                holder.routeStartTime.setVisibility(View.GONE);
                break;

            case 2:

                if (position > 1 && route.getSections().size() > position - 2) {

                    LinearLayout ll1 = (LinearLayout) holder.itemView.findViewById(R.id.mother01);

                    for (int i = 0; i < route.getSections().size(); i++) {
                        LinearLayout.LayoutParams llpImgTime = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llpImgTime.topMargin = 50;
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp.leftMargin = 70;
                        llp.bottomMargin =15;

                        LinearLayout llchild1 = new LinearLayout(mActivity);
                        llchild1.setOrientation(LinearLayout.VERTICAL);

                        RelativeLayout llchild2 = new RelativeLayout(mActivity);

                        ImageView routeLaneStart = new ImageView(mActivity);
                        routeLaneStart.setImageResource(R.drawable.lane1);
                        routeLaneStart.setLayoutParams(llpImgTime);

                        TextView routeStartStation = new TextView(mActivity);
                        routeStartStation.setTextColor(Color.BLACK);
                        routeStartStation.setText("" + route.getSections().get(i).get(0).getStationName());
                        routeStartStation.setTextSize(20);


                        routeStartStation.setLayoutParams(llp);

                        TextView routeNumStations = new TextView(mActivity);
                        routeNumStations.setTextColor(Color.BLACK);
                        routeNumStations.setText("" + route.getSections().get(i).size() + "개 역");
                        routeNumStations.setTextSize(16);
                        routeNumStations.setLayoutParams(llp);


                        Calendar startTime = route.getSections().get(i).get(0).getArriveTime();
                        TextView routeStartTime = new TextView(mActivity);
                        routeStartTime.setTextColor(Color.BLACK);
                        routeStartTime.setTextSize(18);
                        routeStartTime.setText(sdf.format(startTime.getTime()));
                        routeStartTime.setLayoutParams(llpImgTime);

                        llchild1.addView(routeStartStation);
                        llchild1.addView(routeNumStations);

                        llchild2.addView(routeLaneStart);
                        RelativeLayout.LayoutParams paramRouteLaneStart = (RelativeLayout.LayoutParams) routeLaneStart.getLayoutParams();
                        paramRouteLaneStart.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        llchild2.addView(llchild1);
                        llchild2.addView(routeStartTime);

                        RelativeLayout.LayoutParams paramsRouteStartTime = (RelativeLayout.LayoutParams) routeStartTime.getLayoutParams();
                        paramsRouteStartTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                        ll1.addView(llchild2);
                        for (int j = 1; j < route.getSections().get(i).size() - 1; j++) {
                            RelativeLayout.LayoutParams llp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            llp2.leftMargin = 80;

                            RelativeLayout llchild = new RelativeLayout(mActivity);

                            llp2.topMargin = 3;
                            ImageView throughImg = new ImageView(mActivity);
                            throughImg.setImageResource(R.drawable.lane1);


                            TextView throughStationName = new TextView(mActivity);
                            throughStationName.setText("" + route.getSections().get(i).get(j).getStationName());
                            throughStationName.setTextColor(Color.BLACK);
                            throughStationName.setTextSize(18);
                            throughStationName.setLayoutParams(llp2);

                            TextView throughStationTime = new TextView(mActivity);
                            Calendar throughStationArrive = route.getSections().get(i).get(j).getArriveTime();
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
                        llp3.setMargins(70,10,15,15); // llp.setMargins(left, top, right, bottom);


                        RelativeLayout llchild3 = new RelativeLayout(mActivity);

                        TextView routeArriveStationName = new TextView(mActivity);
                        routeArriveStationName.setText("" + route.getSections().get(i).get(route.getSections().get(i).size() - 1).getStationName());
                        routeArriveStationName.setTextColor(Color.BLACK);
                        routeArriveStationName.setTextSize(20);

                        routeArriveStationName.setLayoutParams(llp3);
                        Calendar arriveTime = route.getSections().get(i).get(route.getSections().get(i).size() - 1).getArriveTime();
                        TextView routeArriveTime = new TextView(mActivity);
                        routeArriveTime.setTextColor(Color.BLACK);
                        routeArriveTime.setTextSize(18);
                        routeArriveTime.setText(sdf.format(arriveTime.getTime()));


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
                holder.routeStartStation.setVisibility(View.GONE);
                holder.routeStationTo.setVisibility(View.GONE);
                holder.routeNumStations.setVisibility(View.GONE);
                holder.routeStartTime.setVisibility(View.GONE);
                holder.routeFirstMom.setVisibility(View.GONE);
                break;
            case 3:
                holder.routeStartStation.setText("검색 근거");
                holder.routeStationTo.setVisibility(View.GONE);
                holder.routeNumStations.setVisibility(View.GONE);
                holder.routeStartTime.setVisibility(View.GONE);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return 4;
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

}