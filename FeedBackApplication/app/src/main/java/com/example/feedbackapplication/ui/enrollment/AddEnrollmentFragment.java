package com.example.feedbackapplication.ui.enrollment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddEnrollmentFragment extends Fragment {
    private final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapterClassID;
    private AutoCompleteTextView classID;
    private TextInputEditText traineeID;
    private String inspirationalKey = "where_is_my_key";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_enrollment, container, false);

        //views
        classID = view.findViewById(R.id.actClassID);
        traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);

        //Take data to dropdown classID
        adapterClassID = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        classID.setAdapter(adapterClassID);

        //Insert Data
        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            String trainee_ID = Objects.requireNonNull(traineeID.getText()).toString().trim();
            int class_ID = Integer.parseInt(classID.getText().toString().trim());
            Enrollment enrollment = new Enrollment(class_ID, trainee_ID);
            inspirationalKey = FirebaseDatabase.getInstance().getReference("Enrollment").push().getKey();
            FirebaseDatabase.getInstance().getReference("Enrollment").child(inspirationalKey).setValue(enrollment);
            Toast.makeText(v.getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
        });

        //Back
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_enrollment));

        fetchData();
        return view;
    }

    public void fetchData() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                        String temp;
                        if (enrollment != null) {
                            temp = String.valueOf(enrollment.getClassID());
                            list.add(temp);
                        }
                    }
                }
                adapterClassID.notifyDataSetChanged();
                classID.setText(adapterClassID.getItem(0), false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}