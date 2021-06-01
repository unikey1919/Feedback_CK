package com.example.feedbackapplication.ui.classjoin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feedbackapplication.Adapter.ClassAdapter;
import com.example.feedbackapplication.Adapter.ClassRoleTraineeAdapter;
import com.example.feedbackapplication.Adapter.ClassRoleTrainerAdapter;
import com.example.feedbackapplication.Adapter.ModuleRoleAdapter;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Enrollment;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassFragment extends Fragment implements ClassAdapter.ClickListener, ClassRoleTrainerAdapter.ClickListener, ClassRoleTraineeAdapter.ClickListener{
    private ClassRoleTrainerAdapter roleAdapter;
    private ClassRoleTraineeAdapter roleAdapter1;
    private ClassAdapter adapter;
    private RecyclerView rcvClass;
    private DatabaseReference database, refAssignment,refEnroll;
    private ArrayList<Assignment> arrayList;
    private ArrayList<Enrollment> arrayList1;
    private FloatingActionButton btnInsert;
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
        btnInsert = root.findViewById(R.id.btnNewClass);

        //Retrieve data
        if (role.equals("admin")){
            FirebaseRecyclerOptions<Class> options =
                    new FirebaseRecyclerOptions.Builder<Class>()
                            .setQuery(database, Class.class)
                            .build();
            adapter = new ClassAdapter(options,this);
            rcvClass.setAdapter(adapter);
        }

        //Retrieve data when trainer log
        if(role.equals("trainer"))
        {
            btnInsert.setVisibility(root.GONE);
            retrieveTrainer();
            rcvClass.setAdapter(roleAdapter);
        }

        //Retrieve data when trainee log
        if(role.equals("trainee"))
        {
            btnInsert.setVisibility(root.GONE);
            retrieveTrainee();
            rcvClass.setAdapter(roleAdapter1);
        }

        //Save data
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
    public void updateClicked(Class class1) {
        EventBus.getDefault().postSticky(class1);
        Navigation.findNavController(getView()).navigate(R.id.action_nav_class_to_nav_edit_class);
    }

    @Override
    public void deleteClicked(Class class1) {
        Date now = new Date();
        String now1 = sdf.format(now).toString();
        if (CheckDates(now1, class1.getStartDate())==true | CheckDates(class1.getEndDate(), now1)==true){
            EventBus.getDefault().postSticky(class1);
            DeleteClassDialog delClass = new DeleteClassDialog();
            delClass.show(getActivity().getSupportFragmentManager(),"delete");
        }
        else{
            EventBus.getDefault().postSticky(class1);
            DeleteClassActiveDialog delClass = new DeleteClassActiveDialog();
            delClass.show(getActivity().getSupportFragmentManager(),"delete");
        }
    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
        userName = results.getString("username");
    }

    static SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");
    public static boolean CheckDates(String d1, String d2)  {
        boolean b = false;
        try {
            if(sdf.parse(d1).before(sdf.parse(d2)))
            {
                b = true;//If start date is before end date
            }
            else if(sdf.parse(d1).equals(sdf.parse(d2)))
            {
                b = false;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public void retrieveTrainer(){
        refAssignment = FirebaseDatabase.getInstance().getReference().child("Assignment");
        Query queryAsg = refAssignment.orderByChild("trainerID").equalTo(userName);
        arrayList = new ArrayList<>();
        roleAdapter = new ClassRoleTrainerAdapter(getContext(),arrayList, this::seeClicked);
        queryAsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Assignment assignment = dataSnapshot.getValue(Assignment.class);
                    arrayList.add(assignment);
                    roleAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void seeClicked(Assignment assignment) {
        Bundle bundle = new Bundle();
        bundle.putInt("classid", assignment.getClassID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_class_to_nav_classtrainer_listtrainee, bundle);
    }

    public void retrieveTrainee(){
        refEnroll = FirebaseDatabase.getInstance().getReference().child("Enrollment");
        Query queryAsg = refEnroll.orderByChild("traineeID").equalTo(userName);
        arrayList1 = new ArrayList<>();
        roleAdapter1 = new ClassRoleTraineeAdapter(getContext(),arrayList1, this::see1Clicked);
        queryAsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    arrayList1.add(enrollment);
                    roleAdapter1.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void see1Clicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();
        bundle.putInt("classid", enrollment.getClassID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_class_to_nav_classtrainer_listtrainee, bundle);
    }
}