package com.example.plantie.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class APIPlantsAdapter extends RecyclerView.Adapter<APIPlantsAdapter.PlantViewHolder> {

    private Context mContext;
    private ArrayList<Plant> mPlants;

    public APIPlantsAdapter(Context mContext, ArrayList<Plant> mPlants) {
        this.mContext = mContext;
        this.mPlants = mPlants;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.api_result_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        final Plant currentPlant = mPlants.get(position);

        String commonName = currentPlant.getCommonName();
        String family = currentPlant.getFamilyCommonName();
        String scientificName = currentPlant.getScientificName();
        String imgURL = currentPlant.getImageURL();
        String slug = currentPlant.getSlug();

        holder.txtCommonNameAPI.setText(commonName);
        holder.txtFamilyAPI.setText(family);
        holder.txtScientificNameAPI.setText(scientificName);
        holder.txtSlugAPI.setText(slug);
        Picasso.get().load(imgURL).fit().centerInside().into(holder.imgViewAPI);

        holder.btnAddToGardenAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean alreadyHaveIt = checkIfYouHaveIt(currentPlant);
                if (alreadyHaveIt) {
                    Toast.makeText(mContext, "Looks like you already have this one! Search for a different one..", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean successful = DatabaseBroker.getInstance(mContext).addPlant(currentPlant);
                if (successful) {
                    Toast.makeText(mContext, "Plant successfully added to your garden!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Oops! Looks like your plant wasn't added to your garden :(", Toast.LENGTH_SHORT).show();
                }

            }

            private boolean checkIfYouHaveIt(Plant plant) {
                ArrayList<Plant> plants = DatabaseBroker.getInstance(mContext).getAllPlants();
                for (Plant p : plants) {
                    if (p.equals(plant)){
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlants.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCommonNameAPI, txtFamilyAPI, txtScientificNameAPI, txtSlugAPI;
        public ImageView imgViewAPI;
        public Button btnAddToGardenAPI;

        public PlantViewHolder(@NonNull final View itemView) {
            super(itemView);


            txtCommonNameAPI = itemView.findViewById(R.id.txtCommonNameAPI);
            txtFamilyAPI = itemView.findViewById(R.id.txtFamilyAPI);
            txtSlugAPI = itemView.findViewById(R.id.txtSlugAPI);

            txtScientificNameAPI = itemView.findViewById(R.id.txtScientificNameAPI);
            imgViewAPI = itemView.findViewById(R.id.imgPlantAPI);
            btnAddToGardenAPI = itemView.findViewById(R.id.btnAddToGardenAPI);

        }
    }
}
