package com.example.feedbackapplication.ui.enrollment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewEnrollmentFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_enrollment, container, false);
        //database = FirebaseDatabase.getInstance().getReference().child("Module");

        //back
        FloatingActionButton btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_module));

        return root;
    }
}