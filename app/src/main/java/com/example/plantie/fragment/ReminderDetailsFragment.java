package com.example.plantie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.plantie.R;
import com.example.plantie.domen.Reminder;
import com.example.plantie.help.DatabaseBroker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ReminderDetailsFragment extends Fragment {

    private ImageView imageView;
    private Button btnDelete;
    private TextView txtCommonName, txtFamily, txtScientific;
    private ConstraintLayout clDate;
    private EditText etDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_details, container, false);
        Bundle bundle = this.getArguments();
        final Reminder reminder = bundle.getParcelable("reminder");

        imageView = view.findViewById(R.id.imgPlantAPI);
        btnDelete = view.findViewById(R.id.btnDeleteReminder);
        txtCommonName = view.findViewById(R.id.txtCommonNameAPI);
        txtFamily = view.findViewById(R.id.txtFamilyAPI);
        txtScientific = view.findViewById(R.id.txtScientificNameAPI);
        clDate = view.findViewById(R.id.clPickedDate);
        etDate = view.findViewById(R.id.etDate);
        clDate.setVisibility(View.VISIBLE);

        txtCommonName.setText(reminder.getPlant().getCommonName());
        txtScientific.setText(reminder.getPlant().getScientificName());
        txtFamily.setText(reminder.getPlant().getFamilyCommonName());
        Picasso.get().load(reminder.getPlant().getImageURL()).fit().centerInside().into(imageView);
        etDate.setText(reminder.getDate());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean successful = DatabaseBroker.getInstance(getActivity()).deleteReminder(reminder);

                if (successful) {
                    Toast.makeText(getActivity(), "Reminder removed from your list of reminders!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "We weren't able to remove your reminder :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
