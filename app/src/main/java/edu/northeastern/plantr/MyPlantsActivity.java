package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.Manifest;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyPlantsActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlantAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private DatabaseReference db;
    private static final int pic_id = 123;
    private BottomNavigationView navBar;
    private ArrayList<CharSequence> plantTypes;

    private int sorted; //0 for unsorted, 1 for sorted a-z, 2 for sorted z-a

    //TODO Edit this to correctly get current user
    private String userID;
    private Bitmap photoStore;
    FirebaseStorage storage;
    StorageReference photoDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        //TODO Get user ID
        userID = plantrAutologin.getUsername(this);
        db = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        photoDB = storage.getReference().child("Images").child(userID);
        //Set up Camera Stuff
        photoStore = null;
        sorted = 0;
        createRecyclerView();

        // Navbar setup
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.plantsNav);
        navBar.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.plantsNav) {
                    return true;

                } else if (item.getItemId() == R.id.profileNav) {
                    startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                } else {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
            }
        });

        // Filter Spinner
        /*
        plantTypes = new ArrayList<>();
        for (Plant plant:plantList) {
            if(!plantTypes.contains(plant.getPlantSpecies())) {
                plantTypes.add(plant.getPlantSpecies());
            }
        }
         */
        plantTypes = new ArrayList<>();
        plantTypes.add("All Plants");
        //plantTypes.add("Test2");
        //plantTypes.add("Test3");
        Spinner filterSpinner = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> plantAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, plantTypes);
        //ArrayAdapter<CharSequence> plantAdapter = ArrayAdapter.createFromResource
        //        (this, R.array.f1_spinner_array, android.R.layout.simple_dropdown_item_1line);
        plantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(plantAdapter);
        /*
        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("Made it Here", "---===---");
                //Toast.makeText(parent.getContext(),
                //        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                //        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.w("Fuck you Java", "---===---");
            }
        };
         */
        //SpinnerActivity selectedListener = new SpinnerActivity();
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Intent newUserIntent = new Intent(MyPlantsActivity.this, PlantrLoginActivity.class);
                //startActivity(newUserIntent);
                Log.d("Made it Here", parentView.getItemAtPosition(position).toString());
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d("Fuck you Java", "---===---");
                // your code here
            }

        });
    }

    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            Log.d("Made it Here", "---===---");
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            Log.d("Fuck you Java", "---===---");
            // Another interface callback
        }
    }

    // Recycler View
    private void createRecyclerView() {
        rLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.myPlantsRecycler);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new PlantAdapter(plantList, this);
        db.child("Users").child(userID).child("plantList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String plantID = child.getKey();
                            String plantName = child.child("name").getValue().toString();
                            String plantSpecies = child.child("plantSpecies").getValue().toString();
                            String plantPhoto = child.child("plantPic").getValue().toString();
                            Plant newPlant = new Plant(plantID, plantName, plantSpecies, plantPhoto);
                            plantList.add(0, newPlant);
                            rviewAdapter.notifyItemInserted(0);
                            if(!plantTypes.contains(child.child("plantSpecies").getValue().toString())) {
                                plantTypes.add(child.child("plantSpecies").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Check Data", "Bad Data");
                    }
                }
        );
        PlantClickListener plantClickListener = position -> {
            String plantID = plantList.get(position).getPlantID();
            String plantName = plantList.get(position).getName();
            String speciesName = plantList.get(position).getPlantSpecies();
            String plantPhoto = plantList.get(position).getPlantPic();
            if(plantPhoto == null){
                plantPhoto = "null";
            }
            rviewAdapter.notifyItemChanged(position);

            //Send Intent with Plant ID
            Intent plantIntent = new Intent(this, PlantDetails.class);
            plantIntent.putExtra("plantID", plantID);
            plantIntent.putExtra("plantName", plantName);
            plantIntent.putExtra("speciesName", speciesName);
            plantIntent.putExtra("plantPic", plantPhoto);
            startActivity(plantIntent);
        };
        rviewAdapter.setOnClickListener(plantClickListener);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManager);
    }

    protected void createNewPlant(String newName, String newSpecies, View view) {
        String photoName = newName + ".jpg";
        StorageReference newPhoto = photoDB.child(photoName);
        Log.w("Checking Photo obj", "---" + photoDB.child(photoName) + "---");
        //Convert photo to storeable data
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        photoStore.compress(Bitmap.CompressFormat.PNG, 100, bao);
        photoStore.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.getEncoder().encodeToString(byteArray);
        Plant newPlant = new Plant(newName, newSpecies, imageB64);
        //Add the current User gets this new plant added to their plant array here
        plantList.add(0, newPlant);
        rviewAdapter.notifyItemInserted(0);
        db.child("Users").child(userID).child("plantList").push().setValue(newPlant);
    }

    private void setupPermissions(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.i("Permissions", "Permission to Snap Photo Denied");
            getCameraPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Log.i("Tag", "Permission Has been denied by user");
            }else{
                Log.i("Tag", "Permission granted by user");
            }
        }
    }

    protected void getCameraPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                101);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bundle extras = data.getExtras();
            photoStore = (Bitmap)extras.get("data");
            //imageView.setImageBitmap(imageBitmap);
        }
    }

    public void addPlantPhoto(View view) {
        //Get THe permissions
        setupPermissions();
        /*TODO: Create Photo API to take a Photo*/
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
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

    @SuppressLint("NotifyDataSetChanged")
    public void sortButton(View view){
        if(sorted==0 || sorted==2){
            Collections.sort(plantList, new Comparator(){
                public int compare(Object o1, Object o2) {
                    Plant p1 = (Plant) o1;
                    Plant p2 = (Plant) o2;
                    return p1.getName().compareToIgnoreCase(p2.getName());
                }
            });
            sorted = 1;
            rviewAdapter.notifyDataSetChanged();
        }
        else if(sorted==1){
            Collections.sort(plantList, new Comparator(){
                public int compare(Object o1, Object o2) {
                    Plant p1 = (Plant) o1;
                    Plant p2 = (Plant) o2;
                    return p2.getName().compareToIgnoreCase(p1.getName());
                }
            });
            sorted = 2;
            rviewAdapter.notifyDataSetChanged();
        }
    }
}
