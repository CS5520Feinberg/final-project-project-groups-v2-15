package edu.northeastern.plantr;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        db.child("plants").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                        String plantID = snapshot.child("name").getValue().toString();
                        db.child("plants").addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            if (child.getKey().equals(plantID)) {
                                                Plant newPlant = new Plant(child.child("name").getValue(String.class), child.child("plantSpecies").getValue(String.class));
                                                plantList.add(0, newPlant);
                                            }
                                            if (rviewAdapter != null) {
                                                rviewAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
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
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String plantID = child.child("name").getValue().toString();
                            Log.w("My Plant ID", plantID);
                            db.child("plants").addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                Plant newPlant = new Plant(child.child("name").getValue(String.class), child.child("plantSpecies").getValue(String.class));
                                                plantList.add(0, newPlant);
                                            }
                                            rviewAdapter.notifyItemInserted(0);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.w("Check Data", "Bad Data");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
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

    protected void createNewPlant(Integer number, String newName, String newSpecies, View view) {
        Plant newPlant = new Plant(number, newName, newSpecies);
        /*
        Add the current User gets this new plant added to their plant array here
         */
        plantList.add(0, newPlant);
        rviewAdapter.notifyItemInserted(0);
        db.child("plants").push().setValue(newPlant);
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
                Log.i("Tag", "Permission grantged by user");
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
                createNewPlant(id, plantName, plantSpecies, view);
            }
        });
        builder.show();
    }

}
