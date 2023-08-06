package edu.northeastern.plantr;

import android.content.DialogInterface;
import static java.lang.Integer.parseInt;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

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
        String plantNameLoaded = getIntent().getStringExtra("plantName");
        plantName.setText(plantNameLoaded);
        String speciesNameLoaded = getIntent().getStringExtra("speciesName");
        speciesName.setText("Species: " + speciesNameLoaded);
    }

    public void AddWater(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlantDetails.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_water_layout, null);
        builder.setView(v);
        CheckBox waterCheck = (CheckBox)v.findViewById(R.id.waterCheck);
        EditText heightUpdate = (EditText)v.findViewById(R.id.growthInput);
        builder.setNegativeButton("Cancel", (dialog, id)-> dialog.dismiss());
        builder.setPositiveButton("Update", (DialogInterface.OnClickListener) (dialog, id) -> {
            Boolean watered = waterCheck.isChecked();
            String plantHeight = heightUpdate.getText().toString();
            int plantHeightInt = parseInt(plantHeight);
            //TODO: Push to Database to update plant
            if(watered){
                LocalDate date = java.time.LocalDate.now();
            }
        });

        builder.show();

    }

}
