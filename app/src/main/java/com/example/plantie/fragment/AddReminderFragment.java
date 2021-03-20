package com.example.plantie.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.allyants.notifyme.NotifyMe;
import com.example.plantie.R;
import com.example.plantie.domen.Plant;
import com.example.plantie.domen.Reminder;
import com.example.plantie.help.DatabaseBroker;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;


public class AddReminderFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    
    private Button btnDatePicker, btnSaveReminder;
    private EditText etDate;
    private TextView txtCommonName, txtScientificName, txtFamily;
    private ConstraintLayout clReminder;
    private ImageView imgPlant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        Bundle bundle = this.getArguments();
        final Plant plant = (Plant) bundle.getParcelable("plant");
        System.out.println(plant.toString());

        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnSaveReminder = view.findViewById(R.id.btnDeleteReminder);
        clReminder = view.findViewById(R.id.clPickedDate);
        etDate = view.findViewById(R.id.etDate);
        txtCommonName = view.findViewById(R.id.txtCommonNameAPI);
        txtFamily = view.findViewById(R.id.txtFamilyAPI);
        txtScientificName = view.findViewById(R.id.txtScientificNameAPI);
        imgPlant = view.findViewById(R.id.imgPlantAPI);

        fillForm(plant);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btnSaveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "You have to pick a date for the reminder!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Reminder reminder = new Reminder();
                reminder.setPlant(plant);
                reminder.setDate(etDate.getText().toString());
                boolean successful = DatabaseBroker.getInstance(getActivity()).addReminder(reminder);

                if (successful) {
                    Toast.makeText(getActivity(), "Reminder added to your list of reminders!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "We weren't able to add your reminder :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void fillForm(Plant plant) {
        String commonName = plant.getCommonName();
        String family = plant.getFamilyCommonName();
        String scientificName = plant.getScientificName();
        String imageURL = plant.getImageURL();
        System.out.println(commonName + "\n" + scientificName + "\n" + family + "\n" + imageURL);

        txtCommonName.setText(commonName);
        txtFamily.setText(family);
        txtScientificName.setText(scientificName);
        txtFamily.setText(family);
        Picasso.get().load(imageURL).fit().centerInside().into(imgPlant);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        String pickedDate = DateFormat.getDateInstance().format(calendar.getTime());

        etDate.setText(pickedDate);
        clReminder.setVisibility(View.VISIBLE);

        NotifyMe notifyMe = new NotifyMe.Builder(getActivity().getApplicationContext())
                .title("Time to water!")
                .content("please don't let your plants die")
                .color(255,255,255,255)
                .led_color(255,255,255,255)
                .time(calendar.getTime())
                .addAction(new Intent(), "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", false)
                .addAction(new Intent(), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
