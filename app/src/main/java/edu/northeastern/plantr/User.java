package edu.northeastern.plantr;

import android.net.Uri;

public class User {
    public String username;
    public String firstName;
    public String lastName;
    public String favePlant;
    public String lastActivity;
    public Uri profPic;

    public User() {}

    public User(String username, String firstName, String lastName,
                String favePlant, String lastActivity, Uri profPic) {

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.favePlant = favePlant;
        this.lastActivity = lastActivity;
        this.profPic = profPic;
    }

    public String getUsername(){
        return this.username;
    }
    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
}
