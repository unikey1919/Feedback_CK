package com.example.feedbackapplication.ui.enrollment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.EnrollmentAdapter;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Enrollment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EnrollmentFragment extends Fragment implements EnrollmentAdapter.ClickListener {
    List<Enrollment> enrollmentList;
    private EnrollmentAdapter adapter;
    private ArrayAdapter<String> adapterClassName;
    private ValueEventListener listener;
    private EnrollmentViewModel myViewModel;
    private RecyclerView rcvEnrollment;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, databaseRefClass;
    private ArrayList<Enrollment> arrayList;
    private FloatingActionButton btnInsert;
    private ArrayList<String> list;
    private AutoCompleteTextView className;

    private String TraineeName = "-1";
    private String ClassName = "-1";
    private String EnrollmentKey = "-1";
    private FirebaseRecyclerOptions<Enrollment> options;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //String test_result = FireBaseHelper.fetchTraineeName_v2("traineeA");
        //Toast.makeText(getActivity(), test_result, Toast.LENGTH_LONG).show();

        View root = inflater.inflate(R.layout.enrollment_fragment, container, false);

        databaseRefClass = FirebaseDatabase.getInstance().getReference("Class");
        databaseRef = FirebaseDatabase.getInstance().getReference("Enrollment");
        //getting views
        rcvEnrollment = root.findViewById(R.id.rcvEnrollment);
        btnInsert = root.findViewById(R.id.btnNew);
        rcvEnrollment.setHasFixedSize(true);
        rcvEnrollment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Take data to dropdown classID
        className = root.findViewById(R.id.actClassName);
        list = new ArrayList<String>();
        adapterClassName = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        className.setAdapter(adapterClassName);

        //Retrieve data
        FirebaseRecyclerOptions<Enrollment> options =
                new FirebaseRecyclerOptions.Builder<Enrollment>()
                        .setQuery(databaseRef, Enrollment.class)
                        .build();
        adapter = new EnrollmentAdapter(options, this);
        rcvEnrollment.setAdapter(adapter);

        //add
        btnInsert.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_enrollment_to_nav_add));

        fetchData();
        return root;
    }

    public void fetchData() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String temp = dataSnapshot.child("className").getValue(String.class);
                    if (temp != null)
                        list.add(temp);
                }
                //adapter.notifyDataSetChanged();
                adapterClassName.notifyDataSetChanged();
                className.setText(adapterClassName.getItem(0), false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        bundle.putString("TraineeID", enrollment.getTraineeID());
        bundle.putInt("ClassID", enrollment.getClassID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_enrollment_to_nav_view, bundle);
    }

    @Override
    public void updateClicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();

        FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("classID").equalTo(enrollment.getClassID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Class temp = dataSnapshot.getValue(Class.class);
                                if (temp != null) {
                                    ClassName = temp.getClassName();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("Trainee")
                .orderByChild("UserName").equalTo(enrollment.getTraineeID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                //Trainee temp = dataSnapshot.getValue(Trainee.class); //Trainee ko lay dc
                                String temp = dataSnapshot.child("Name").getValue(String.class);
                                if (temp != null) {
                                    TraineeName = temp;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("Enrollment")
                .orderByChild("classID").equalTo(enrollment.getClassID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                if (temp != null) {
                                    if (temp.getTraineeID().equals(enrollment.getTraineeID())) {
                                        EnrollmentKey = dataSnapshot.getKey();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        if (!TraineeName.equals("-1") && !ClassName.equals("-1") && !EnrollmentKey.equals("-1")) {
            bundle.putString("TraineeID", enrollment.getTraineeID());
            bundle.putInt("ClassID", enrollment.getClassID());
            bundle.putString("TraineeName", TraineeName);
            bundle.putString("ClassName", ClassName);
            bundle.putString("EnrollmentKey", EnrollmentKey);
            Navigation.findNavController(getView()).navigate(R.id.action_nav_enrollment_to_nav_edit, bundle);
        }
    }

    @Override
    public void deleteClicked(Enrollment enrollment) {
        FirebaseDatabase.getInstance().getReference("Enrollment")
                .orderByChild("classID").equalTo(enrollment.getClassID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                //String temp = dataSnapshot.child("className").getValue(String.class);
                                if (temp != null) {
                                    if (temp.getTraineeID().equals(enrollment.getTraineeID())) {
                                        String key = dataSnapshot.getKey();
                                        deleteConfirmDialog(key);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void deleteConfirmDialog(String key) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.delete_class_not_over_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnYes = dialog.findViewById(R.id.btnYesAct);
        Button btnCancel = dialog.findViewById(R.id.btnCancelAct);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            if (key != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Enrollment")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(task -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}