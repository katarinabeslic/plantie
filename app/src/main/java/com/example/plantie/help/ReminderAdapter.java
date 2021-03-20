package com.example.plantie.help;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.domen.Reminder;
import com.example.plantie.fragment.ReminderDetailsFragment;
import com.example.plantie.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context mContext;
    private ArrayList<Reminder> mReminders;
    private FragmentManager fragmentManager;

    public ReminderAdapter(Context mContext, ArrayList<Reminder> mReminders, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mReminders = mReminders;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        final Reminder currentReminder = mReminders.get(position);

        View view = holder.itemView;

        holder.txtPlantName.setText(currentReminder.getPlant().getCommonName());
        holder.txtNextReminder.setText(currentReminder.getDate());
        Picasso.get().load(currentReminder.getPlant().getImageURL()).fit().centerInside().into(holder.imgPlant);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("reminder", currentReminder);

                ReminderDetailsFragment reminderDetailsFragment = new ReminderDetailsFragment();
                reminderDetailsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, reminderDetailsFragment, reminderDetailsFragment.getTag()).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPlant;
        public TextView txtPlantName, txtNextReminder;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlant = itemView.findViewById(R.id.imgPlantAPI);
            txtPlantName = itemView.findViewById(R.id.txtCommonNameAPI);
            txtNextReminder = itemView.findViewById(R.id.txtDateReminder);
        }
    }
}
