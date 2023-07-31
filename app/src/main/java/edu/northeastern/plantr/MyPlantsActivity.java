package edu.northeastern.plantr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MyPlantsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);

    }

    protected void createNewPlant(String newName, String newSpecies, View view){
        Plant newPlant = new Plant(newName, newSpecies);
    }

    public void fabAddPlantDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPlantsActivity.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_dialog_layout, null);
        builder.setView(v);
        EditText txt_PlantNameInput = (EditText)v.findViewById(R.id.plantNameEnter);
        EditText txt_PlantSpeciesInput = (EditText)v.findViewById(R.id.plantSpeciesEnter);
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        builder.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) (dialog, id) -> {
            String plantName = txt_PlantNameInput.getText().toString();
            String plantSpecies = txt_PlantSpeciesInput.getText().toString();
            if(plantName.length() == 0 || plantSpecies.length() == 0){
                Toast.makeText(MyPlantsActivity.this, "Please Enter Info", Toast.LENGTH_SHORT)
                        .show();
            }else{
                createNewPlant(plantName, plantSpecies, view);
            }
        });
        builder.show();
    }

}
