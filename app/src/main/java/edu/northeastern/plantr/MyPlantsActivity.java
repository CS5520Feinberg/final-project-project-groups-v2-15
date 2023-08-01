package edu.northeastern.plantr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyPlantsActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlantAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        createRecyclerView();
    }

    private void createRecyclerView() {
        rLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.myPlantsRecycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new PlantAdapter(plantList, this);
        PlantClickListener plantClickListener = position -> {
            String plantName = plantList.get(position).getName();
            rviewAdapter.notifyItemChanged(position);
            Toast.makeText(MyPlantsActivity.this, plantName, Toast.LENGTH_LONG).show();
        };
        rviewAdapter.setOnClickListener(plantClickListener);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManager);
    }

    protected void createNewPlant(String newName, String newSpecies, View view){
        Plant newPlant = new Plant(newName, newSpecies);
        plantList.add(0, newPlant);
        rviewAdapter.notifyItemInserted(0);
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
