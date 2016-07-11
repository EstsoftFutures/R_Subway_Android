package com.estsoft.r_subway_android.UI.StationInfo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;


public class StationInfoFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    FragmentActivity mActivity;
    RecyclerView mRecyclerView;
    RecyclerViewAdapter adapter;


    public StationInfoFragment() {
        // Required empty public constructor
    }


    public static StationInfoFragment newInstance(int page) {
        StationInfoFragment fragment = new StationInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        setRetainInstance(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_station_info, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

//////

        adapter = new RecyclerViewAdapter(mActivity);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
Log.d("clicked","position"+position);
        TextView test1 = (TextView) v.findViewById(R.id.test_expandable);
                // do something with position
                if (position == 2) {
                    Log.d("child=2","child2");
                    if(test1.getVisibility() == View.VISIBLE){
                        Log.d("child=2","child2&willbegone");

                        test1.setVisibility(View.GONE);


                    }else{
                        Log.d("child=2","child2&willbeshown");
                        test1.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d("child=!2","child!2");
                    test1.setVisibility(View.GONE);
                }
            }


        });
    }
}
