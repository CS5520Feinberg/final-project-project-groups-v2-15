package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlantrHomePage extends AppCompatActivity {

    /*
    BottomNavigationView navBar;
    FragmentMyPlants myPlantsFragment = new FragmentMyPlants();
    FragmentSettings mySettingsFragment = new FragmentSettings();
    FragmentMyProfile myProfileFragment = new FragmentMyProfile();
     */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);
        /*
        navBar = findViewById(R.id.navBar);
        navBar.setOnNavigationItemSelectedListener(this);
        navBar.setSelectedItemId(R.id.plantsNav);
         */
    }

    public void openMyPlants(View view){
        Intent myPlantsIntent = new Intent(this, MyPlantsActivity.class);
        startActivity(myPlantsIntent);
    }
    public void openProfile(View view){
        Intent profileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(profileIntent);
    }
    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

}