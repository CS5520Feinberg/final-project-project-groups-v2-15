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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: This line will make it so you're always prompted to login by setting the username to an empty string, useful for testing login
        //plantrAutologin.setUserName(this, "");
        //TODO: If you're working and can't get past login screen, comment out the below "if" block
        if(plantrAutologin.getUserName(this).length() == 0) {
            Intent newUserIntent = new Intent(this, PlantrLoginActivity.class);
            startActivity(newUserIntent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);
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