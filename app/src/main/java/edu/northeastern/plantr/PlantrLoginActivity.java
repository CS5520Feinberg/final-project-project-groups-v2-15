package edu.northeastern.plantr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlantrLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void createNewUser(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlantrLoginActivity.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_create_new_user, null);
        builder.setView(v);
        EditText txt_usernameInput = v.findViewById(R.id.usernameEnter);
        EditText txt_firstNameInput = v.findViewById(R.id.firstNameEnter);
        EditText txt_lastNameInput = v.findViewById(R.id.lastNameEnter);
        EditText txt_passwordInput = v.findViewById(R.id.passwordEnterNewUser);
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        builder.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) (dialog, id) -> {
            String userName = txt_usernameInput.getText().toString();
            String firstName = txt_firstNameInput.getText().toString();
            String lastName = txt_lastNameInput.getText().toString();
            String password = txt_passwordInput.getText().toString();
            String lastActivity = "Created Profile!";
            Uri profilePhoto = null;
            if(userName.length() == 0 || firstName.length() == 0 || lastName.length() == 0 ||
                password.length() == 0){
                Toast.makeText(PlantrLoginActivity.this, "Please Enter Info", Toast.LENGTH_SHORT)
                        .show();
            }else{
                User newUser = new User(userName, firstName, lastName, password, lastActivity, profilePhoto);
            }
        });
        builder.show();
    }
}