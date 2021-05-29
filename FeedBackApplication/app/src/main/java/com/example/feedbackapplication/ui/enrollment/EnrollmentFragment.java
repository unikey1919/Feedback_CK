package com.example.feedbackapplication.ui.enrollment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.EnrollmentAdapter;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class EnrollmentFragment extends Fragment implements EnrollmentAdapter.ClickListener {

    private EnrollmentAdapter adapter;
    private EnrollmentViewModel myViewModel;
    private RecyclerView rcvEnrollment;
    private DatabaseReference database;
    private ArrayList<Enrollment> arrayList;
    private FloatingActionButton btnInsert;
    private FirebaseRecyclerOptions<Enrollment> options;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.enrollment_fragment, container, false);

        database = FirebaseDatabase.getInstance().getReference().child("Enroll");
        rcvEnrollment = root.findViewById(R.id.rcvEnrollment);
        rcvEnrollment.setHasFixedSize(true);
        rcvEnrollment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Retrieve data
        FirebaseRecyclerOptions<Enrollment> options =
                new FirebaseRecyclerOptions.Builder<Enrollment>()
                        .setQuery(database, Enrollment.class)
                        .build();
        adapter = new EnrollmentAdapter(options, this);
        rcvEnrollment.setAdapter(adapter);

        //Save data
        btnInsert = root.findViewById(R.id.btnNew);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_enrollment_to_nav_add);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void viewClicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();
        bundle.putString("traineeID", enrollment.getTrainee());
        bundle.putInt("classID", enrollment.getClassId());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_enrollment_to_nav_view, bundle);
    }

    @Override
    public void updateClicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();
        bundle.putString("traineeID", enrollment.getTrainee());
        bundle.putInt("classID", enrollment.getClassId());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_enrollment_to_nav_edit, bundle);
    }

    @Override
    public void deleteClicked(Enrollment enrollment) {
        //String key = String.valueOf(enrollment.getClassID()); ///////////// tạm
        String key = fetchEnrollmentKey(enrollment.getClassId(),enrollment.getTrainee());
        FirebaseDatabase.getInstance().getReference()
                .child("Enroll")
                .child(key)
                .setValue(null)
                .addOnCompleteListener(task -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show());

    }

    public String getEnrollmentKey() {
        return "1";
    }
    public String fetchEnrollmentKey(int class_ID, String trainee_ID){
        String enrollment_key = database.child("Enroll")
                .orderByChild("classId").equalTo(String.valueOf(class_ID))
                .orderByChild("trainee").equalTo(trainee_ID).toString();
        return enrollment_key;
    }
}