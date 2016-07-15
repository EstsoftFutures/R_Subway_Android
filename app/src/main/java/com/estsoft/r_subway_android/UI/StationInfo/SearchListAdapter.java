package com.estsoft.r_subway_android.UI.StationInfo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.listener.SearchListAdapterListener;

import java.util.List;

/**
 * Created by estsoft on 2016-07-13.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private static final String TAG = "SearchListAdapter";

    private List<SemiStation> stationList = null;

    private SearchListAdapterListener mSearchListAdapterListener;

    private OnItemClickListener mItemClickListener;

    private RelativeLayout mTransferRelativeLayout;

    private LayoutInflater inflater;

    private ImageView transferStandard = null;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;
//        public List<Integer> laneNumbers;
        public SemiStation station;

        public ViewHolder(View view) {
            super(view);

            transferStandard = (ImageView)view.findViewById(R.id.transfer_standard);

            mImageView = (ImageView) view.findViewById(R.id.search_list_image);
            mTextView = (TextView) view.findViewById(R.id.search_list_text);

            mTransferRelativeLayout = (RelativeLayout)view.findViewById(R.id.search_transfer_Layout);

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
                    Log.d(TAG, "make new itemclick" + position);
                    mSearchListAdapterListener.itemClick( stationList.get(position) );

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

        // 수정
        if (true) {
            holder.mImageView.setImageResource(R.drawable.checked_image);
        } else {
            holder.mImageView.setImageResource(R.drawable.danger_image);
        }

        holder.station = stationList.get(position);
//        holder.laneNumbers = matchStation.getLaneNumbers();

        int transferX = 0;

        for (int i = 0; i < holder.station.getLaneNumbers().size(); i ++ ) {
//            for (int i = 0; i < 2; i ++ ) {
            ImageView transfer = (ImageView)inflater.inflate( R.layout.search_list_item_transfer, null );
            transfer.setImageResource( getResouceIdByLaneNumber( holder.station.getLaneNumbers().get(i) ) );
//            transfer.setImageResource(  );
//                RelativeLayout.LayoutParams rParam = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

            if ( mTransferRelativeLayout != null ) {
                mTransferRelativeLayout.addView(transfer);
                transfer.setLayoutParams(transferStandard.getLayoutParams());
                transfer.setVisibility(View.VISIBLE);

                transfer.setX(transferX);
                transferX -= transferStandard.getDrawable().getIntrinsicWidth() + transferStandard.getDrawable().getIntrinsicWidth() / 4 ;
            }

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


    public int getResouceIdByLaneNumber( int laneNumber ) {
        switch ( laneNumber ) {
            case 1 :
                return R.drawable.lane1;
            case 2 :
                return R.drawable.lane2;
            case 3 :
                return R.drawable.lane3;
            case 4 :
                return R.drawable.lane4;
            case 5 :
                return R.drawable.lane5;
            case 6 :
                return R.drawable.lane6;
            case 7 :
                return R.drawable.lane7;
            case 8 :
                return R.drawable.lane8;
            case 9 :
                return R.drawable.lane9;
            case 21 :
                return R.drawable.lane21;
            case 100 :
                return R.drawable.lane100;
            case 101 :
                return R.drawable.lane101;
            case 104 :
                return R.drawable.lane104;
            case 107 :
                return R.drawable.lane107;
            case 108 :
                return R.drawable.lane108;
            case 109 :
                return R.drawable.lane109;
            case 110 :
                return R.drawable.lane110;
            case 111 :
                return R.drawable.lane111;
            default:
                return R.drawable.recycle_image;

        }
    }

    public SearchListAdapterListener getmSearchListAdapterListener() {
        return mSearchListAdapterListener;
    }

    public void setmSearchListAdapterListener(SearchListAdapterListener mSearchListAdapterListener) {
        this.mSearchListAdapterListener = mSearchListAdapterListener;
    }
}