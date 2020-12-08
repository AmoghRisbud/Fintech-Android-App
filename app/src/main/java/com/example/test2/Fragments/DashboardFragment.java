package com.example.test2.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test2.ways_to_grow.MainActivity2;
import com.example.test2.ways_to_grow.NeedForInvest;
import com.example.test2.R;
import com.example.test2.ways_to_grow.ShareMarket;

public class DashboardFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragmnt_dash,container,false);
        TextView mutual_funds = (TextView)view.findViewById(R.id.mutual_funds);
        TextView need_for_invest=(TextView) view.findViewById(R.id.needforinvestment);
        TextView share_market=(TextView) view.findViewById(R.id.share_mrket);
        TextView govt_rules=view.findViewById(R.id.govt_rules);


        mutual_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);
            }
        });
        need_for_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NeedForInvest.class);
                startActivity(intent);
            }
        });
        share_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareMarket.class);
                startActivity(intent);
            }
        });

        return view;


    }

}
