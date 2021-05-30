package com.example.feedbackapplication.ui.classjoin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditClassFragment extends Fragment {

    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    EditText edtClassName, edtCapacity, edtStartDate, edtEndDate;
    ImageButton ibtnCalendar1, getIbtnCalendar2;
    private int classID = 0;
    private Class class1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_class, container, false);

        edtClassName = view.findViewById(R.id.edtClassName);
        edtCapacity = view.findViewById(R.id.edtCapacity);

        edtClassName.setText(getArguments().getString("classname"));
        edtCapacity.setText(getArguments().getString("capacity"));
        classID = getArguments().getInt("id");

        //Take data to dropdown adminID
//        database = FirebaseDatabase.getInstance().getReference("Admin");
//        list = new ArrayList<String>();
//        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
//        adminID.setAdapter(adapter);
//        fetchData();

        //update
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(classID);
                Map<String,Object> map = new HashMap<>();
                map.put("className", edtClassName.getText().toString().trim());
                map.put("capacity", Integer.parseInt(edtCapacity.getText().toString()));

                FirebaseDatabase.getInstance().getReference()
                        .child("Class")
                        .child(key)
                        .updateChildren(map);
                Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

//    public void fetchData(){
//        listener = database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    list.add(dataSnapshot.getKey());
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}