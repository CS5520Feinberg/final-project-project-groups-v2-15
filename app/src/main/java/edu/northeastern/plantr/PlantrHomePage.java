package edu.northeastern.plantr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PlantrHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);
    }

    public void openMyPlants(View view){
        Intent myPlantsIntent = new Intent(this, MyPlantsActivity.class);
        startActivity(myPlantsIntent);
    }
    public void openProfile(View view){
        Intent profileIntent = new Intent(this, myProfileActivity.class);
        startActivity(profileIntent);
    }
     /*
    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
*/
}