package com.example.feedbackapplication.ui.classjoin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.feedbackapplication.Adapter.ClassAdapter;
import com.example.feedbackapplication.Adapter.ClassRoleTrainerAdapter;
import com.example.feedbackapplication.Adapter.ClassRoleTrainerListTraineeAdapter;
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
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassRoleTrainerListTraineeFragment extends Fragment{
    private RecyclerView rcvTraineeList;
    Button btnBack;
    private Assignment assignment;
    private ArrayList<Enrollment> arrayList;
    private ClassRoleTrainerListTraineeAdapter roleAdapter;
    private int assignmentClassID;
    TextView tvClassID, tvClassName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.trainer_trainee_list_fragment, container, false);

        rcvTraineeList = root.findViewById(R.id.rcv_trainee_traineelist);
        rcvTraineeList.setHasFixedSize(true);
        rcvTraineeList.setLayoutManager(new LinearLayoutManager(root.getContext()));
        btnBack = root.findViewById(R.id.btnBack_trainer);
        tvClassID = root.findViewById(R.id.tvClassID_trainee);
        tvClassName = root.findViewById(R.id.tvClassName_trainee);
        assignmentClassID = getArguments().getInt("classid");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_classtrainer_listtrainee_to_nav_class);
            }
        });

        //dinh dang tvClassID
        final SpannableStringBuilder sb = new SpannableStringBuilder("Class ID: ");
        sb.append(String.valueOf(assignmentClassID));
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvClassID.setText(sb);

        //get class name
        DatabaseReference refClass = FirebaseDatabase.getInstance().getReference().child("Class");
        Query query1 = refClass.orderByChild("classID").equalTo(assignmentClassID);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //dinh dang tvClassName
                    final SpannableStringBuilder sb1 = new SpannableStringBuilder("Class Name: ");
                    sb1.append(dataSnapshot.child("className").getValue().toString());
                    final ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.rgb(0, 0, 0));
                    final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
                    sb1.setSpan(fcs1, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb1.setSpan(bss1, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvClassName.setText(sb1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Lay ra danh sach enrollment co classID
        DatabaseReference refEnroll1 = FirebaseDatabase.getInstance().getReference().child("Enrollment");
        Query query2 = refEnroll1.orderByChild("classID").equalTo(assignmentClassID);
        arrayList = new ArrayList<>();
        roleAdapter = new ClassRoleTrainerListTraineeAdapter(getContext(),arrayList);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    arrayList.add(enrollment);
                    roleAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rcvTraineeList.setAdapter(roleAdapter);

        return root;
    }
//    //Gui du lieu qua tu EventBus
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
//    @Subscribe(sticky = true)
//    public void onAssignmentEvent(Assignment assignment1) {
//        assignmentClassID = assignment1.getClassID();
//    }

}