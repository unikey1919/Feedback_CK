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

import com.example.feedbackapplication.Adapter.ClassAdapter;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Class;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        //Retrieve data when trainer log
        if(role.equals("trainer"))
        {
            FirebaseRecyclerOptions<Assignment> options =
                    new FirebaseRecyclerOptions.Builder<Class>()
                            .setQuery(database, Class.class)
                            .build();
            adapter = new ClassAdapter(options,this);
            rcvClass.setAdapter(adapter);
        }

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
        userName = results.getString("userName");
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
}