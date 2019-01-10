package com.example.awizom.jihuzur.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.awizom.jihuzur.R;

public class MyBookingFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mybooking,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {



    }
    }