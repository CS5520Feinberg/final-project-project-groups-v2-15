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

public class PlantrHomePage extends AppCompatActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);

        navBar = findViewById(R.id.navBar);
        navBar.setOnNavigationItemSelectedListener(this);
        navBar.setSelectedItemId(R.id.plantsNav);
    }
    FragmentMyPlants myPlantsFragment = new FragmentMyPlants();
    FragmentSettings mySettingsFragment = new FragmentSettings();
    FragmentMyProfile myProfileFragment = new FragmentMyProfile();

    public void openMyPlants(View view){
        Intent myPlantsIntent = new Intent(this, MyPlantsActivity.class);
        startActivity(myPlantsIntent);
    }
    public void openProfile(View view){
        Intent profileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(profileIntent);
    }
    public void openSettings(View view){
        //Intent settingsIntent = new Intent(this, SettingsActivity.class);
        //startActivity(settingsIntent);
        Toast.makeText(this, "Settings Not Yet Ready", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    /*
        switch (item.getItemId()) {
            case R.id.plantsNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.plantsNav, myPlantsFragment)          //this is wrong TODO fix
                        .commit();
                return true;

            case R.id.profileNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.plantsNav, myProfileFragment)          //this is wrong TODO fix
                        .commit();
                return true;

            case R.id.settingsNav:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.plantsNav, mySettingsFragment)          //this is wrong TODO fix
                        .commit();
                return true;
        }
    */
        return false;
    }
}