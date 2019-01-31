package com.example.awizom.jihuzur.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.R;
import com.google.firebase.database.DatabaseReference;

public class CatalogFragment extends Fragment implements View.OnClickListener {
    private CardView homeCleaningCardView,appliancecardView;
    private ImageView homecleaning,appliance;
    private TextView homeCleaningTextView;
    DatabaseReference datauserprofile;
    private Intent intent;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalog_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        homeCleaningCardView = view.findViewById(R.id.homeCleancardViewOne);
        appliancecardView=view.findViewById(R.id.appliancecardView);
        homeCleaningCardView.setOnClickListener(this);
        appliancecardView.setOnClickListener(this);
        homecleaning = view.findViewById(R.id.homecleaning);
        appliance=view.findViewById(R.id.appliance);
        appliance.setOnClickListener(this);
        homecleaning.setOnClickListener(this);
        homeCleaningTextView = view.findViewById(R.id.homecleaningTextView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == homeCleaningCardView.getId()) {

            intent = new Intent(getActivity(), MenuActivity.class);
            startActivity(intent);
//            SearchOnlyFragment fragment1 = new SearchOnlyFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(android.R.id.content, fragment1);
//            fragmentTransaction.commit();
        }
        if(v.getId()==appliancecardView.getId())
        {
            intent = new Intent(getActivity(), MenuActivity.class);
            startActivity(intent);

        }

        if (v.getId() == homecleaning.getId()) {

        }
    }



}
