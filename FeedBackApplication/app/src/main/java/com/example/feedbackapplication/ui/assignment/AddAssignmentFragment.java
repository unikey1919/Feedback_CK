package com.example.feedbackapplication.ui.assignment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAssignmentFragment extends Fragment {
    private TextInputLayout txt_ip_ModuleName, txt_ip_ClassName, txt_ip_TrainerID;
    private AutoCompleteTextView actModuleName, actClassName, actTrainerID;
    private Button btnBack, btnAdd, btnSuccess;
    private DatabaseReference refModule, refClass, refTrainer;
    private ValueEventListener listener,listener1,listener2,listener3,listener4;
    private ArrayAdapter<String> adapterModule, adapterClass, adapterTrainer;
    private ArrayList<String> listModule, listClass, listTrainer;
    private static int ClassID,ModuleID;
    private String inspirationalKey = "where_is_my_key";
    private Spinner spnModule, spnClass, spnTrainer;
    private ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_assignment, container, false);


        //take module name
        refModule = FirebaseDatabase.getInstance().getReference().child("Module");
        spnModule = root.findViewById(R.id.spnModule);
        listModule = new ArrayList<String>();
        adapterModule = new ArrayAdapter<>(getActivity(),R.layout.option_item,listModule);
        spnModule.setAdapter(adapterModule);
        spnModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Query query = refModule.orderByChild("moduleName").equalTo(spnModule.getSelectedItem().toString());
                listener3 =query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ModuleID = dataSnapshot.child("moduleID").getValue(Integer.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fetchModuleData();

        //take class name
        refClass = FirebaseDatabase.getInstance().getReference().child("Class");
        spnClass = root.findViewById(R.id.spnClassName);
        listClass = new ArrayList<String>();
        adapterClass = new ArrayAdapter<>(getActivity(),R.layout.option_item,listClass);
        spnClass.setAdapter(adapterClass);
        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Query query = refClass.orderByChild("className").equalTo(spnClass.getSelectedItem().toString());
                listener4 =query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ClassID = dataSnapshot.child("classID").getValue(Integer.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fetchClassData();

        //take trainer
        refTrainer = FirebaseDatabase.getInstance().getReference().child("Trainer");
        spnTrainer = root.findViewById(R.id.spnTrainer);
        listTrainer = new ArrayList<String>();
        adapterTrainer = new ArrayAdapter<>(getActivity(),R.layout.option_item,listTrainer);
        spnTrainer.setAdapter(adapterTrainer);
        fetchTrainerData();

        //Insert
        btnAdd = root.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<>();
                Long ts = System.currentTimeMillis()/1000;
                String TrainerID = spnTrainer.getSelectedItem().toString();
                String Code = "CL" + ClassID + "M" +ModuleID + "T" + ts.toString();
                DatabaseReference refAssign = FirebaseDatabase.getInstance().getReference("Assignment");
                Query refTrainer = refAssign.orderByChild("moduleID").equalTo(ModuleID);
                refTrainer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String a = dataSnapshot.child("trainerID").getValue().toString();
                            if(TrainerID.equals(a)){
                                FailDialog();
                                return;
                            }
                        }
                        Assignment assignment = new Assignment(ModuleID, ClassID, TrainerID, Code);
                        inspirationalKey = FirebaseDatabase.getInstance().getReference("Assignment").push().getKey();
                        FirebaseDatabase.getInstance().getReference("Assignment").child(inspirationalKey).setValue(assignment);
                        SuccessDialog(v);
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
                Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_assignment);
            }
        });
        return root;
    }


    public void fetchModuleData(){
        listener = refModule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listModule.add(dataSnapshot.child("moduleName").getValue().toString());
                }
                adapterModule.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchClassData(){
        listener1 = refClass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int a = dataSnapshot.child("classID").getValue(Integer.class);
                    listClass.add(dataSnapshot.child("className").getValue().toString());
                }
                adapterClass.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchTrainerData(){
        listener2 = refTrainer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listTrainer.add(dataSnapshot.getKey());
                }
                adapterTrainer.notifyDataSetChanged();
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
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Navigation.findNavController(view).navigate(R.id.action_nav_add_to_nav_assignment);
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