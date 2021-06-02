package com.example.feedbackapplication.ui.feedback;

import android.app.Dialog;
import android.os.Bundle;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailFeedbackFragment extends Fragment {

    private TextView txtTitle, txtAdminId;
    private Button btnEdit, btnBack;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private FeedBack feedBack;
    private int feedbackId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_feedback, container, false);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtAdminId = view.findViewById(R.id.txtAdminId);

        txtTitle.setText(getArguments().getString("title"));
        txtAdminId.setText(getArguments().getString("adminID"));
        feedbackId = getArguments().getInt("id");

        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_detail_feedback_to_nav_edit_feedback);
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_detail_feedback_to_nav_feedback);
            }
        });
        return view;
    }
}