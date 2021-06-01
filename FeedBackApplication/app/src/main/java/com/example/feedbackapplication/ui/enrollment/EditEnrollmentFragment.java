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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditEnrollmentFragment extends Fragment {

    private ArrayList<String> list;
    private ArrayAdapter<String> adapterClassName;
    private AutoCompleteTextView className;
    //values from bundle
    private String trainee_ID = "0";
    private String trainee_Name = "0";
    private String class_Name = "0";
    private String enrollment_key = "0";
    //new classID
    private int newClassId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_enrollment, container, false);
        //views
        TextInputEditText traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);
        TextInputEditText traineeName = view.findViewById(R.id.txt_ip_edt_TraineeName);
        className = view.findViewById(R.id.actClassName);

        //get from bundle
        if (getArguments() != null) {
            trainee_ID = getArguments().getString("TraineeID");
            trainee_Name = getArguments().getString("TraineeName");
            class_Name = getArguments().getString("ClassName");
            enrollment_key = getArguments().getString("EnrollmentKey"); //enough to get both trainee and class
        }

        //set texts
        traineeID.setText(trainee_ID);
        traineeName.setText(trainee_Name);
        className.setText(class_Name);

        //Take data to dropdown adminID
        list = new ArrayList<>();
        adapterClassName = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        className.setAdapter(adapterClassName);

        //update
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> btnSaveEXE());

        //back
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_edit_to_nav_enrollment));
        fetchClassNameList(); //
        return view;
    }

    public void fetchClassNameList() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String temp = dataSnapshot.child("className").getValue(String.class);
                    if (temp != null)
                        list.add(temp);
                }
                adapterClassName.notifyDataSetChanged();
                Toast.makeText(getActivity(), "ahoy", Toast.LENGTH_LONG).show(); // 1 lan khi frag create
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void btnSaveEXE() {
        String new_class_Name = className.getText().toString().trim();
        //query id cua new_class_Name
        FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("className").equalTo(new_class_Name).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot != null) {
                                    newClassId = Integer.parseInt(String.valueOf(dataSnapshot.child("classID").getValue(Integer.class)));
                                    Map<String, Object> map = new HashMap<>();
                                    if (new_class_Name.equals(class_Name)) {
                                        alreadyExistDialog("Trainee already","exists in the class");
                                    } else {
                                        map.put("classID", newClassId);
                                        map.put("traineeID", trainee_ID);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Enrollment")
                                                .child(enrollment_key)
                                                .updateChildren(map);
                                        Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                                        updateSuccessDialog("Update success!");
                                    }
                                }
                            }
                        } else {
                            updateFailDialog("Update failed!");
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
        if(!String.valueOf(s1).isEmpty())
            text1.setText(String.valueOf(s1));
        TextView text2 = dialog.findViewById(R.id.text2);
        if(!String.valueOf(s2).isEmpty())
            text2.setText(String.valueOf(s2));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); //already exists thi o lai edit ti3p
        });
        dialog.show();
    }

    private void updateSuccessDialog(String s1) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_green_1_blue);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        if(!String.valueOf(s1).isEmpty())
            text1.setText(String.valueOf(s1));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_edit_to_nav_enrollment);
        });
        dialog.show();
    }

    private void updateFailDialog(String s1) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_red_1_red);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        if(!String.valueOf(s1).isEmpty())
            text1.setText(String.valueOf(s1));
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); // o lai edit ti3p
        });
        dialog.show();
    }
}