package edu.northeastern.plantr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;

import android.Manifest;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyPlantsActivity extends AppCompatActivity {

    private List<Plant> plantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlantAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private DatabaseReference db;
    private CameraManager myCameraManager;
    private String myCameraID;
    private CameraDevice myCamera;
    private SurfaceView mySurfaceView;
    private SurfaceHolder mySurfaceHolder;
    private Surface mySurface;
    private CameraCaptureSession myCaptureSession;
    private BottomNavigationView navBar;

    //TODO Edit this to correctly get current user
    private String userID;

    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            myCamera = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            myCamera = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            myCamera = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        //TODO Get user ID
        userID = "myFarmer";
        db = FirebaseDatabase.getInstance().getReference();
        //Set up Camera Stuff
        myCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIDs = myCameraManager.getCameraIdList();
            if (cameraIDs.length > 0) {
                myCameraID = cameraIDs[0];
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myCameraManager.openCamera(myCameraID, mCameraStateCallback, null);
            }else{
                //No Camera Found
                Log.w("Camera Bug", "No Camera Found");
            }
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
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
    }

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
                            Plant newPlant = new Plant(plantID, plantName, plantSpecies);
                            plantList.add(0, newPlant);
                            rviewAdapter.notifyItemInserted(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Check Data", "Bad Data");
                    }
                }
        );
        PlantClickListener plantClickListener = position -> {
            String plantID = plantList.get(position).getID();
            String plantName = plantList.get(position).getName();
            String speciesName = plantList.get(position).getPlantSpecies();
            rviewAdapter.notifyItemChanged(position);


            //TODO: Make Intent to open Plant Screen
            //Send Intent with Plant ID
            Intent plantIntent = new Intent(this, PlantDetails.class);
            plantIntent.putExtra("plantID", plantID);
            plantIntent.putExtra("plantName", plantName);
            plantIntent.putExtra("speciesName", speciesName);
            startActivity(plantIntent);

        };
        rviewAdapter.setOnClickListener(plantClickListener);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManager);
    }

    private void createCaptureSession() {
        try{
            myCamera.createCaptureSession(
                    Collections.singletonList(mySurface),
                    new CameraCaptureSession.StateCallback(){
                        @Override
                        public void onConfigured(CameraCaptureSession session){
                            myCaptureSession = session;
                        }
                        @Override
                        public void onConfigureFailed(CameraCaptureSession session){
                            //Handle the Errror
                            Log.w("MyCamera", "Configure Failed");
                            myCaptureSession = null;
                        }
                    },
                    null
            );
        }catch(CameraAccessException e){
            e.printStackTrace();
        }
    }

    protected void createNewPlant(String newName, String newSpecies, View view) {
        Plant newPlant = new Plant(newName, newSpecies);
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

    public void addPlantPhoto(View view) {
        //Get THe permissions
        setupPermissions();
        /*TODO: Create Photo API to take a Photo*/
        try{
            Log.w("MyCamera", myCamera.toString());
            CaptureRequest.Builder captureBuilder = myCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            Log.w("my Catpure builder", captureBuilder.toString());
            captureBuilder.addTarget(mySurfaceHolder.getSurface());
            Log.w("my Capture session", myCaptureSession.toString());
            myCaptureSession.capture(captureBuilder.build(), null, null);
        }catch(CameraAccessException e){
            e.printStackTrace();
        }

    }

    public void fabAddPlantDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPlantsActivity.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_dialog_layout, null);
        builder.setView(v);
        EditText txt_PlantNameInput = (EditText)v.findViewById(R.id.plantNameEnter);
        EditText txt_PlantSpeciesInput = (EditText)v.findViewById(R.id.plantSpeciesEnter);
        //mySurfaceView = findViewById(R.id.surfaceView);
        //mySurfaceHolder = mySurfaceView.getHolder();
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
