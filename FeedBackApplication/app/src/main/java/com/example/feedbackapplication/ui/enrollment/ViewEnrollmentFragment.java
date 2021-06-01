package com.example.feedbackapplication.ui.enrollment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewEnrollmentFragment extends Fragment {
    private String TraineeID = "ahoy";
    private String TraineeName = "ahoy";
    private int Phone = 0;
    private String Address = "ahoy";
    private String Email = "ahoy";
    private int ClassID = 0;
    private String Start = "ahoy";
    private String ClassName = "ahoy";
    private String End = "ahoy";
    private int Capacity = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_enrollment, container, false);
        //get data from previous
        //get from bundle
        TraineeID = getArguments() != null ? getArguments().getString("TraineeID") : "ahoy plus";
        ClassID = getArguments().getInt("ClassID");

        //views
        TextView txtTraineeID = root.findViewById(R.id.txtTraineeID);
        TextView txtTraineePhone = root.findViewById(R.id.txtTraineePhone);
        TextView txtTraineeName = root.findViewById(R.id.txtTraineeName);
        TextView txtAddress = root.findViewById(R.id.txtAddress);
        TextView txtEmail = root.findViewById(R.id.txtEmail);
        TextView txtClassID = root.findViewById(R.id.txtClassID);
        TextView txtStart = root.findViewById(R.id.txtStartTime);
        TextView txtClassName = root.findViewById(R.id.txtClassName);
        TextView txtEnd = root.findViewById(R.id.txtEndTime);
        TextView txtCapacity = root.findViewById(R.id.txtCapacity);
        Button btnBack = root.findViewById(R.id.btnBack);

        //set texts
        txtTraineeID.setText(String.valueOf(TraineeID));
        txtTraineePhone.setText(String.valueOf(Phone));
        txtTraineeName.setText(String.valueOf(TraineeName));
        txtAddress.setText(String.valueOf(Address));
        txtEmail.setText(String.valueOf(Email));
        txtClassID.setText(String.valueOf(ClassID));
        txtStart.setText(String.valueOf(Start));
        txtClassName.setText(String.valueOf(ClassName));
        txtEnd.setText(String.valueOf(End));
        txtCapacity.setText(String.valueOf(Capacity));

        //back
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_view_to_nav_enrollment));

        FirebaseDatabase.getInstance().getReference("Trainee")
                .orderByChild("UserName").equalTo(TraineeID).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Phone = Integer.parseInt(String.valueOf(dataSnapshot.child("Phone").getValue(Integer.class)));
                            TraineeName = dataSnapshot.child("Name").getValue(String.class);
                            Address = dataSnapshot.child("Address").getValue(String.class);
                            Email = dataSnapshot.child("Email").getValue(String.class);

                            txtTraineeID.setText(String.valueOf(TraineeID));
                            txtTraineePhone.setText(String.valueOf(Phone));
                            txtTraineeName.setText(String.valueOf(TraineeName));
                            txtAddress.setText(String.valueOf(Address));
                            txtEmail.setText(String.valueOf(Email));

                            Toast.makeText(getActivity(), "ahoy, got ya trainee", Toast.LENGTH_LONG).show(); // 1 lan khi frag create
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("classID").equalTo(ClassID).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Start = dataSnapshot.child("startDate").getValue(String.class);
                            ClassName = dataSnapshot.child("className").getValue(String.class);
                            End = dataSnapshot.child("endDate").getValue(String.class);
                            Capacity = Integer.parseInt(String.valueOf(dataSnapshot.child("capacity").getValue(Integer.class)));

                            txtClassID.setText(String.valueOf(ClassID));
                            txtStart.setText(String.valueOf(Start));
                            txtClassName.setText(String.valueOf(ClassName));
                            txtEnd.setText(String.valueOf(End));
                            txtCapacity.setText(String.valueOf(Capacity));

                            Toast.makeText(getActivity(), "ahoy, got ya class", Toast.LENGTH_LONG).show(); // 1 lan khi frag create
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        return root;
    }
}