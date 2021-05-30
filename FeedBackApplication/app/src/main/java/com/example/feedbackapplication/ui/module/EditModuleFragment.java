package com.example.feedbackapplication.ui.module;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditModuleFragment extends Fragment {

    private Button btnBack,btnSave;
    private DatabaseReference database,reference, databaseFb;
    private ValueEventListener listener, listener1;
    private ArrayList<String> list, listTitle;
    private ArrayAdapter<String> adapter, adapterTitle;
    private AutoCompleteTextView adminID, fbTitle ;
    private TextInputEditText moduleName, startDate, endDate, feedbackStartDate, feedbackEndDate;
    private int moduleID = 0;
    private Module module;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_module, container, false);

        //init
        moduleName = view.findViewById(R.id.txt_ip_edt_ModuleName);
        adminID = view.findViewById(R.id.actAdminID);
        startDate = view.findViewById(R.id.txt_ip_edt_startDate);
        endDate = view.findViewById(R.id.txt_ip_edt_endDate);
        feedbackEndDate = view.findViewById(R.id.txt_ip_edt_FbEnd);
        feedbackStartDate = view.findViewById(R.id.txt_ip_edt_FbStart);
        fbTitle = view.findViewById(R.id.actFeedBack);

        moduleName.setText(getArguments().getString("name"));
        adminID.setText(getArguments().getString("adminID"));
        moduleID = getArguments().getInt("id");
        startDate.setText(getArguments().getString("startDate"));
        endDate.setText(getArguments().getString("endDate"));
        fbTitle.setText(getArguments().getString("feedbackTitle"));
        feedbackStartDate.setText(getArguments().getString("feedbackStartDate"));
        feedbackEndDate.setText(getArguments().getString("feedbackEndDate"));

        showStartDate(startDate);
        showStartDate(endDate);
        showStartDate(feedbackEndDate);
        showStartDate(feedbackStartDate);


        //Take data to dropdown adminID
        database = FirebaseDatabase.getInstance().getReference("Admin");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        adminID.setAdapter(adapter);
        fetchData();

        //Take data to dropdown fbTitle
        databaseFb = FirebaseDatabase.getInstance().getReference("Feedback");
        listTitle = new ArrayList<String>();
        adapterTitle = new ArrayAdapter<>(getActivity(),R.layout.option_item,listTitle);
        fbTitle.setAdapter(adapterTitle);
        fetchDataFbTitle();

        //update
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(moduleID);
                Map<String,Object> map = new HashMap<>();
                map.put("moduleName",moduleName.getText().toString().trim());
                map.put("adminID",adminID.getText().toString().trim());
                map.put("endDate",endDate.getText().toString().trim());
                map.put("startDate",startDate.getText().toString().trim());
                map.put("feedbackEndDate",feedbackEndDate.getText().toString().trim());
                map.put("feedbackStartDate",feedbackStartDate.getText().toString().trim());
                map.put("feedbackTitle",fbTitle.getText().toString().trim());


                FirebaseDatabase.getInstance().getReference()
                        .child("Module")
                        .child(key)
                        .updateChildren(map);
                Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void fetchData(){
        listener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchDataFbTitle(){
        listener1 = databaseFb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listTitle.add(dataSnapshot.child("Title").getValue().toString());
                }
                adapterTitle.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showStartDate(final TextInputEditText date ){

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (startDate.getRight() - startDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        final Calendar calendar = Calendar.getInstance();
                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR,year);
                                calendar.set(Calendar.MONTH,month);
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" MM/dd/yyyy");
                                date.setText(simpleDateFormat.format(calendar.getTime()));

                            }
                        };
                        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}