package com.example.plantie.domen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Plant implements Serializable, Parcelable {
    private int id;
    private String commonName;
    private String familyCommonName;
    private String scientificName;
    private String slug;
    private String imageURL;

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", commonName='" + commonName + '\'' +
                ", familyCommonName='" + familyCommonName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", slug='" + slug + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public Plant() {
    }

    public Plant(int id, String commonName, String familyCommonName, String scientificName, String slug, String imageURL) {
        this.id = id;
        this.commonName = commonName;
        this.familyCommonName = familyCommonName;
        this.scientificName = scientificName;
        this.slug = slug;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getFamilyCommonName() {
        return familyCommonName;
    }

    public void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(commonName, plant.commonName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commonName);
    }

    protected Plant(Parcel in) {
        id = in.readInt();
        commonName = in.readString();
        familyCommonName = in.readString();
        scientificName = in.readString();
        slug = in.readString();
        imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(commonName);
        dest.writeString(familyCommonName);
        dest.writeString(scientificName);
        dest.writeString(slug);
        dest.writeString(imageURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Plant> CREATOR = new Parcelable.Creator<Plant>() {
        @Override
        public Plant createFromParcel(Parcel in) {
            return new Plant(in);
        }

        @Override
        public Plant[] newArray(int size) {
            return new Plant[size];
        }
    };
}