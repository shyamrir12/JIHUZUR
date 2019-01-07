package com.example.awizom.jihuzur.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.R;

public class SearchFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View view= inflater.inflate(R.layout.activity_search,container,false);
       initView(view);
       return  view;
    }
    private void initView(View view)
    {
        TextView search=view.findViewById(R.id.message);
    }
}
