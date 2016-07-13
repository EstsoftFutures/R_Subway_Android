package com.estsoft.r_subway_android.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> implements RecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = "SearchListAdapter";

    private List<SemiStation> stationList = null;

    private RecyclerViewAdapter.OnItemClickListener mItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.search_list_image);
            mTextView = (TextView)view.findViewById(R.id.search_list_text);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG","CLICKEDLOGdfdf");
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
                Log.d("TAG","CLICKEDLOG");
            }
        }
    }

    public void SetOnItemClickListener(final RecyclerViewAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public SearchListAdapter(List<SemiStation> stationList) {
        this.stationList = stationList;




    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_item, parent, false );

        ViewHolder vh = new ViewHolder( view );
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


    @Override
    public void onItemClick(View view, int position) {

        Log.d(TAG, "onItemClick: " + position);
        
    }
}
