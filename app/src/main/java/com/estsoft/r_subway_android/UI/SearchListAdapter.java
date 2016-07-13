package com.estsoft.r_subway_android.UI;

import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.UI.StationInfo.Car;
import com.estsoft.r_subway_android.UI.StationInfo.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-07-13.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private static final String TAG = "SearchListAdapter";

    private List<SemiStation> stationList = null;

    private OnItemClickListener mItemClickListener;

    private RelativeLayout mTransferRelativeLayout;

    private LayoutInflater inflater;

    private ImageView transferStandard = null;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);

            transferStandard = (ImageView)view.findViewById(R.id.transfer_standard);

            mImageView = (ImageView) view.findViewById(R.id.search_list_image);
            mTextView = (TextView) view.findViewById(R.id.search_list_text);

            mTransferRelativeLayout = (RelativeLayout)view.findViewById(R.id.search_transfer_Layout);

            int transferX = 0;

            for (int i = 0; i < 4; i ++ ) {
                ImageView transfer = (ImageView)inflater.inflate( R.layout.search_list_item_transfer, null );
//                RelativeLayout.LayoutParams rParam = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

                if ( mTransferRelativeLayout != null ) {
                    mTransferRelativeLayout.addView(transfer);
                    transfer.setLayoutParams(transferStandard.getLayoutParams());
                    transfer.setVisibility(View.VISIBLE);

                    transfer.setX(transferX);
                    transferX -= transferStandard.getDrawable().getIntrinsicWidth() + transferStandard.getDrawable().getIntrinsicWidth() / 4;


                }

            }
            Log.d(TAG, "ViewHolder: -------------------");

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public SearchListAdapter(List<SemiStation> stationList, LayoutInflater inflater) {
        this.stationList = stationList;
        this.inflater = inflater;

    }


    public OnItemClickListener getmItemClickListener() {
        if (mItemClickListener == null) {
            mItemClickListener = new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.d(TAG, "make new itemclick"+position);

                }
            };
        }
        return mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(stationList.get(position).getName());
        if (true) {
            holder.mImageView.setImageResource(R.drawable.checked_image);
        } else {
            holder.mImageView.setImageResource(R.drawable.danger_image);
        }
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    void onItemClick2(View view, int position){


    }

}