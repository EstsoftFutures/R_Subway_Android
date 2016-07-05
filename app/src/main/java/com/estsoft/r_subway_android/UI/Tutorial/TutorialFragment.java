package com.estsoft.r_subway_android.UI.Tutorial;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.MainActivity;
import com.estsoft.r_subway_android.R;


public class TutorialFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static TutorialFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TutorialFragment fragment = new TutorialFragment();
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
        //PAGE받기
        mPage = getArguments().getInt(ARG_PAGE);

        switch (mPage) {

            case 1:
                LinearLayout linearLayout1 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page1, container, false);
                LinearLayout background1 = (LinearLayout) linearLayout1.findViewById(R.id.background);
                TextView page_num1 = (TextView) linearLayout1.findViewById(R.id.page_num);
                page_num1.setText(String.valueOf(1));
                background1.setBackground(new ColorDrawable(0xff6dc6d2));
                return linearLayout1;
            case 2:
                LinearLayout linearLayout2 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page2, container, false);
                LinearLayout background2 = (LinearLayout) linearLayout2.findViewById(R.id.background);
                TextView page_num2 = (TextView) linearLayout2.findViewById(R.id.page_num);
                page_num2.setText(String.valueOf(2));
                background2.setBackground(new ColorDrawable(0xff26abb5));
                return linearLayout2;
            case 3:
                LinearLayout linearLayout3 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page3, container, false);
                LinearLayout background3 = (LinearLayout) linearLayout3.findViewById(R.id.background);
                TextView page_num3 = (TextView) linearLayout3.findViewById(R.id.page_num);
                page_num3.setText(String.valueOf(3));
                background3.setBackground(new ColorDrawable(0xff008c9e));

                return linearLayout3;
            case 4:
                LinearLayout linearLayout4 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page4, container, false);
                LinearLayout background4 = (LinearLayout) linearLayout4.findViewById(R.id.background);
                TextView page_num4 = (TextView) linearLayout4.findViewById(R.id.page_num);
                page_num4.setText(String.valueOf(4));
                background4.setBackground(new ColorDrawable(0xff009c9e));
                Button btn = (Button) linearLayout4.findViewById(R.id.TutorialF);
                btn.setOnClickListener(this);
                return linearLayout4;
            default:
                LinearLayout linearLayoutD = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page4, container, false);
                LinearLayout backgroundD = (LinearLayout) linearLayoutD.findViewById(R.id.background);
                TextView page_numD = (TextView) linearLayoutD.findViewById(R.id.page_num);
                page_numD.setText("Error");
                backgroundD.setBackground(new ColorDrawable(0xffff0000));
                Button btnD = (Button) linearLayoutD.findViewById(R.id.TutorialF);
                btnD.setOnClickListener(this);
                return linearLayoutD;
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}