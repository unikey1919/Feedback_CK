package com.example.feedbackapplication.ui.enrollment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.BetterEnrollmentAdapter;
import com.example.feedbackapplication.Adapter.BetterEnrollmentAdapter.ClickListener;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Enrollment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;


public class EnrollmentFragment extends Fragment implements ClickListener, AdapterView.OnItemSelectedListener {
    private final ArrayList<String> arrayListClassName = new ArrayList<>(); //for ClassName List
    //private EnrollmentAdapter adapter; //for Enrollment List

    private RecyclerView rcvEnrollment;
    private BetterEnrollmentAdapter betterEnrollmentAdapter;
    private ArrayList<Enrollment> enrollmentArrayList; // for BetterEnrollmentAdapter

    private ArrayAdapter<String> adapterClassName; //for ClassName List
    private AutoCompleteTextView actClassName; //for ClassName Filter
    //for item info
    private String currentClassName = "All";
    private String TraineeName = "0";
    private String ClassName = "0";
    private String EnrollmentKey = "0";
    //private String just_a_string = "0";

    private ValueEventListener fetchDataForRCVEnrollment1;
    private ValueEventListener fetchDataForRCVEnrollment2;
    private ValueEventListener fetchDataForRCVEnrollment3;
    private ValueEventListener fetchInDeleteClicked;
    private ValueEventListener fetchInDeleteClicked2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String test_result = FireBaseHelper.fetchTraineeName_v2("traineeA");
        //Toast.makeText(getActivity(), test_result, Toast.LENGTH_LONG).show();
        View root = inflater.inflate(R.layout.enrollment_fragment, container, false);

        //getting views
        FloatingActionButton btnInsert = root.findViewById(R.id.btnNew);
        rcvEnrollment = root.findViewById(R.id.rcvEnrollment);
        rcvEnrollment.setHasFixedSize(true);
        rcvEnrollment.setLayoutManager(new LinearLayoutManager(root.getContext()));

        enrollmentArrayList = new ArrayList<>();
        betterEnrollmentAdapter = new BetterEnrollmentAdapter(
                (ClickListener) this,
                getContext(),
                enrollmentArrayList
        );

