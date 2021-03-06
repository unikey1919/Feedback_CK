package com.example.feedbackapplication.ui.assignment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.AssignmentAdapter;
import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.Adapter.ModuleRoleAdapter;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class AssignmentFragment extends Fragment implements AssignmentAdapter.ClickListener{

    private AssignmentAdapter adapter;
    private RecyclerView rcvAssignment;
    private DatabaseReference database, refModule;
    static String role, userName;
    private FirebaseRecyclerOptions<Assignment> options;
    private Button btnSearch;
    private FloatingActionButton btnAdd;
    private EditText edtSearch;
    private Query query;
    private Button btnSuccess,btnCancel;
    private TextView txt,txtExist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.assignment_fragment, container, false);
        getDataFromDB();
        database = FirebaseDatabase.getInstance().getReference().child("Assignment");

        //Search Data
        edtSearch = root.findViewById(R.id.edtSearch);
        btnSearch = root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseSearch(edtSearch.getText().toString().trim());
            }
        });


        rcvAssignment = root.findViewById(R.id.rcv_assignment);
        rcvAssignment.setHasFixedSize(true);
        rcvAssignment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        if(role.equals("admin")){
            query =database;
        }
        if(role.equals("trainer")){
            query = database.orderByChild("trainerID").equalTo(userName);
        }

        FirebaseRecyclerOptions<Assignment> options =
                new FirebaseRecyclerOptions.Builder<Assignment>()
                        .setQuery(query, Assignment.class)
                        .build();
        adapter = new AssignmentAdapter(options,this,role);
        rcvAssignment.setAdapter(adapter);

        //insert
        btnAdd = root.findViewById(R.id.btnNew);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_assignment_to_nav_add);
            }
        });
        return root;
    }

    private void firebaseSearch(String s) {
        Query query,query1;
        if(s.length() == 0){
            query = database;
        }
        else {
            query = database.orderByChild("code").startAt(s).endAt(s+"\uf8ff");
        }
        FirebaseRecyclerOptions<Assignment> options =
                new FirebaseRecyclerOptions.Builder<Assignment>()
                        .setQuery(query, Assignment.class)
                        .build();
        adapter = new AssignmentAdapter(options,this,role);
        adapter.startListening();
        rcvAssignment.setAdapter(adapter);
    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        userName = results.getString("username");
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
    public void updateClicked(Assignment model,String moduleName, String className, String position) {
        Bundle bundle = new Bundle();
        bundle.putInt("classID",model.getClassID());
        bundle.putString("position",position);
        bundle.putInt("moduleID",model.getModuleID());
        bundle.putString("moduleName",moduleName);
        bundle.putString("className",className);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_assignment_to_nav_edit,bundle);
    }

    @Override
    public void deleteClicked(Assignment model,String moduleName, String className, String key) {
        if(moduleName.length() == 0 | className.length() == 0){
            WarningDialog(key);
        }
        else {
            WarningDialogExist(key);
        }
    }

    private void WarningDialog(String key) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.sure_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Assignment")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SuccessDialog();
                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void WarningDialogExist(String key) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.sure_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        txtExist= dialog.findViewById(R.id.txt2);
        txtExist.setText(getActivity().getString(R.string.sure2));
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Assignment")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SuccessDialog();
                            }
                        });
                dialog.dismiss();
            }
        });

        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void SuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        txt = dialog.findViewById(R.id.txt);
        txt.setText("Delete Success!");
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