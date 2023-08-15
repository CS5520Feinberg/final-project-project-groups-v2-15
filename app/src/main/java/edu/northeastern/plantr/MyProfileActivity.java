package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class MyProfileActivity extends AppCompatActivity {
    private String username;
    private String firstName;
    private String lastName;
    private String favePlant;   // user can edit
    private String lastActivity;    // system change only
    private int profPic;  // user can edit
    private BottomNavigationView navBar;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // get user info
        username = plantrAutologin.getUsername(this);
        firstName = plantrAutologin.getFirstName(this);
        lastName = plantrAutologin.getLastName(this);
        favePlant = plantrAutologin.getFavePlant(this);
        lastActivity = plantrAutologin.getLastActivity(this);
        // profPic = "";
/*
        String photoName = newName + ".jpg";
        StorageReference newPhoto = photoDB.child(photoName);
        Log.w("Checking Photo obj", "---" + photoDB.child(photoName) + "---");
        //Convert photo to storeable data
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        String imageB64;
        if(photoStore == null){
            imageB64 = "null";
        }else {
            photoStore.compress(Bitmap.CompressFormat.PNG, 100, bao);
            photoStore.recycle();
            byte[] byteArray = bao.toByteArray();
            imageB64 = Base64.getEncoder().encodeToString(byteArray);
        }
*/
        // set text values
        editNameText(firstName, lastName);
        editUsernameText(username);
        editFavePlantText(favePlant);
        editLastActivityText(lastActivity);
        editProfPic(profPic);

        db = FirebaseDatabase.getInstance().getReference();
        db.child("users").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("EventListener", "Child Added");
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("Event Listener", "Child Changed");
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

    public void changeLastActivity () {
        // TODO: implement this
        // no user input, get data from system
        // save last activity to database with login info

        String newLastActivity = "";
        editLastActivityText(newLastActivity);
    }

    // text change helper fns
    protected void editNameText(String newFirstName, String newLastName) {
        ((TextView) findViewById(R.id.nameText)).setText(
                String.format("Name: %s %s", newFirstName, newLastName));
        firstName = newFirstName;
        lastName = newLastName;
    }

    protected void editUsernameText (String newUsername) {
        ((TextView) findViewById(R.id.usernameText)).setText(
                String.format("Username: %s", newUsername));
        username = newUsername;
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

    protected void editProfPic(int newImgURL) {
        ((ImageView) findViewById(R.id.profilePic)).setImageResource(newImgURL);
        profPic = newImgURL;
    }
}
