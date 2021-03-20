package com.example.plantie.domen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Reminder implements Parcelable {
    int id;
    Plant plant;
    String date;

    public Reminder() {
    }

    public Reminder(int id, Plant plant, String date) {
        this.id = id;
        this.plant = plant;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(plant, reminder.plant) &&
                Objects.equals(date, reminder.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plant, date);
    }

    protected Reminder(Parcel in) {
        id = in.readInt();
        plant = (Plant) in.readValue(Plant.class.getClassLoader());
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(plant);
        dest.writeString(date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}