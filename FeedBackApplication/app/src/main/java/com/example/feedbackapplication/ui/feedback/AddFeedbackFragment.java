package com.example.feedbackapplication.ui.feedback;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.FeedBack;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFeedbackFragment extends Fragment {

    private Button btnBack,btnReview;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView txtType;
    private TextInputEditText title, feedBackId;
    private FeedBack feedBack;
    private int maxID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_feedback, container, false);

        btnReview = root.findViewById(R.id.btnReview);
        btnBack = root.findViewById(R.id.btnBack);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_feedback_to_nav_review_feedback);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_add_feedback_to_nav_feedback);
            }
        });
        return root;
    }

}