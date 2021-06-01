package com.example.feedbackapplication.ui.module;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.Adapter.AssignmentAdapter;
import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.ui.logout.LogoutDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ModuleFragment extends Fragment implements ModuleAdapter.ClickListener {

    private ModuleAdapter adapter;
    private RecyclerView rcvModule;
    private DatabaseReference database;
    private ArrayList<Module> arrayList;
    private FloatingActionButton btnInsert;
    private FirebaseRecyclerOptions<Module> options;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.module_fragment, container, false);

<<<<<<< HEAD
        Log.d("TAGkk", "onCreateView : ");
=======
        btnInsert = root.findViewById(R.id.btnNew);
        getDataFromDB();
>>>>>>> origin/test

        database = FirebaseDatabase.getInstance().getReference().child("Module");
        rcvModule = root.findViewById(R.id.rcvModule);
        rcvModule.setHasFixedSize(true);
        rcvModule.setLayoutManager(new LinearLayoutManager(root.getContext()));
        //Retrieve data
<<<<<<< HEAD
        FirebaseRecyclerOptions<Module> options =
                new FirebaseRecyclerOptions.Builder<Module>()
                        .setQuery(database, Module.class)
                        .build();
        adapter = new ModuleAdapter(options,this);
        rcvModule.setAdapter(adapter);
=======
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
            btnInsert.setVisibility(root.GONE);
            retrieveTrainer();
            rcvModule.setAdapter(roleAdapter);
        }
        //Retrieve data when trainee log
        if(role.equals("trainee"))
        {
            btnInsert.setVisibility(root.GONE);
            retrieveTrainee();
            rcvModule.setAdapter(roleAdapter);
        }

>>>>>>> origin/test

        //Save data

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
        adapter.startListening();
        Log.d("TAGkk", "onCreateView start: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }


    @Override
    public void updateClicked(Module module) {
        Bundle bundle = new Bundle();
        bundle.putString("name",module.getModuleName());
        bundle.putString("adminID",module.getAdminID());
        bundle.putInt("id",module.getModuleID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_module_to_nav_edit,bundle);
    }

    @Override
    public void deleteClicked(Module module) {
<<<<<<< HEAD
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
=======
            DeleteModuleFragmet del = new DeleteModuleFragmet(module);
            del.show(getActivity().getSupportFragmentManager(),"delete");

    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        userName = results.getString("username");
    }

    public void retrieveTrainer(){
        refAssignment = FirebaseDatabase.getInstance().getReference().child("Assignment");
        Query queryAsg = refAssignment.orderByChild("trainerID").equalTo(userName);
        arrayList = new ArrayList<>();
        roleAdapter = new ModuleRoleAdapter(getContext(),arrayList);
        rcvModule.setAdapter(roleAdapter);
        queryAsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String moduleID = dataSnapshot.child("moduleID").getValue().toString();
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
        refEnroll = FirebaseDatabase.getInstance().getReference().child("Enrollment");
        refAssignment = FirebaseDatabase.getInstance().getReference().child("Assignment");
        Query queryEnroll = refEnroll.orderByChild("traineeID").equalTo(userName);
        arrayList = new ArrayList<>();
        roleAdapter = new ModuleRoleAdapter(getContext(),arrayList);
        rcvModule.setAdapter(roleAdapter);
        queryEnroll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int classID = dataSnapshot.child("classID").getValue(Integer.class);
                    Query queryAsg = refAssignment.orderByChild("classID").equalTo(classID);
                    queryAsg.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String moduleID = dataSnapshot.child("moduleID").getValue().toString();
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
>>>>>>> origin/test
    }
}