package com.example.feedbackapplication.ui.feedback;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.FeedBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewEditFragment extends Fragment {

    private TextView txtTitle;
    private Button btnSave, btnBack;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private FeedBack feedBack;
    private int feedbackId, adminID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_edit, container, false);

        txtTitle = view.findViewById(R.id.txtTitle);
//        txtTitle.setText(getArguments().getString("titles"));
//        adminID = getArguments().getInt("adminID");
//        feedbackId = getArguments().getInt("id");

        //Insert Data
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(feedbackId);
                Map<String,Object> map = new HashMap<>();
                map.put("title",txtTitle.getText().toString().trim());
                //map.put("adminID",adminID.getText().toString().trim());

                FirebaseDatabase.getInstance().getReference()
                        .child("Feedback")
                        .child(key)
                        .updateChildren(map);
                Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                SuccessDialog(Gravity.CENTER);
                Navigation.findNavController(v).navigate(R.id.action_nav_review_edit_to_nav_feedback);
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_review_edit_to_nav_edit_feedback);
            }
        });
        return view;
    }

    private void SuccessDialog(int gravity){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_success);
        TextView textView = dialog.findViewById(R.id.textViewSuccess);
        textView.setText("Update success!");

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        Button btnAddSuccess = dialog.findViewById(R.id.btnAddSuccess);
        btnAddSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}