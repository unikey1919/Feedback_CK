package com.example.feedbackapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.AssignmentAdapter;
import com.example.feedbackapplication.Adapter.ListFeedBackAdapter;
import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Item_ListFeedBackTrainee;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//implements ModuleAdapter.ClickListener
public class HomeFragment extends Fragment implements AssignmentAdapter.ClickListener{

    private HomeViewModel homeViewModel;
    private Button btnLogin;
    private FloatingActionButton btnAdd;
    private RecyclerView rcv_Category;
    static String role;
    static String user;
    private ModuleAdapter adapter;
    public DatabaseReference rootRef;
    private View root;
    private AssignmentAdapter adapter1;
    private RecyclerView rcvAssignment;
    private DatabaseReference database, refModule;
    private Query query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_home, container, false);

        getDataFromDB();
        if (role.equals("null")) {
            root = inflater.inflate(R.layout.fragment_home, container, false);
            btnLogin = root.findViewById(R.id.btnLoginHome);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (role.equals("admin")) {
            root = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
            getAdminDashboard();
        }
        if (role.equals("trainer")) {
            root = inflater.inflate(R.layout.fragment_dashboard_trainer, container, false);
            getTrainerDashboard();
        }

        if (role.equals("trainee")) {
//            root = inflater.inflate(R.layout.fragment_dashboard_trainee, container, false);
            root = inflater.inflate(R.layout.fragment_dashboard_trainee, container, false);

            TextView txt = root.findViewById(R.id.txt_tua);

            RecyclerView recyclerView = root.findViewById(R.id.rcvFeedbackList);

            ArrayList<Item_ListFeedBackTrainee> itemListFeedBackTrainees = new ArrayList<Item_ListFeedBackTrainee>();
            itemListFeedBackTrainees.add(new Item_ListFeedBackTrainee("...", "...", "...", "...","...","...","..."));
//            items.add(new Item("3", "cuc gach plus pro", "day la cai so 3, eqweqw eqwe qư eqwe qư eew wq ưqeffasff", "Nokia","a","b","c"));

            ListFeedBackAdapter adapter = new ListFeedBackAdapter(itemListFeedBackTrainees);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);


            rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference assignmentRef = rootRef.child("Enrollment");
            Query codeQuery = assignmentRef.orderByChild("traineeID").equalTo(user);
            codeQuery.addValueEventListener(new ValueEventListener() {          // lay classID
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    if(ds.getValue()!=null) { // = null la list chua co gi
                        for (DataSnapshot dataSnapshot : ds.getChildren()) {
                            Integer Classid = dataSnapshot.child("classID").getValue(Integer.class);
                            moduleIdByClassId(itemListFeedBackTrainees,Classid);
                            Log.d("lamon0", "clss id:- " +  Classid);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//            adapter = new ModuleAdapter(options,this);
//            rcvModule.setAdapter(adapter);
        }
        return root;
    }

    private void moduleIdByClassId(ArrayList<Item_ListFeedBackTrainee> itemListFeedBackTrainees, Integer Classid) {
        DatabaseReference assignmentRef = rootRef.child("Assignment");
        Query codeQuery = assignmentRef.orderByChild("ClassID").equalTo(Classid);
        codeQuery.addValueEventListener(new ValueEventListener() {          // lay classID
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if(ds.getValue()!=null) {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Integer MoId = dataSnapshot.child("ModuleID").getValue(Integer.class); // co moid
                        Log.d("lamon", "module id:- " + MoId);
                        ClassNameByClassId(itemListFeedBackTrainees, Classid, MoId);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ClassNameByClassId(ArrayList<Item_ListFeedBackTrainee> itemListFeedBackTrainees, Integer Classid, Integer MoId) {   // Date End Luon
        DatabaseReference assignmentRef = rootRef.child("Class");
        Query codeQuery = assignmentRef.orderByChild("classID").equalTo(Classid);
        codeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if(ds.getValue()!=null) {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        String className = dataSnapshot.child("className").getValue(String.class);
                        String endDate = dataSnapshot.child("endDate").getValue(String.class);
                        Log.d("lamon 2", "module id:- " + className+endDate);
                        GetTitleInModule(itemListFeedBackTrainees, Classid, MoId,className,endDate);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void GetTitleInModule(ArrayList<Item_ListFeedBackTrainee> itemListFeedBackTrainees, Integer Classid, Integer MoId, String className, String endDate) {   // Date End Luon
        DatabaseReference assignmentRef = rootRef.child("Module");
        Query codeQuery = assignmentRef.orderByChild("moduleID").equalTo(MoId);
        codeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        String feedbackTitle = dataSnapshot.child("feedbackTitle").getValue(String.class);
                        String moduleName = dataSnapshot.child("moduleName").getValue(String.class);
                        Log.d("lamon 3", "tit id:- " + feedbackTitle+" _ "+moduleName+ " ,, "+ds);
                        Log.d("TAG", "BEFORE ADD :-"+ "1"+"Module Name: "+moduleName+
                                "Class name: "+className+ "Feedback Title: "+feedbackTitle+
                                "Class ID: " + Classid.toString()+ "End time: "+ endDate+ "Module ID: " + MoId.toString());
                        itemListFeedBackTrainees.add(new Item_ListFeedBackTrainee("1", "Module Name: "+moduleName,
                                "Class name: "+className, "Feedback Title: "+feedbackTitle,
                                "Class ID: " + Classid.toString(), "End time: "+ endDate, "Module ID: " + MoId.toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void getDataFromDB() {
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        user = results.getString("username");
    }

    //get dashboard admin

    public void getAdminDashboard(){
        database = FirebaseDatabase.getInstance().getReference().child("Assignment");
        rcvAssignment = root.findViewById(R.id.rcv_assignment);
        rcvAssignment.setHasFixedSize(true);
        rcvAssignment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<Assignment> options =
                new FirebaseRecyclerOptions.Builder<Assignment>()
                        .setQuery(database, Assignment.class)
                        .build();
        adapter1 = new AssignmentAdapter(options, this,role);
        rcvAssignment.setAdapter(adapter1);

        //insert
        btnAdd = root.findViewById(R.id.btnNew);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_admin_to_nav_add);
            }
        });
    }

    public void getTrainerDashboard(){
        database = FirebaseDatabase.getInstance().getReference().child("Assignment");
        query = database.orderByChild("trainerID").equalTo(user);
        rcvAssignment = root.findViewById(R.id.rcv_assignment);
        rcvAssignment.setHasFixedSize(true);
        rcvAssignment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<Assignment> options =
                new FirebaseRecyclerOptions.Builder<Assignment>()
                        .setQuery(query, Assignment.class)
                        .build();
        adapter1 = new AssignmentAdapter(options, this,role);
        rcvAssignment.setAdapter(adapter1);

    }

    @Override
    public void updateClicked(Assignment module, String moduleName, String className) {
    }

    @Override
    public void deleteClicked(Assignment module) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if(role.equals("admin") | role.equals("trainer")){
            adapter1.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(role.equals("admin") | role.equals("trainer")){
            adapter1.startListening();
        }

    }
}