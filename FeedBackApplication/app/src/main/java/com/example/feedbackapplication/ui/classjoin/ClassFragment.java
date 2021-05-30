package com.example.feedbackapplication.ui.classjoin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedbackapplication.Adapter.ClassAdapter;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassFragment extends Fragment implements ClassAdapter.ClickListener {

    private ClassViewModel myViewModel;
    private ClassAdapter adapter;
    private RecyclerView rcvClass;
    private DatabaseReference database;
    private ArrayList<Class> arrayList;
    private FloatingActionButton btnInsert;
    private FirebaseRecyclerOptions<Class> options;
    static String role, userName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.class_fragment, container, false);
        getDataFromDB();

        database = FirebaseDatabase.getInstance().getReference().child("Class");
        rcvClass = root.findViewById(R.id.rcvClass);
        rcvClass.setHasFixedSize(true);
        rcvClass.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Retrieve data
        if (role.equals("admin")){
            FirebaseRecyclerOptions<Class> options =
                    new FirebaseRecyclerOptions.Builder<Class>()
                            .setQuery(database, Class.class)
                            .build();
            adapter = new ClassAdapter(options,this);
            rcvClass.setAdapter(adapter);
        }

//        //Retrieve data when trainer log
//        if(role.equals("trainer"))
//        {
//            retrieveTrainer();
//            rcvModule.setAdapter(roleAdapter);
//        }

        //Save data
        btnInsert = root.findViewById(R.id.btnNewClass);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_class_to_nav_add_class);
            }
        });
        return root;
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
    public void updateClicked(Class class1) {
        Bundle bundle = new Bundle();
        bundle.putString("classname", class1.getClassName());
        bundle.putInt("classID", class1.getClassID());
        bundle.putInt("capacity", class1.getCapacity());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_class_to_nav_edit_class, bundle);
    }

    @Override
    public void deleteClicked(Class class1) {
        String key = String.valueOf(class1.getClassID());
        FirebaseDatabase.getInstance().getReference()
                .child("Class")
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
        userName = results.getString("userName");
    }
}