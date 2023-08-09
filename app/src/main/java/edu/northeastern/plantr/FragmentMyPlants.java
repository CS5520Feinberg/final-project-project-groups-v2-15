package edu.northeastern.plantr;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMyPlants extends Fragment {

    public FragmentMyPlants() {
        startActivity(new Intent(getContext(), MyPlantsActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_my_plants, container, false);
        return inflater.inflate(R.layout.fragment_water_layout, container, false);
    }
}