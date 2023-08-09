package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfileActivity extends AppCompatActivity {
    private String name;    // shouldn't need to change
    private String favePlant;   // user can edit
    private String lastActivity;    // system change only
    private Uri imgURL;  // user can edit
    private BottomNavigationView navBar;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        db = FirebaseDatabase.getInstance().getReference();
        db.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("EventListener", "Child Added");
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Log.w("Event Listener", "Child Removed");
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("Event Listener", "Child Moved");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Event Listener", "Action Cancelled");
                    }
                }
        );

        // close button
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button closeBtn = findViewById(R.id.closeProfileButton);
        closeBtn.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });

        // TODO: get user info from main activity somehow then save to global vars
        editNameText(name);
        editFavePlantText(favePlant);
        editLastActivityText(lastActivity);
        editProfPic(imgURL);

        // Navbar setup
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.profileNav);
        navBar.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.plantsNav) {
                    startActivity(new Intent(getApplicationContext(), MyPlantsActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                } else if (item.getItemId() == R.id.profileNav) {
                    return true;

                } else {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
            }
        });
    }


    // change profile picture
    public void changeProfPic () {
        // TODO: implement this
        // use activity dialog with pic upload?
        // save pic to database with login info
    }

    // change fave plant
    public void changeFavePlant () {
        // TODO: implement this
        // use activity dialog with text input field
        // save fave plant to database with login info

        String newFavePlant = "";
        editFavePlantText(newFavePlant);
    }

    public void changeLastActivity () {
        // TODO: implement this
        // no user input, get data from system
        // save last activity to database with login info

        String newLastActivity = "";
        editLastActivityText(newLastActivity);
    }


    // logout button
    public void logout(View view) {
        // TODO: implement logout logic
    }


    // helper fns
    protected void editNameText(String newName) {
        ((TextView) findViewById(R.id.nameText)).setText(
                String.format("Name: %s", newName));
        name = newName;
    }

    protected void editFavePlantText(String newFavePlant) {
        ((TextView) findViewById(R.id.favePlantText)).setText(
                String.format("Favorite Plant: %s", newFavePlant));
        favePlant = newFavePlant;
    }

    protected void editLastActivityText(String newLastActivity) {
        ((TextView) findViewById(R.id.lastActivityText)).setText(
                String.format("Last Activity: %s", newLastActivity));
        lastActivity = newLastActivity;
    }

    protected void editProfPic(Uri newImgURL) {
        ((ImageView) findViewById(R.id.profilePic)).setImageURI(newImgURL);
        imgURL = newImgURL;
    }
}
