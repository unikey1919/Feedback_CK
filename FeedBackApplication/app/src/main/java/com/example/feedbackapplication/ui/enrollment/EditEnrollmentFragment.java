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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_enrollment, container, false);

        //reference = FirebaseDatabase.getInstance().getReference().child("Enroll");
        database = FirebaseDatabase.getInstance().getReference();

        traineeID = view.findViewById(R.id.txt_ip_edt_TraineeID);
        traineeName = view.findViewById(R.id.txt_ip_edt_TraineeName);
        className = view.findViewById(R.id.actClassName);

        trainee_ID = getArguments().getString("traineeID");
        class_ID = Integer.parseInt(getArguments().getString("classID"));
        traineed_Name = fetchTraineeName(trainee_ID);
        class_Name = fetchClassName(class_ID);

        traineeID.setText(trainee_ID);
        traineeName.setText(traineed_Name);
        className.setText(class_Name);

        //Take data to dropdown adminID
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.option_item,list);
        className.setAdapter(adapter);
        fetchClassNameList();

        //update
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String key = fetchEnrollmentKey(class_ID,trainee_ID);
            Map<String,Object> map = new HashMap<>();
            int newClassId = fetchClassID(className.getText().toString().trim());
            map.put("classId",newClassId);
            map.put("trainee",trainee_ID);

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

    public String fetchTraineeName(String trainee_ID){
        String trainee_key = database.child("Trainee").orderByChild("UserName").equalTo(trainee_ID).toString();
        String trainee_name = database.child("Trainee").child(trainee_key).child("Name").toString();
        return trainee_name;
    }

    public String fetchClassName(int class_ID){
        String class_key = database.child("Class").orderByChild("ClassID").equalTo(class_ID).toString();
        String class_name = database.child("Class").child(class_key).child("ClassName").toString();
        return class_name;
    }

    public int fetchClassID(String class_name){
        String class_key = database.child("Class").orderByChild("ClassName").equalTo(class_name).toString();
        int class_id = Integer.parseInt(database.child("Class").child(class_key).child("ClassID").toString());
        return class_id;
    }

    public String fetchEnrollmentKey(int class_ID, String trainee_ID){
        String enrollment_key = database.child("Enroll")
                .orderByChild("classId").equalTo(class_ID)
                .orderByChild("trainee").equalTo(trainee_ID).toString();
        return enrollment_key;
    }

    public void fetchClassNameList(){
        listener = database.child("Class").addValueEventListener(new ValueEventListener() {
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