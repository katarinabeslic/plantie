package com.example.plantie.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.help.DatabaseBroker;
import com.example.plantie.help.PlantAdapter;

import java.util.ArrayList;


public class MyGardenFragment extends Fragment {

    private Button btnAddMorePlants;
    private RecyclerView rvPlants;
    private ArrayList<Plant> mPlants;
    private PlantAdapter plantAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_garden, container, false);

        btnAddMorePlants = view.findViewById(R.id.btnAddMorePlants);
        rvPlants = view.findViewById(R.id.rvMyGarden);
        mPlants = DatabaseBroker.getInstance(getActivity()).getAllPlants();

        plantAdapter = new PlantAdapter(getActivity(), mPlants, getFragmentManager());
        rvPlants.setAdapter(plantAdapter);
        rvPlants.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        btnAddMorePlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SearchFragment searchFragment = new SearchFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, searchFragment, searchFragment.getTag()).commit();
            }
        });

        return view;
    }
}