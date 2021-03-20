package com.example.plantie.domen;

import android.net.Uri;

public class User {
    int userID;
    String firstName;
    String lastName;
    String email;
    String username;
    Uri profilePicture;

    public User() {
    }

    public User(int userID, String firstName, String lastName, String email, String username, Uri profilePicture) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }
}
