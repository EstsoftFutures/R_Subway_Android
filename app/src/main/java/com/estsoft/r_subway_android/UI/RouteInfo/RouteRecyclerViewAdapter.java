package com.estsoft.r_subway_android.UI.RouteInfo;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {

    private final FragmentActivity mActivity;
    private final List<Car> mUserDetails = new ArrayList<>();
    OnItemClickListener mItemClickListener;
    public RouteRecyclerViewAdapter(FragmentActivity mActivity) {
        this.mActivity = mActivity;
        createUserDetails();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.statioin_info_item, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        holder.vId.setText("ID: " + mUserDetails.get(position).getId());
        holder.vName.setText("Name: " + mUserDetails.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mUserDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vName, vSex, vId, vAge;

        public ViewHolder(View view) {
            super(view);
            vId = (TextView) view.findViewById(R.id.list_id);
            vName = (TextView) view.findViewById(R.id.list_name);
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

    /* ==========This Part is not necessary========= */

    /**
     * Create Random Users
     */
    private void createUserDetails() {

        for (int i = 0; i < 5; i++) {
            Car mDetails = new Car(i,"name"+i);
            mUserDetails.add(mDetails);
        }
    }


    /* ==========This Part is not necessary========= */
}