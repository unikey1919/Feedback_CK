package com.example.feedbackapplication.ui.module;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditModuleFragment extends Fragment {

    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView adminID;
    private TextInputEditText moduleName;
    private int moduleID = 0;
    private Module module;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_module, container, false);

        moduleName = view.findViewById(R.id.txt_ip_edt_ModuleName);
        adminID = view.findViewById(R.id.actAdminID);

        moduleName.setText(getArguments().getString("name"));
        adminID.setText(getArguments().getString("adminID"));
        moduleID = getArguments().getInt("id");

        //Take data to dropdown adminID
        database = FirebaseDatabase.getInstance().getReference("Admin");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        adminID.setAdapter(adapter);
        fetchData();

        //update
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(moduleID);
                Map<String,Object> map = new HashMap<>();
                map.put("moduleName",moduleName.getText().toString().trim());
                map.put("adminID",adminID.getText().toString().trim());

                FirebaseDatabase.getInstance().getReference()
                        .child("Module")
                        .child(key)
                        .updateChildren(map);
                Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void fetchData(){
        listener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}