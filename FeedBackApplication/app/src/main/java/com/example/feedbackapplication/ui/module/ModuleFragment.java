package com.example.feedbackapplication.ui.module;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.Adapter.ModuleRoleAdapter;
import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModuleFragment extends Fragment implements ModuleAdapter.ClickListener {

    private ModuleAdapter adapter;
    private ModuleRoleAdapter roleAdapter;
    private RecyclerView rcvModule;
    private DatabaseReference database, refAssignment,refEnroll;
    private ArrayList<Module> arrayList;
    private FloatingActionButton btnInsert;
    private FirebaseRecyclerOptions<Module> options;
    static String role, userName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.module_fragment, container, false);

        getDataFromDB();

        database = FirebaseDatabase.getInstance().getReference().child("Module");
        rcvModule = root.findViewById(R.id.rcvModule);
        rcvModule.setHasFixedSize(true);
        rcvModule.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Retrieve data
        if(role.equals("admin"))
        {
            FirebaseRecyclerOptions<Module> options =
                    new FirebaseRecyclerOptions.Builder<Module>()
                            .setQuery(database, Module.class)
                            .build();
            adapter = new ModuleAdapter(options,this);
            rcvModule.setAdapter(adapter);
        }
        //Retrieve data when trainer log
        if(role.equals("trainer"))
        {
            retrieveTrainer();
            rcvModule.setAdapter(roleAdapter);
        }
        //Retrieve data when trainee log
        if(role.equals("trainee"))
        {
            retrieveTrainee();
            rcvModule.setAdapter(roleAdapter);
        }


        //Save data
        btnInsert = root.findViewById(R.id.btnNew);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_module_to_nav_add);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(role.equals("admin")){
            adapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(role.equals("admin")){
            adapter.startListening();
        }
    }

    @Override
    public void updateClicked(Module module) {
        Bundle bundle = new Bundle();
        bundle.putString("name",module.getModuleName());
        bundle.putString("adminID",module.getAdminID());
        bundle.putInt("id",module.getModuleID());
        bundle.putString("startDate",module.getStartDate());
        bundle.putString("endDate",module.getEndDate());
        bundle.putString("feedbackTitle",module.getFeedbackTitle());
        bundle.putString("feedbackStartDate",module.getFeedbackStartDate());
        bundle.putString("feedbackEndDate",module.getFeedbackEndDate());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_module_to_nav_edit,bundle);
    }

    @Override
    public void deleteClicked(Module module) {
        String key = String.valueOf(module.getModuleID());
        FirebaseDatabase.getInstance().getReference()
                .child("Module")
                .child(key)
                .setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        userName = results.getString("username");
    }

    public void retrieveTrainer(){
        refAssignment = FirebaseDatabase.getInstance().getReference().child("Assignment");
        Query queryAsg = refAssignment.orderByChild("TrainerID").equalTo(userName);
        arrayList = new ArrayList<>();
        roleAdapter = new ModuleRoleAdapter(getContext(),arrayList);
        rcvModule.setAdapter(roleAdapter);
        queryAsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String moduleID = dataSnapshot.child("ModuleID").getValue().toString();
                    Query queryModule = database.child(moduleID);
                    queryModule.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Module module = snapshot.getValue(Module.class);
                            arrayList.add(module);
                            roleAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void retrieveTrainee(){
        refEnroll = FirebaseDatabase.getInstance().getReference().child("Enroll");
        refAssignment = FirebaseDatabase.getInstance().getReference().child("Assignment");
        Query queryEnroll = refEnroll.orderByChild("trainee").equalTo(userName);
        arrayList = new ArrayList<>();
        roleAdapter = new ModuleRoleAdapter(getContext(),arrayList);
        rcvModule.setAdapter(roleAdapter);
        queryEnroll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int classID = dataSnapshot.child("classId").getValue(Integer.class);
                    Query queryAsg = refAssignment.orderByChild("ClassID").equalTo(classID);
                    queryAsg.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String moduleID = dataSnapshot.child("ModuleID").getValue().toString();
                                Query queryModule = database.child(moduleID);
                                queryModule.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Module module = snapshot.getValue(Module.class);
                                        arrayList.add(module);
                                        roleAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}