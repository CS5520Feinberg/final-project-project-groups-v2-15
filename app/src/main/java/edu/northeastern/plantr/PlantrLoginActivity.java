package edu.northeastern.plantr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PlantrLoginActivity extends AppCompatActivity {

    DatabaseReference db;

    String loginUsername;

    String loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void newUserButton(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlantrLoginActivity.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_create_new_user, null);
        builder.setView(v);
        EditText txt_usernameInput = v.findViewById(R.id.usernameEnter);
        EditText txt_firstNameInput = v.findViewById(R.id.firstNameEnter);
        EditText txt_lastNameInput = v.findViewById(R.id.lastNameEnter);
        EditText txt_passwordInput = v.findViewById(R.id.passwordEnterNewUser);
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        builder.setPositiveButton("Create New User", (DialogInterface.OnClickListener) (dialog, id) -> {
            String userName = txt_usernameInput.getText().toString();
            String firstName = txt_firstNameInput.getText().toString();
            String lastName = txt_lastNameInput.getText().toString();
            String password = txt_passwordInput.getText().toString();
            String favePlant = "";          // fave plant default is blank for a new user
            String lastActivity = "Created Profile!";
            Uri profilePhoto = null;
            if(userName.length() == 0 || firstName.length() == 0 || lastName.length() == 0 ||
                password.length() == 0){
                Toast.makeText(PlantrLoginActivity.this, "Please Enter Info", Toast.LENGTH_SHORT)
                        .show();
            }else if(userName.contains(".") || userName.contains("#") || userName.contains("$") || userName.contains("[") || userName.contains("]")){
                Toast.makeText(getApplicationContext(),
                        "Invalid Character in Username. Please do not include any of '.', '#', '$', '[', or ']' and try again!",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                User newUser = new User(userName, firstName, lastName, favePlant, password, lastActivity, profilePhoto);
                db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean alreadyHere = false;
                        for (DataSnapshot child:snapshot.getChildren()) {
                            //Log.w("Firebase", (String) child.child("username").getValue());
                            //Log.w("Username", userName);
                            if(Objects.equals(child.child("username").getValue(), userName)){
                                alreadyHere = true;
                                //Log.w("Gotcha!", String.valueOf(alreadyHere) + userName + child.child("username").getValue());
                                break;
                            }
                        }
                        Log.w("Checking alreadyHere", "---" + alreadyHere + "---");
                        if(!alreadyHere) {
                            //Log.w("Trying to push?", newUser.toString());
                            db.child("Users").push().setValue(newUser);
                            plantrAutologin.setUsername(getApplicationContext(), txt_usernameInput.getText().toString());
                            plantrAutologin.setFirstName(getApplicationContext(), txt_firstNameInput.getText().toString());
                            plantrAutologin.setLastName(getApplicationContext(), txt_lastNameInput.getText().toString());
                            plantrAutologin.setFavePlant(getApplicationContext(), "");
                            plantrAutologin.setLastActivity(getApplicationContext(), "Created Profile!");
                            Intent goHome = new Intent(getApplicationContext(), PlantrHomePage.class);
                            startActivity(goHome);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("onCancelled", "Cancelled");
                    }
                });
            }
        });
        builder.show();
    }

    public void loginButton(View view){
        //Go to Firebase, check u/p vars
        loginUsername = ((EditText)findViewById(R.id.usernameETBox)).getText().toString();
        loginPassword = ((EditText)findViewById(R.id.passwordETBox)).getText().toString();
        //Log.w("Checking", "---" + loginUsername + "---" + loginPassword);
        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()) {
                    if(Objects.equals(child.child("username").getValue(), loginUsername)){
                        if(Objects.equals(child.child("password").getValue(), loginPassword)){
                            Log.w("Found a match!", "---" + loginUsername + "---" + child.child("username") + "---" + loginPassword + "---" + child.child("password"));
                            plantrAutologin.setUsername(getApplicationContext(), loginUsername);
                            plantrAutologin.setFirstName(getApplicationContext(), String.valueOf(child.child("firstName").getValue()));
                            plantrAutologin.setLastName(getApplicationContext(), String.valueOf(child.child("lastName").getValue()));
                            plantrAutologin.setFavePlant(getApplicationContext(), String.valueOf(child.child("favePlant").getValue()));
                            plantrAutologin.setLastActivity(getApplicationContext(), String.valueOf(child.child("lastActivity").getValue()));
                            Intent goHome = new Intent(getApplicationContext(), PlantrHomePage.class);
                            startActivity(goHome);
                        }
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