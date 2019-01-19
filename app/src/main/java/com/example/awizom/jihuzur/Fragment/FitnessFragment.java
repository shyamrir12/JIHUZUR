package com.example.awizom.jihuzur.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.awizom.jihuzur.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FitnessFragment extends ListFragment {


    String[] Fitness = new String[]{
            "Massage For Men",
            "Fitness Trainer at Home",
            "Massage For Women",
            "Yoga Trainer at Home",
            "Physiotherapy at Home",
            "Dietician",
            "Men's HairCut & Grooming",

    };

    // Array of integers points to images stored in /res/drawable/
    int[] Icons = new int[]{
            R.drawable.massage,
            R.drawable.massage,
            R.drawable.massage,
            R.drawable.massage,
            R.drawable.massage,
            R.drawable.massage,
            R.drawable.massage
    };

    // Array of strings to store currencies


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.fragment_appliance, container, false);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Fitness[i]);

            hm.put("Icons", Integer.toString(Icons[i]));
            aList.add(hm);

        }

        // Keys used in Hashmap
        String[] from = {"Icons", "txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.txt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.fragment_fitness, from, to);

        setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        {
            Toast.makeText(getActivity(), "Clicked on ", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appliance, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_appliance) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }
}