        //Take data to dropdown classID
        actClassName = root.findViewById(R.id.actClassName);
        adapterClassName = new ArrayAdapter<>(getActivity(), R.layout.option_item, arrayListClassName);
        fetchDataForACTVClassName();
        actClassName.setAdapter(adapterClassName);
        actClassName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getContext(), "test_result", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getContext(), "test_results", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Retrieve data
                fetchDataForRCVEnrollment(s.toString());
            }
        });

        rcvEnrollment.setAdapter(betterEnrollmentAdapter);
        //add
        btnInsert.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_enrollment_to_nav_add));

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentClassName = parent.getItemAtPosition(position).toString();
        //spClassName.setAdapter(adapterClassName);
        Toast.makeText(getActivity(), currentClassName, Toast.LENGTH_LONG).show(); //ko lay dc currentClassName o day
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        currentClassName = parent.getItemAtPosition(0).toString();
        Toast.makeText(getActivity(), currentClassName, Toast.LENGTH_LONG).show(); //ko lay dc currentClassName o day
    }

    public void fetchDataForRCVEnrollment(String a_string) {
        if (!a_string.equals("All")) {
            //get class IDs of classes which contain string s
            killFetch("Enrollment",fetchDataForRCVEnrollment3);
            fetchDataForRCVEnrollment1 = FirebaseDatabase.getInstance().getReference("Class")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Class temp = dataSnapshot.getValue(Class.class);
                                if (temp != null)
                                    if (temp.getClassName().contains(a_string)) {
                                        //get enrollments with class IDs
                                        fetchDataForRCVEnrollment2 = FirebaseDatabase.getInstance().getReference().child("Enrollment")
                                                .orderByChild("classID").equalTo(temp.getClassID())
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        //killFetchDataForRCVEnrollment2();
                                                        enrollmentArrayList = new ArrayList<>();
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                                                            if (enrollment != null)
                                                                enrollmentArrayList.add(enrollment);
                                                        }
                                                        betterEnrollmentAdapter = new BetterEnrollmentAdapter(
                                                                EnrollmentFragment.this,
                                                                getContext(),
                                                                enrollmentArrayList);
                                                        rcvEnrollment.setAdapter(betterEnrollmentAdapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } else {
            killFetch("Class",fetchDataForRCVEnrollment1);
            killFetch("Enrollment",fetchDataForRCVEnrollment2);
            //get All Enrollments
            fetchDataForRCVEnrollment3 = FirebaseDatabase.getInstance().getReference("Enrollment")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            enrollmentArrayList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                if (temp != null) {
                                    enrollmentArrayList.add(temp);
                                }
                            }
                            betterEnrollmentAdapter = new BetterEnrollmentAdapter(
                                    EnrollmentFragment.this,
                                    getContext(),
                                    enrollmentArrayList);
                            rcvEnrollment.setAdapter(betterEnrollmentAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    public void fetchDataForACTVClassName() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListClassName.add("All");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String temp = dataSnapshot.child("className").getValue(String.class);
                    if (temp != null)
                        arrayListClassName.add(temp);
                }
                //adapter.notifyDataSetChanged();
                //adapterClassName.notifyDataSetChanged();
                Toast.makeText(getActivity(), "currentClassName", Toast.LENGTH_LONG).show(); // 1 lan khi frag create
                actClassName.setText(adapterClassName.getItem(0), false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //betterEnrollmentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.startListening();
    }

    @Override
    public void viewClicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();
        bundle.putString("TraineeID", enrollment.getTraineeID());
        bundle.putInt("ClassID", enrollment.getClassID());
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_enrollment_to_nav_view, bundle);
    }

    @Override
    public void updateClicked(Enrollment enrollment) {
        Bundle bundle = new Bundle();

        FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("classID").equalTo(enrollment.getClassID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Class temp = dataSnapshot.getValue(Class.class);
                                if (temp != null) {
                                    ClassName = temp.getClassName();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("Trainee")
                .orderByChild("UserName").equalTo(enrollment.getTraineeID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                //Trainee temp = dataSnapshot.getValue(Trainee.class); //Trainee ko lay dc
                                String temp = dataSnapshot.child("Name").getValue(String.class);
                                if (temp != null) {
                                    TraineeName = temp;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference("Enrollment")
                .orderByChild("classID").equalTo(enrollment.getClassID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                if (temp != null) {
                                    if (temp.getTraineeID().equals(enrollment.getTraineeID())) {
                                        EnrollmentKey = dataSnapshot.getKey();
                                        if (EnrollmentKey != null && !TraineeName.equals("0") && !ClassName.equals("0")) {
                                            bundle.putString("TraineeID", enrollment.getTraineeID());
                                            bundle.putString("TraineeName", TraineeName);
                                            bundle.putString("ClassName", ClassName);
                                            bundle.putString("EnrollmentKey", EnrollmentKey);
                                            Navigation.findNavController(requireView()).navigate(R.id.action_nav_enrollment_to_nav_edit, bundle);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public void deleteClicked(Enrollment enrollment) {
        fetchInDeleteClicked = FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("classID").equalTo(enrollment.getClassID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Class temp = dataSnapshot.getValue(Class.class);
                                if (temp != null) {
                                    //String temp_key = dataSnapshot.getKey();
                                    if (isOverDated(temp.getEndDate())) {
                                        //class ended
                                        deleteClicked2(enrollment,2);
                                    }else {
                                        deleteClicked2(enrollment,4);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void deleteClicked2(Enrollment enrollment, int number) {
        killFetch("Class",fetchInDeleteClicked);
        fetchInDeleteClicked2 = FirebaseDatabase.getInstance().getReference("Enrollment")
                .orderByChild("classID").equalTo(enrollment.getClassID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Enrollment temp = dataSnapshot.getValue(Enrollment.class);
                                if (temp != null) {
                                    if(temp.getTraineeID().equals(enrollment.getTraineeID())){
                                        String enrollment_key = dataSnapshot.getKey();
                                        deleteConfirmDialog(enrollment_key,number);
                                        killFetch("Enrollment",fetchInDeleteClicked2);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void deleteConfirmDialog(String key, int number) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_red_4_blue_red);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        if(number==4) {
            TextView text1 = dialog.findViewById(R.id.text1);
            text1.setText("Are you sure?");
            TextView text2 = dialog.findViewById(R.id.text2);
            text2.setText("Class is not over. Do you want to");
            TextView text3 = dialog.findViewById(R.id.text3);
            text3.setText("remove the trainee from the");
            TextView text4 = dialog.findViewById(R.id.text4);
            text4.setText("classroom?");
        }else if(number==2){
            TextView text1 = dialog.findViewById(R.id.text1);
            text1.setText("Are you sure?");
            TextView text2 = dialog.findViewById(R.id.text2);
            text2.setText("Do you want to delete this item?");
            TextView text3 = dialog.findViewById(R.id.text3);
            text3.setText("");
            TextView text4 = dialog.findViewById(R.id.text4);
            text4.setText("");
        }
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            if (key != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Enrollment")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(task -> Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show());
                dialog.dismiss();
                deleteSuccessDialog();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void deleteSuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_green_1_blue);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView text1 = dialog.findViewById(R.id.text1);
        text1.setText("Delete success!");
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void killFetch(String path, ValueEventListener valueEventListener) {
        if(valueEventListener!=null)
            FirebaseDatabase.getInstance().getReference(path).removeEventListener(valueEventListener);
    }

    private boolean isOverDated(String date)  {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("MM/dd/yyyy");
            Date temp = simpleDateFormat.parse(date);
            if(temp!=null && temp.after(Date.from(Instant.now())))
            {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            return true;
        }
        return true;
    }

}