package com.example.feedbackapplication.ui.assignment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAssignmentFragment extends Fragment {
    private TextInputEditText classID,className, moduleID, moduleName;
    private Button btnEdit, btnBack, btnSuccess;
    private AutoCompleteTextView actTrainerID;
    private DatabaseReference database;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private ValueEventListener listener;
    private String key;
    private TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_assignment, container, false);

        //init
        classID = root.findViewById(R.id.txt_ip_edt_ClassID);
        moduleID = root.findViewById(R.id.txt_ip_edt_ModuleID);
        className = root.findViewById(R.id.txt_ip_edt_ClassName);
        moduleName = root.findViewById(R.id.txt_ip_edt_ModuleName);

        //on screen
        classID.setText(Integer.toString(getArguments().getInt("classID")));
        moduleID.setText(Integer.toString(getArguments().getInt("moduleID")));
        className.setText(getArguments().getString("className"));
        moduleName.setText(getArguments().getString("moduleName"));
        key = getArguments().getString("position");

        //Take data to dropdown trainerID
        actTrainerID = root.findViewById(R.id.actTrainerID);
        database = FirebaseDatabase.getInstance().getReference("Trainer");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        actTrainerID.setAdapter(adapter);
        fetchData();

        //Edit
        btnEdit = root.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainerID = actTrainerID.getText().toString().trim();

                DatabaseReference refAssign = FirebaseDatabase.getInstance().getReference("Assignment");
                Query refTrainer = refAssign.orderByChild("moduleID").equalTo(getArguments().getInt("moduleID"));
                refTrainer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String a = dataSnapshot.child("trainerID").getValue().toString();
                            if(trainerID.equals(a)){
                                FailDialog();
                                return;
                            }
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("trainerID",trainerID);

                        FirebaseDatabase.getInstance().getReference()
                                .child("Assignment")
                                .child(key)
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        SuccessDialog(v);
                                    }
                                });
                        refTrainer.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_to_nav_assignment);
            }
        });

        return root;
    }

    private void fetchData() {
        listener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
                actTrainerID.setText(adapter.getItem(0),false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SuccessDialog(View view) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        txt = dialog.findViewById(R.id.txt);
        txt.setText("Success!");
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Navigation.findNavController(view).navigate(R.id.action_nav_edit_to_nav_assignment);
            }
        });
        dialog.show();
    }

    private void FailDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fail_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}