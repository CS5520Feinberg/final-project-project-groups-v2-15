package edu.northeastern.plantr;

import android.content.DialogInterface;
import static java.lang.Integer.parseInt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class PlantDetails extends AppCompatActivity {
    protected TextView plantName;
    protected ImageView photoPlant;
    protected TextView speciesName;
    protected TextView waterComments;
    private String userID;
    private String plantID;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_plant_details);
        plantName = findViewById(R.id.plantName);
        photoPlant = findViewById(R.id.plantImage);
        speciesName = findViewById(R.id.speciesName);
        waterComments = findViewById(R.id.waterText);

        userID = plantrAutologin.getUsername(this);
        plantID = getIntent().getStringExtra("plantID");
        String plantNameLoaded = getIntent().getStringExtra("plantName");
        plantName.setText(plantNameLoaded);
        String speciesNameLoaded = getIntent().getStringExtra("speciesName");
        String plantPhoto = getIntent().getStringExtra("plantPic");

        //DECODE THE PLANT PHOTO
        if(plantPhoto.equals("null")){
            photoPlant.setImageResource(R.mipmap.ic_yellow_sticker_foreground);
        }else {
            byte[] decodedString = Base64.decode(plantPhoto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photoPlant.setImageBitmap(decodedByte);
        }
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
                                // wateredToday = true;
                                // if watered today, how do we determine the streak length?
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
                    // Katey's thoughts for "streak" logic:
                    // if not watered today, check if watered yesterday.
                    // if yes, keep streak, if no, set streak to 0 days
                }
            }
        });
        builder.show();
    }
}
