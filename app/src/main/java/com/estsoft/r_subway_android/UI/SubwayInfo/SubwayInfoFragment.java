package com.estsoft.r_subway_android.UI.SubwayInfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;


public class SubwayInfoFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static SubwayInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SubwayInfoFragment fragment = new SubwayInfoFragment();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_subway_info, container, false);
        TextView txtview2 = (TextView) view.findViewById(R.id.textTest2);
        TextView txtview3 = (TextView) view.findViewById(R.id.textTest3);
        TextView txtview4 = (TextView) view.findViewById(R.id.textTest4);
        TextView txtview5 = (TextView) view.findViewById(R.id.textTest5);
        TextView txtview6 = (TextView) view.findViewById(R.id.textTest6);
        TextView txtview7 = (TextView) view.findViewById(R.id.textTest7);
/*        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText("Fragment #" + mPage);*/
        return view;
    }
}
