package com.example.feedbackapplication.ui.classjoin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.ui.logout.LogoutDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddClassFragment extends Fragment {
    private Button btnBack,btnAdd, btnOK;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    EditText edtClassName, edtCapacity, edtStartDate, edtEndDate;
    TextView tvClassName, tvCapacity, tvStartDate, tvEndDate;
    ImageButton ibtnCalendar1, ibtnCalendar2;
    private int maxID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_class, container, false);

        //Take data to dropdown adminID
//        adminID = view.findViewById(R.id.actAdminID);
//        database = FirebaseDatabase.getInstance().getReference("Admin");
//        list = new ArrayList<String>();
//        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
//        adminID.setAdapter(adapter);

        //database module
        reference = FirebaseDatabase.getInstance().getReference().child("Class");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        maxID = Integer.parseInt(dataSnapshot.getKey());
                    }
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Anh xa
        edtClassName = view.findViewById(R.id.edtClassName);
        edtCapacity = view.findViewById(R.id.edtCapacity);
        ibtnCalendar1 = view.findViewById(R.id.ibtnCalendar1);
        ibtnCalendar2 = view.findViewById(R.id.ibtnCalendar2);
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        btnAdd = view.findViewById(R.id.btnSave);
        tvClassName = view.findViewById(R.id.valiClassName);
        tvCapacity = view.findViewById(R.id.valiCapacity);
        tvStartDate = view.findViewById(R.id.valiStartDate);
        tvEndDate = view.findViewById(R.id.valiEndDate);

        //Insert Data
        ibtnCalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtStartDate);
            }
        });

        ibtnCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtEndDate);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!validateClassName() | !validateCapacity() | !validateStartDate() | !validateEndDate()){
                        return;
                    }
                    else {
                        String className = edtClassName.getText().toString();
                        String startDate = edtStartDate.getText().toString();
                        String endDate = edtEndDate.getText().toString();
                        int classID = maxID + 1;
                        int capacity1 = Integer.parseInt(edtCapacity.getText().toString());
                        Class class1 = new Class(classID, className, capacity1, startDate, endDate);
                        reference.child(String.valueOf(classID)).setValue(class1);
                        AddClassSuccessDialog addSuccess = new AddClassSuccessDialog();
                        addSuccess.show(getActivity().getSupportFragmentManager(),"addsuccess");
                        Navigation.findNavController(v).navigate(R.id.action_nav_add_class_to_nav_class);
                    }
            }
        });

        //Back
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_class_to_nav_class);
            }
        });

//        fetchData();
        return view;
    }

    //datetime picker:
    private void showDateDialog(final EditText edt)
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                edt.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private boolean validateClassName(){
        String className1 = edtClassName.getText().toString().trim();
        if(className1.isEmpty()){
            tvClassName.setText("Please enter class name and less than 255 character");
            return false;
        } else {
            tvClassName.setText("");
            return true;
        }
    }

    private boolean validateCapacity(){
        String capacity1 = edtCapacity.getText().toString().trim();
        if(capacity1.isEmpty() || Integer.parseInt(capacity1)<=0){
            tvCapacity.setText("Please enter capacity is integer and larger than 0");
            return false;
        } else {
            tvCapacity.setText("");
            return true;
        }
    }

    private boolean validateStartDate(){
        SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        String now1 = sdf.format(now).toString();
        String start = edtStartDate.getText().toString().trim();
        if(start.isEmpty()){
            tvStartDate.setText("Please choose start date or fill mm/dd/yy");
            return false;
        }
        if(CheckDates(now1,start) == false){
            tvStartDate.setText("Please choose date after now date");
            return false;
        }else {
            tvStartDate.setText("");
            return true;
        }
    }

    private boolean validateEndDate(){
        SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        String now1 = sdf.format(now).toString();
        String start = edtStartDate.getText().toString().trim();
        String end = edtEndDate.getText().toString().trim();
        if(end.isEmpty()){
            tvEndDate.setText("Please choose start date or fill mm/dd/yy");
            return false;
        }
        if(CheckDates(now1,end) == false){
            tvEndDate.setText("Please choose date after now date");
            return false;
        }
        if (CheckDates(start,end) == false | start.equals(end)){
            tvEndDate.setText("Please choose end date after start date");
            return false;
        }else {
            tvEndDate.setText("");
            return true;
        }
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
                b = true;//If two dates are equal
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