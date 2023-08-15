package edu.northeastern.plantr;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
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

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private String newFavePlant;
    private String newFirstName;
    private String newLastName;

    private Uri profPicURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Navbar setup
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.settingsNav);
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
