package edu.northeastern.plantr;

import android.content.DialogInterface;
import static java.lang.Integer.parseInt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class PlantDetails extends AppCompatActivity {
    TextView plantName;
    ImageView plantPhoto;
    TextView speciesName;
    TextView waterComments;
    String userID;
    String plantID;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_plant_details);
        plantName = findViewById(R.id.plantName);
        plantPhoto = findViewById(R.id.plantImage);
        speciesName = findViewById(R.id.speciesName);
        waterComments = findViewById(R.id.waterText);
        //TODO Fix THis
        userID = "myFarmer";
        plantID = getIntent().getStringExtra("plantID");
        Log.w("PlantID", plantID);
        String plantNameLoaded = getIntent().getStringExtra("plantName");
        plantName.setText(plantNameLoaded);
        String speciesNameLoaded = getIntent().getStringExtra("speciesName");
        speciesName.setText("Species: " + speciesNameLoaded);
        db = FirebaseDatabase.getInstance().getReference();
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
            if(!plantHeight.equals("")) {
                int plantHeightInt = parseInt(plantHeight);
                DatabaseReference plantRef = db.child("Users").child(userID).child("plantList").child(plantID);
                String date = java.time.LocalDate.now().toString();
                Growth newGrowth = new Growth(date, plantHeightInt);
                plantRef.child("growth").push().setValue(newGrowth);
            }
            if(watered){
                String date = java.time.LocalDate.now().toString();
                DatabaseReference wateredArray = db.child("Users").child(userID).child("plantList").child(plantID);
                boolean wateredToday = false;
                wateredArray.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        Iterable<DataSnapshot> waterArray = dataSnapshot.child("watered").getChildren();
                        for(DataSnapshot waterChild: waterArray){
                            if(waterChild.child("value").equals(java.time.LocalDate.now().toString())){
                                //wateredToday = true;
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError){
                        Log.d("Bad", "BadData");
                    }
                });
                if(!wateredToday){
                    db.child("Users").child(userID).child("plantList").child(plantID).child("watered").push().setValue(date);
                }
            }
        });

        builder.show();

    }


}
