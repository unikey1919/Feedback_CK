package com.example.feedbackapplication.ui.module;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddModuleFragment extends Fragment {
    private Button btnBack,btnAdd;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView adminID;
    private TextInputEditText moduleName, moduleID;
    private int maxID = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_module, container, false);

        //Take data to dropdown adminID
        adminID = view.findViewById(R.id.actAdminID);
        database = FirebaseDatabase.getInstance().getReference("Admin");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        adminID.setAdapter(adapter);

        //database module
        reference = FirebaseDatabase.getInstance().getReference().child("Module");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        maxID = Integer.parseInt(dataSnapshot.getKey());
                    }


                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Insert Data
        moduleName = view.findViewById(R.id.txt_ip_edt_ModuleName);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = moduleName.getText().toString().trim();
                String adminId = adminID.getText().toString().trim();
                int moduleID = maxID + 1;
                Module module = new Module(moduleID,adminId,name);

                reference.child(String.valueOf(moduleID)).setValue(module);
            }
        });

        //Back
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_module);
            }
        });

        fetchData();
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
               adminID.setText(adapter.getItem(0),false);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}