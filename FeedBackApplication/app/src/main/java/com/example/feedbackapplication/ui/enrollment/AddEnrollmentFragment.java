package com.example.feedbackapplication.ui.enrollment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddEnrollmentFragment extends Fragment {
    private final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapterClassID;
    private AutoCompleteTextView classID;
    private TextInputEditText traineeID;
    private String inspirationalKey = "where_is_my_key";

    private FirebaseDatabase databaseReference;
    private ValueEventListener fetchInClass;
    private ValueEventListener fetchInTrainee;
    private ValueEventListener fetchInEnrollment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_enrollment, container, false);

        //sep tong
        databaseReference = FirebaseDatabase.getInstance();

        //views
        classID = view.findViewById(R.id.actClassID);
        traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);

        //Take data to dropdown classID
        adapterClassID = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        classID.setAdapter(adapterClassID);

        //Insert Data
        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            String trainee_ID = String.valueOf(traineeID.getText());
            int class_ID = Integer.parseInt(classID.getText().toString().trim());

            fetchInClass(trainee_ID, class_ID);
        });

        //Back
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_enrollment));

        fetchData();
        return view;
    }

    //for classID dropdown
    public void fetchData() {
        databaseReference.getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        com.example.feedbackapplication.model.Class temp = dataSnapshot.getValue(com.example.feedbackapplication.model.Class.class);
                        if (temp != null) {
                            list.add(String.valueOf(temp.getClassID()));
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

    private void fetchInClass(String trainee_ID, int class_ID) {
        fetchInClass = databaseReference.getReference("Class")
                .orderByChild("classID").equalTo(class_ID).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag = true;
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot != null) {
                                    //exist class_ID
                                    flag = false;
                                    killFetchInClass();
                                    fetchInTrainee(trainee_ID, class_ID);
                                }
                            }
                        }
                        if (flag) {
                            //class_ID not exists
                            killFetchInClass();
                            addFailDialog(String.valueOf(class_ID), "not exists");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void fetchInTrainee(String trainee_ID, int class_ID) {
        //query id cua new_class_Name
        fetchInTrainee = databaseReference.getReference("Trainee")
                .orderByChild("UserName").equalTo(trainee_ID).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag = true;
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot != null) {
                                    //exist trainee_ID
                                    flag = false;
                                    killFetchInTrainee();
                                    fetchInEnrollment(trainee_ID, class_ID);
                                }
                            }
                        }
                        if (flag) {
                            killFetchInTrainee();
                            addFailDialog(trainee_ID, "not exists");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void fetchInEnrollment(String trainee_ID, int class_ID) {
        fetchInEnrollment = databaseReference.getReference("Enrollment")
                .orderByChild("classID").equalTo(class_ID).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag = true;
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.exists()) { //toi day dc r, tip cho vui
                                    //exist class_ID
                                    Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                    if (temp != null) {
                                        if (temp.getTraineeID().equals(trainee_ID)) {
                                            //already exits in Enrollment
                                            flag = false;
                                            killFetchInEnrollment();
                                            alreadyExistDialog(trainee_ID + " in " + class_ID, "already exists");
                                        }
                                    }
                                }
                            }
                        }
                        if (flag) {
                            //exists in Class, exits in Trainee, no exist in Enrollment
                            killFetchInEnrollment();
                            String trainee_ID = Objects.requireNonNull(traineeID.getText()).toString().trim();
                            int class_ID = Integer.parseInt(classID.getText().toString().trim());
                            Enrollment enrollment = new Enrollment(class_ID, trainee_ID);
                            inspirationalKey = FirebaseDatabase.getInstance().getReference("Enrollment").push().getKey();
                            Map<String, Object> map = new HashMap<>();
                            map.put("status", 0);
                            map.put("classID", enrollment.getClassID());
                            map.put("traineeID", enrollment.getTraineeID());
                            FirebaseDatabase.getInstance().getReference("Enrollment")
                                    .child(inspirationalKey)
                                    .setValue(map, (error, ref) -> {
                                        if (error != null) {
                                            //System.out.println("Data could not be saved. " + error.getMessage());
                                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                        } else {
                                            //System.out.println("Data saved successfully.");
                                            Toast.makeText(getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                                            addSuccessDialog("Trainee", "added to class");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void alreadyExistDialog(String s1, String s2) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_red_2_red);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        if (!String.valueOf(s1).isEmpty())
            text1.setText(String.valueOf(s1));
        TextView text2 = dialog.findViewById(R.id.text2);
        if (!String.valueOf(s2).isEmpty())
            text2.setText(String.valueOf(s2));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); //already exists thi o lai
        });
        dialog.show();
    }

    private void addSuccessDialog(String s1, String s2) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_green_2_blue);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        if (!String.valueOf(s1).isEmpty())
            text1.setText(String.valueOf(s1));
        TextView text2 = dialog.findViewById(R.id.text2);
        if (!String.valueOf(s2).isEmpty())
            text2.setText(String.valueOf(s2));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_add_to_nav_enrollment);
        });
        dialog.show();
    }

    private void addFailDialog(String s1, String s2) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_red_2_red);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        text1.setText(String.valueOf(s1));
        TextView text2 = dialog.findViewById(R.id.text2);
        text2.setText(String.valueOf(s2));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); //fail thi o lai
        });
        dialog.show();
    }

    private void killFetchInClass() {
        FirebaseDatabase.getInstance().getReference("Class").removeEventListener(fetchInClass);
    }

    private void killFetchInTrainee() {
        FirebaseDatabase.getInstance().getReference("Trainee").removeEventListener(fetchInTrainee);
    }

    private void killFetchInEnrollment() {
        FirebaseDatabase.getInstance().getReference("Enrollment").removeEventListener(fetchInEnrollment);
    }
}