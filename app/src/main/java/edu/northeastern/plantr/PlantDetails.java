package edu.northeastern.plantr;

import android.content.DialogInterface;
import static java.lang.Integer.parseInt;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
        //Plant loadedPlant = (Plant)getIntent().getParcelableExtra("Plant");
        String plantNameLoaded = getIntent().getStringExtra("plantName");
        plantName.setText(plantNameLoaded);
        String speciesNameLoaded = getIntent().getStringExtra("speciesName");
        speciesName.setText("Species: " + speciesNameLoaded);
        //setProfileDetails(loadedPlant);
    }

    public void AddWater(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlantDetails.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_water_layout, null);
        builder.setView(v);
        CheckBox waterCheck = (CheckBox)v.findViewById(R.id.waterCheck);
        EditText heightUpdate = (EditText)v.findViewById(R.id.growthInput);
        builder.setNegativeButton("@string/cancel", (dialog, id)-> dialog.dismiss());
        builder.setPositiveButton("Update", (DialogInterface.OnClickListener) (dialog, id) -> {
            Boolean watered = waterCheck.isChecked();
            String plantHeight = heightUpdate.getText().toString();
            int plantHeightInt = parseInt(plantHeight);
        });

        builder.show();

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
