package com.example.plantie.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.domen.Reminder;
import com.example.plantie.help.DatabaseBroker;
import com.example.plantie.help.PlantAdapter;
import com.example.plantie.help.ReminderAdapter;

import java.util.ArrayList;

public class RemindersFragment extends Fragment {

    private RecyclerView rvReminders;
    private ArrayList<Reminder> mReminders;
    private ReminderAdapter reminderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        mReminders = DatabaseBroker.getInstance(getActivity()).getAllReminders();
        rvReminders = view.findViewById(R.id.rvWatering);
        reminderAdapter = new ReminderAdapter(getActivity(), mReminders, getFragmentManager());
        rvReminders.setAdapter(reminderAdapter);
        rvReminders.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return view;
    }
}