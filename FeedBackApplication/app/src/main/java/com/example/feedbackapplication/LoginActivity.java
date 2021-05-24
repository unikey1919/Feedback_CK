package com.example.feedbackapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btnLogin;
    private Spinner spRole;
    private static String role;
    private TextInputLayout textInputUsername, textInputPassword;

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
                    Intent intent = new Intent(v.getContext(),MainActivity.class);
                    if(role.equals("Admin")){
                        intent.putExtra("role","admin");
                    }
                    if(role.equals("Trainer")){
                        intent.putExtra("role","trainer");
                    }
                    if(role.equals("Trainee")){
                        intent.putExtra("role","trainee");
                    }
                    startActivity(intent);
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