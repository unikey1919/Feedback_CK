package com.example.feedbackapplication.ui.feedback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.feedbackapplication.MainActivity;
import  com.example.feedbackapplication.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class ShowFeedBack_trainee extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("delllll", "COMPLETEEEEEEEEEEEe");
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("delllll", "COMPLETEEEEEEEEEEEe");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
        Log.d("delllll", "COMPLETEEEEEEEEEEEe");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("delllll", "COMPLETEEEEEEEEEEEe");
    }
}
