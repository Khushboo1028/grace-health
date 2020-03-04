package com.replon.www.grace_thehealthapp.Nearby;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;


public class NearbyFragment extends Fragment {

    public static final String TAG = "NearbyFragment";

    GridView gridView;
    View view;
    NearbyGridAdapter nearbyGridAdapter;
    int[] icons={R.drawable.ic_hospital_nearby,R.drawable.ic_pharmacies_nearby,R.drawable.ic_donate_nearby,R.drawable.ic_blood_bank_nearby,R.drawable.ic_ngo_nearby,R.drawable.ic_charity_nearby};
    String[] places;



    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nearby, container, false);

        init();

        gridView.setAdapter(nearbyGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),PlacesActivity.class);
                intent.putExtra("title",places[position]);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });




        return view;
    }

    private void init(){
        gridView = (GridView) view.findViewById(R.id.grid_view);

        places = getResources().getStringArray(R.array.nearby_places);

        nearbyGridAdapter = new NearbyGridAdapter(getActivity(),icons,places);
    }

}
