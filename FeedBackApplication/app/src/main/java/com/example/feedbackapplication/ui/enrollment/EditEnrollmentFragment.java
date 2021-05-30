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

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditEnrollmentFragment extends Fragment {

    private Button btnBack, btnSave;
    private DatabaseReference database, reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapterClassName;
    private AutoCompleteTextView className;
    private TextInputEditText traineeID;
    private TextInputEditText traineeName;
    private int moduleID = 0;
    private Enrollment enrollment;

    private String trainee_ID;
    private String trainee_Name;
    private int class_ID;
    private String class_Name;
    private String enrollment_key;
    private String key_gi_do;
    private int newClassId=-1;
    private int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_enrollment, container, false);
        //views
        traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);
        traineeName = view.findViewById(R.id.txt_ip_edt_TraineeName);
        className = view.findViewById(R.id.actClassName);

        //get from bundle
        trainee_ID = getArguments().getString("TraineeID");
        class_ID = getArguments().getInt("ClassID");
        trainee_Name = getArguments().getString("TraineeName");
        class_Name = getArguments().getString("ClassName");
        enrollment_key = getArguments().getString("EnrollmentKey");

        //set texts
        traineeID.setText(trainee_ID);
        traineeName.setText(trainee_Name);
        className.setText(class_Name);

        //Take data to dropdown adminID
        list = new ArrayList<String>();
        adapterClassName = new ArrayAdapter<>(getActivity(), R.layout.option_item, list);
        className.setAdapter(adapterClassName);

        //update
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            btnSaveEXE();
        });

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
                Toast.makeText(getActivity(), "ahoy", Toast.LENGTH_LONG).show(); //chi chay 1 lan khi frag create
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void btnSaveEXE(){
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
                            if(new_class_Name.equals(class_Name)){
                                alreadyExistDialog();
                            }else{
                                map.put("classID", newClassId);
                                map.put("traineeID", trainee_ID);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Enrollment")
                                        .child(enrollment_key)
                                        .updateChildren(map);
                                Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                                updateSuccessDialog();
                            }
                        }
                    }
                }
                else {
                    updateFailDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void alreadyExistDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.trainee_already_exists_in_the_class_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOK = dialog.findViewById(R.id.btnUpdateFail);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); //already exists thi o lai edit ti3p
        });
        dialog.show();
    }

    private void updateSuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOK = dialog.findViewById(R.id.btnUpdateSuccess);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(v).navigate(R.id.action_nav_edit_to_nav_enrollment);
        });
        dialog.show();
    }

    private void updateFailDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_fail_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOK = dialog.findViewById(R.id.btnUpdateFail);
        btnOK.setOnClickListener(v -> {
            dialog.dismiss(); //already exists thi o lai edit ti3p
        });
        dialog.show();
    }
}