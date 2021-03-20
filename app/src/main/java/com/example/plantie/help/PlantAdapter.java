package com.example.plantie.help;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.fragment.AddReminderFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context mContext;
    private ArrayList<Plant> mPlants;
    private FragmentManager fragmentManager;

    public PlantAdapter(Context mContext, ArrayList<Plant> mPlants, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mPlants = mPlants;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        final Plant currentPlant = mPlants.get(position);

        String name = currentPlant.getCommonName();
        String imageURL = currentPlant.getImageURL();
        Picasso.get().load(imageURL).fit().centerInside().into(holder.imgPlant);
        holder.txtPlantName.setText(name);

        holder.btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("plant", currentPlant);
                AddReminderFragment addReminderFragment = new AddReminderFragment();
                addReminderFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, addReminderFragment, addReminderFragment.getTag()).commit();
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean successful = DatabaseBroker.getInstance(mContext).removePlant(currentPlant);
                if (successful) {
                    Toast.makeText(mContext, "Plant was removed from your garden!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "We weren't able to remove this plant from your garden :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlants.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPlant, imgDelete;
        public Button btnAddReminder;
        public TextView txtPlantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlant = itemView.findViewById(R.id.imgPlantAPI);
            btnAddReminder = itemView.findViewById(R.id.btnAddWatering);
            txtPlantName = itemView.findViewById(R.id.txtCommonNameAPI);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }


}
