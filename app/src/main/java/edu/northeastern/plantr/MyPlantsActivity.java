package edu.northeastern.plantr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MyPlantsActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlantAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private DatabaseReference db;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        db = FirebaseDatabase.getInstance().getReference();
        currentUser = "Farmer Joe";
        db.child("plants").addChildEventListener(
                new ChildEventListener(){
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                        String plantID = snapshot.child("name").getValue().toString();
                        db.child("plants").addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot){
                                        for(DataSnapshot child: snapshot.getChildren()){
                                            if(child.getKey().equals(plantID)){
                                                Plant newPlant = new Plant(child.child("name").getValue(String.class), child.child("plantSpecies").getValue(String.class));
                                                plantList.add(0, newPlant);
                                            }
                                            if(rviewAdapter != null){
                                                rviewAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error){
                                        Log.w("check Data", "Bad Data");
                                    }
                                }
                        );
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("Data Check", "Child Changed");
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Log.w("Data Check", "Child Removed");
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("Data Check", "Child Moved");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Data Check", "Bad Data");
                    }
                }
        );
        createRecyclerView();
    }

    private void createRecyclerView() {
        rLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.myPlantsRecycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new PlantAdapter(plantList, this);
        db.child("plants").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        for(DataSnapshot child : snapshot.getChildren()){
                            String plantID = child.child("name").getValue().toString();
                            Log.w("My Plant ID", plantID);
                            db.child("plants").addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot){
                                            for(DataSnapshot child: snapshot.getChildren()){
                                                Plant newPlant = new Plant(child.child("name").getValue(String.class), child.child("plantSpecies").getValue(String.class));
                                                plantList.add(0, newPlant);
                                            }
                                            rviewAdapter.notifyItemInserted(0);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error){
                                            Log.w("Check Data", "Bad Data");
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        Log.w("Check Data", "Bad Data");
                    }
                }
        );
        PlantClickListener plantClickListener = position -> {
            String plantName = plantList.get(position).getName();
            rviewAdapter.notifyItemChanged(position);
            Toast.makeText(MyPlantsActivity.this, plantName, Toast.LENGTH_LONG).show();
        };
        rviewAdapter.setOnClickListener(plantClickListener);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManager);
    }

    protected void createNewPlant(Integer number, String newName, String newSpecies, View view){
        Plant newPlant = new Plant(number, newName, newSpecies);
        /*
        Add the current User gets this new plant added to their plant array here
         */
        plantList.add(0, newPlant);
        rviewAdapter.notifyItemInserted(0);
        db.child("plants").push().setValue(newPlant);
    }

    public void addPlantPhoto(View view){
        /*TODO: Create Photo API to take a Photo*/
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
                createNewPlant(id, plantName, plantSpecies, view);
            }
        });
        builder.show();
    }

}
