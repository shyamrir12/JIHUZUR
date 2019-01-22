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
import android.widget.Toast;

import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CatalogFragment extends Fragment implements View.OnClickListener {
    private CardView homeCleaningCardView;
    private ImageView homecleaning;
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
        homeCleaningCardView.setOnClickListener(this);
        homecleaning = view.findViewById(R.id.homecleaning);
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
        if (v.getId() == homecleaning.getId()) {

        }
    }

    private boolean addCatalog() {

        datauserprofile = FirebaseDatabase.getInstance().getReference("Catalog");

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String homecleaningTextView = homeCleaningTextView.getText().toString();


        Toast.makeText(getContext(), "DataProfile Added", Toast.LENGTH_LONG).show();
        return true;
    }

}
