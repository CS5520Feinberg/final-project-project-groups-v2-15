package edu.northeastern.plantr;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private String newFavePlant;
    private String newFirstName;
    private String newLastName;

    //Set Up Photos
    DatabaseReference db;
    private Bitmap photoStore;
    private static final int pic_id = 123;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = FirebaseDatabase.getInstance().getReference();
        userID = plantrAutologin.getUserId(this);
        // Navbar setup
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.settingsNav);
        photoStore = null;
        navBar.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.plantsNav) {
                    startActivity(new Intent(getApplicationContext(), MyPlantsActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                } else if (item.getItemId() == R.id.profileNav) {
                    startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                } else {
                    return true;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bundle extras = data.getExtras();
            if(extras != null) {
                photoStore = (Bitmap) extras.get("data");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                photoStore.compress(Bitmap.CompressFormat.PNG, 100, bao);
                photoStore.recycle();
                byte[] byteArray = bao.toByteArray();
                String imageB64 = Base64.getEncoder().encodeToString(byteArray);
                plantrAutologin.setPrefIdentifier(getApplicationContext(), imageB64);
                db.child("Users").child(userID).child("profPhoto").push().setValue(imageB64);
                db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (Objects.equals(child.child("username").getValue(), userID)) {
                                Log.w("Gotcha!", child.getKey());
                                String userKey = child.getKey();
                                db.child("Users").child(userKey).child("profPhoto").setValue(imageB64);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("onCancelled", "Cancelled");
                    }
                });
            }
        }
    }

    public void changeProfPhoto(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
    }

    public void changeFavePlant(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("What is your favorite plant?");

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);

        final EditText plantInput = new EditText(this);
        plantInput.setHint("Plant Name");
        plantInput.setInputType(InputType.TYPE_CLASS_TEXT);
        lay.addView(plantInput);

        dialog.setView(lay);

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newFavePlant = plantInput.getText().toString();
                plantrAutologin.setFavePlant(getApplicationContext(), newFavePlant);
                Toast.makeText(SettingsActivity.this, "Information successfully updated!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void changeName(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Change Account Name");

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setHint("First Name");
        firstNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        lay.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setHint("Last Name");
        lastNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        lay.addView(lastNameInput);

        dialog.setView(lay);

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newFirstName = firstNameInput.getText().toString();
                plantrAutologin.setFirstName(getApplicationContext(), newFirstName);

                newLastName = lastNameInput.getText().toString();
                plantrAutologin.setLastName(getApplicationContext(), newLastName);

                Toast.makeText(SettingsActivity.this, "Information successfully updated!",
                                Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void Logout (View view){
        plantrAutologin.setUsername(this, "");
        Intent homepage = new Intent(this, PlantrHomePage.class);
        startActivity(homepage);
    }

}
