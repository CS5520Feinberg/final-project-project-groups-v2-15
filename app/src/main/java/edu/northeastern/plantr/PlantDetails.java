package edu.northeastern.plantr;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

public class PlantDetails extends AppCompatActivity {
    TextView plantName;
    ImageView plantPhoto;
    TextView speciesName;
    TextView waterComments;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_plant_details);
        plantName = findViewById(R.id.plantName);
        plantPhoto = findViewById(R.id.plantImage);
        speciesName = findViewById(R.id.speciesName);
        waterComments = findViewById(R.id.waterText);
        Plant loadedPlant = (Plant)getIntent().getParcelableExtra("Plant");
        //setProfileDetails(loadedPlant);
    }

    protected void setProfileDetails(Plant loadedPlant){
        plantName.setText(loadedPlant.getName());
        Uri loadedPhoto = loadedPlant.getPlantPic();
        if(loadedPhoto != null){
            plantPhoto.setImageURI(loadedPhoto);
        }else{
            plantPhoto.setImageURI(null);
        }

    }
}
