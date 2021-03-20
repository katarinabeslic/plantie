package com.example.plantie.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.help.APIPlantsAdapter;
import com.example.plantie.help.DatabaseBroker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchByNameFragment extends Fragment {

    private APIPlantsAdapter mAdapter;
    private RecyclerView rvApiResults;
    private Button btnSearch;
    private EditText etPlantName;
    private RequestQueue mRequestQueue;
    private Button btnAddToGarden;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_by_name, container, false);

        btnSearch = view.findViewById(R.id.btnSearchByName);
        etPlantName = view.findViewById(R.id.etPlantName);
        mRequestQueue = Volley.newRequestQueue(getContext());
        rvApiResults = view.findViewById(R.id.rvPlantsAPI);
        btnAddToGarden = view.findViewById(R.id.btnAddToGardenAPI);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plant = etPlantName.getText().toString();
                if (plant.isEmpty()) {
                    Toast.makeText(getActivity(), "You have to provide plant name", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ArrayList<Plant> apiResult = new ArrayList<>();

                String url = "https://trefle.io/api/v1/plants/search?token=tCt20OEU0hCdMv5V93IN8a_hSlyq3-tk_F84CzYwkV0&q=" +plant;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                String commonName = jo.getString("common_name");
                                String slug = jo.getString("slug");
                                String imgURL = jo.getString("image_url");
                                String familyCommonName = jo.getString("family_common_name");
                                String scientificName = jo.getString("scientific_name");

                                Plant plant = new Plant();
                                plant.setCommonName(commonName);

                                if (familyCommonName.equals("null") || familyCommonName.isEmpty()) {
                                    plant.setFamilyCommonName("No family");
                                } else {
                                    plant.setFamilyCommonName(familyCommonName);
                                }

                                plant.setSlug(slug);
                                plant.setScientificName(scientificName);
                                plant.setImageURL(imgURL);

                                apiResult.add(plant);
                            }

                            mAdapter = new APIPlantsAdapter(getActivity(), apiResult);
                            rvApiResults.setAdapter(mAdapter);
                            rvApiResults.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mRequestQueue.add(request);
            }
        });

        return view;
    }
}