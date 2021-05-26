package com.example.feedbackapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btnLogin,btnYes;
    private Spinner spRole;
    private static String role;
    private TextInputLayout textInputUsername, textInputPassword;
    private static String nameFromDB,usernameFromDB,emailFromDB;

    public LoginActivity loginActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Spinner with role
        spRole =findViewById(R.id.spRole);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.test, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Init
        textInputUsername = findViewById(R.id.txt_ip_username);
        textInputPassword = findViewById(R.id.txt_ip_password);

        spRole.setAdapter(arrayAdapter);
        spRole.setOnItemSelectedListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() | !validatePassword()){
                    return;
                }else {
                    isUser(role);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       role = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void isUser(String ref){
        String userEnteredUsername = textInputUsername.getEditText().getText().toString().trim();
        String userEnteredPassword = textInputPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);

        Query checkUser =reference.orderByChild("UserName").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("Password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){
                        nameFromDB = snapshot.child(userEnteredUsername).child("Name").getValue(String.class);
                        emailFromDB = snapshot.child(userEnteredUsername).child("Email").getValue(String.class);
                        usernameFromDB = snapshot.child(userEnteredUsername).child("UserName").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        if(role.equals("Admin")){
                            intent.putExtra("role","admin");
                        }
                        if(role.equals("Trainer")){
                            intent.putExtra("role","trainer");
                        }
                        if(role.equals("Trainee")){
                            intent.putExtra("role","trainee");
                        }
                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("username",usernameFromDB);
                        startActivity(intent);
                    } else {
                        failDialog();
                    }
                } else {
                    failDialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void failDialog(){
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.login_fail_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loginfail_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean validateUsername(){
        String username = textInputUsername.getEditText().getText().toString().trim();
        String usernamePattern = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$";
        String noWhiteSpace = "\\A\\w{1,20}\\z";

        if(username.isEmpty()){
            textInputUsername.setError("Username mas have at least 1 character!");
            return false;
        }else if(!username.matches(usernamePattern)){
            textInputUsername.setError("Username only character");
            return false;
        } else if(!username.matches(noWhiteSpace)){
            textInputUsername.setError("Username must have a blank space!");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String password = textInputPassword.getEditText().getText().toString().trim();
        String passwordPattern = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$";
        String noWhiteSpace = "\\A\\w{1,20}\\z";
        if(password.isEmpty()){
            textInputPassword.setError("Password mas have at least 1 character?");
            return false;
        }else if(!password.matches(passwordPattern)){
            textInputPassword.setError("Username only character");
            return false;
        } else if(!password.matches(noWhiteSpace)){
            textInputPassword.setError("Password must have a blank space!");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public static final Pattern USERNAME = Pattern.compile("[a-z]");
}