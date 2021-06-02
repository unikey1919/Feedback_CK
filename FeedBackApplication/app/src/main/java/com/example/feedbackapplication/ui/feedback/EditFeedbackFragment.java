package com.example.feedbackapplication.ui.feedback;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.FeedBack;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditFeedbackFragment extends Fragment {

    private Button btnBack,btnReview;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView edtType;
    private TextInputEditText edtTitle;
    private FeedBack feedback;
    private int feedbackId, adminId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_feedback, container, false);

        edtTitle = view.findViewById(R.id.edtTitle);
        edtType = view.findViewById(R.id.edtType);

        //Take data to dropdown edtType
        String[] types = getResources().getStringArray(R.array.types);
        adapter = new ArrayAdapter<>(getActivity(),R.layout.item_type,types);
        edtType.setAdapter(adapter);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_feedback_to_nav_feedback);
            }
        });

        //update
        btnReview = view.findViewById(R.id.btnReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_feedback_to_nav_review_edit);
            }
        });
        return view;
    }
}
