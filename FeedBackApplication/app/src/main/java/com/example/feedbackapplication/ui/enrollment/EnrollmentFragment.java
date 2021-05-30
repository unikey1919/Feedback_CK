package com.example.feedbackapplication.ui.enrollment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EnrollmentFragment extends Fragment implements EnrollmentAdapter.ClickListener, AdapterView.OnItemSelectedListener {
    private final ArrayList<String> list = new ArrayList<>(); //for ClassName List
    private EnrollmentAdapter adapter; //for Enrollment List
    private ArrayAdapter<String> adapterClassName; //for ClassName List
    private AutoCompleteTextView actClassName; //for ClassName Filter
    //for item info
    private String currentClassName = "All";
    private String TraineeName = "-1";
    private String ClassName = "-1";
    private String EnrollmentKey = "-1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String test_result = FireBaseHelper.fetchTraineeName_v2("traineeA");
        //Toast.makeText(getActivity(), test_result, Toast.LENGTH_LONG).show();
        View root = inflater.inflate(R.layout.enrollment_fragment, container, false);

        //getting views
        FloatingActionButton btnInsert = root.findViewById(R.id.btnNew);
        RecyclerView rcvEnrollment = root.findViewById(R.id.rcvEnrollment);
        rcvEnrollment.setHasFixedSize(true);
        rcvEnrollment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Take data to Spinner dropdown classID
        actClassName = root.findViewById(R.id.actClassName);
        //ArrayAdapter<CharSequence> adapterClassName = ArrayAdapter.createFromResource(getContext(),R.array.test, android.R.layout.simple_spinner_item);
        //adapterClassName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterClassName = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        actClassName.setAdapter(adapterClassName);
        actClassName.setOnItemSelectedListener(this);

        //Retrieve data
        FirebaseRecyclerOptions<Enrollment> options =
                new FirebaseRecyclerOptions.Builder<Enrollment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Enrollment"), Enrollment.class)
                        .build();
        adapter = new EnrollmentAdapter(options, this);
        rcvEnrollment.setAdapter(adapter);

        //add
        btnInsert.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_enrollment_to_nav_add));

        fetchData();
        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentClassName = parent.getItemAtPosition(position).toString();
        //spClassName.setAdapter(adapterClassName);
        Toast.makeText(getActivity(), currentClassName, Toast.LENGTH_LONG).show(); //ko lay dc currentClassName o day
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        currentClassName = parent.getItemAtPosition(0).toString();
        Toast.makeText(getActivity(), currentClassName, Toast.LENGTH_LONG).show(); //ko lay dc currentClassName o day
    }

    public void fetchData() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add("All");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String temp = dataSnapshot.child("className").getValue(String.class);
                    if (temp != null)
                        list.add(temp);
                }
                //adapter.notifyDataSetChanged();
                adapterClassName.notifyDataSetChanged();
                Toast.makeText(getActivity(), "currentClassName", Toast.LENGTH_LONG).show(); // 1 lan khi frag create
                actClassName.setText(adapterClassName.getItem(0), false);
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
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_enrollment_to_nav_view, bundle);
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
                                        if (EnrollmentKey != null && !TraineeName.equals("-1") && !ClassName.equals("-1")) {
                                            bundle.putString("TraineeID", enrollment.getTraineeID());
                                            bundle.putInt("ClassID", enrollment.getClassID());
                                            bundle.putString("TraineeName", TraineeName);
                                            bundle.putString("ClassName", ClassName);
                                            bundle.putString("EnrollmentKey", EnrollmentKey);
                                            Navigation.findNavController(requireView()).navigate(R.id.action_nav_enrollment_to_nav_edit, bundle);
                                        }
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
                deleteSuccessDialog();
            }
        });
        dialog.show();
    }

    private void deleteSuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.delete_success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOK = dialog.findViewById(R.id.btnDeleteSuccess);
        btnOK.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}