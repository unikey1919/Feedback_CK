package com.example.feedbackapplication.ui.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.AssignmentAdapter;
import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.Adapter.ModuleRoleAdapter;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AssignmentFragment extends Fragment implements AssignmentAdapter.ClickListener{

    private AssignmentAdapter adapter;
    private RecyclerView rcvAssignment;
    private DatabaseReference database, refModule;
    static String role, userName;
    private FirebaseRecyclerOptions<Assignment> options;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.assignment_fragment, container, false);
        getDataFromDB();

        database = FirebaseDatabase.getInstance().getReference().child("Assignment");
        rcvAssignment = root.findViewById(R.id.rcv_assignment);
        rcvAssignment.setHasFixedSize(true);
        rcvAssignment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<Assignment> options =
                new FirebaseRecyclerOptions.Builder<Assignment>()
                        .setQuery(database, Assignment.class)
                        .build();
        adapter = new AssignmentAdapter(options,this);
        rcvAssignment.setAdapter(adapter);

        return root;
    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        userName = results.getString("userName");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void updateClicked(Module module) {

    }

    @Override
    public void deleteClicked(Module module) {

    }
}