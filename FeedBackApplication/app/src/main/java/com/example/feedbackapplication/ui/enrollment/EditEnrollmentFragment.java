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
import androidx.fragment.app.Fragment;

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

    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView className;
    private TextInputEditText traineeID;
    private TextInputEditText traineeName;
    private int moduleID = 0;
    private Enrollment enrollment;

    private String trainee_ID;
    private String traineed_Name;
    private int class_ID;
    private String class_Name;
    private String key_gi_do;
    private int newClassId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_enrollment, container, false);

        //reference = FirebaseDatabase.getInstance().getReference().child("Enrollment");

        traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);
        traineeName = view.findViewById(R.id.txt_ip_edt_TraineeName);
        className = view.findViewById(R.id.actClassName);

        trainee_ID = getArguments().getString("TraineeID");
        class_ID = getArguments().getInt("ClassID");
        //traineed_Name = fetchTraineeName(trainee_ID);
        traineed_Name = trainee_ID;
        //class_Name = fetchClassName(class_ID);
        class_Name = String.valueOf(class_ID);


        traineeID.setText(trainee_ID);
        traineeName.setText(traineed_Name);
        className.setText(class_Name);

        //Take data to dropdown adminID
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.option_item,list);
        className.setAdapter(adapter);
        fetchClassNameList();
        //newClassId =FireBaseHelper.fetchClassID(className.getText().toString().trim());
        newClassId =7;
        //key_gi_do = FireBaseHelper.fetchEnrollmentKey(class_ID,trainee_ID);
        key_gi_do = "2";

        //update
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            //String key = fetchEnrollmentKey(class_ID,trainee_ID);
            String key = key_gi_do;
            Map<String,Object> map = new HashMap<>();
            //int newClassId = fetchClassID(className.getText().toString().trim());
            map.put("ClassID",newClassId);
            map.put("TraineeID",trainee_ID);

            FirebaseDatabase.getInstance().getReference()
                    .child("Enrollment")
                    .child(key)
                    .updateChildren(map);
            Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
        });

        //back
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
        });
        return view;
    }


    public void fetchClassNameList(){
        listener = FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String className = dataSnapshot.child("ClassName").getValue(String.class);
                    list.add(className);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}