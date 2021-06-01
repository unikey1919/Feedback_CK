package com.example.feedbackapplication.ui.module;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditModuleFragment extends Fragment {

    private Button btnBack,btnSave, btnSuccess;
    private DatabaseReference database,reference, databaseFb;
    private ValueEventListener listener, listener1;
    private ArrayList<String> list, listTitle;
    private ArrayAdapter<String> adapter, adapterTitle;
    private AutoCompleteTextView adminID, fbTitle ;
    private TextInputEditText moduleName, startDate, endDate, feedbackStartDate, feedbackEndDate;
    private TextInputLayout tilModuleName, tilStartDate, tilEndDate, tilFbStart, tilFbEnd;
    private int moduleID = 0;
    private Module module;
    private TextView txt;

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

        //validate
        tilModuleName = view.findViewById(R.id.txt_ip_ModuleName);
        tilStartDate = view.findViewById(R.id.txt_ip_Start);
        tilEndDate = view.findViewById(R.id.txt_ip_End);
        tilFbStart = view.findViewById(R.id.txt_ip_FbStart);
        tilFbEnd = view.findViewById(R.id.txt_ip_FbEnd);


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
                if(!validateModuleName() | !validateStartDate() | !validateEndDate() | !validateFbStartDate() | !validateFbEndDate()){
                    return;
                }
                else {
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
                    SuccessDialog(v);
                }
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_to_nav_module);
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

    private boolean validateModuleName(){
        String name = tilModuleName.getEditText().getText().toString().trim();
        tilModuleName.setErrorIconDrawable(null);
        if(name.isEmpty()){
            tilModuleName.setError("Please enter module name and less than 255");
            return false;
        } else {
            tilModuleName.setError(null);
            return true;
        }
    }

    private boolean validateStartDate(){
        String start = tilStartDate.getEditText().getText().toString().trim();
        tilStartDate.setErrorIconDrawable(null);
        if(start.isEmpty()){
            tilStartDate.setError("Please choose start date or fill mm/dd/yy");
            return false;
        } else {
            tilStartDate.setError(null);
            return true;
        }
    }

    private boolean validateEndDate(){
        String start = tilStartDate.getEditText().getText().toString().trim();
        String end = tilEndDate.getEditText().getText().toString().trim();
        tilEndDate.setErrorIconDrawable(null);
        if(end.isEmpty()){
            tilEndDate.setError("Please choose start date or fill mm/dd/yy");
            return false;
        }
        if (CheckDates(start,end) == false){
            tilEndDate.setError("Please choose end date after start date");
            return false;
        }else {
            tilEndDate.setError(null);
            return true;
        }
    }

    private boolean validateFbStartDate(){
        String start = tilFbStart.getEditText().getText().toString().trim();
        tilFbStart.setErrorIconDrawable(null);
        if(start.isEmpty()){
            tilFbStart.setError("Please choose start date or fill mm/dd/yy");
            return false;
        } else {
            tilFbStart.setError(null);
            return true;
        }
    }

    private boolean validateFbEndDate(){
        String end = tilFbEnd.getEditText().getText().toString().trim();
        String start = tilFbStart.getEditText().getText().toString().trim();
        tilFbEnd.setErrorIconDrawable(null);
        if(end.isEmpty()){
            tilFbEnd.setError("Please choose start date or fill mm/dd/yy");
            return false;
        }
        if (CheckDates(start,end) == false){
            tilFbEnd.setError("Please choose end date after start date");
            return false;
        }else {
            tilFbEnd.setError(null);
            return true;
        }
    }

    static SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

    public static boolean CheckDates(String d1, String d2) {
        boolean b = false;
        try {
            if (sdf.parse(d1).before(sdf.parse(d2))) {
                b = true;//If start date is before end date
            } else if (sdf.parse(d1).equals(sdf.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    private void SuccessDialog(View view) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
        txt = dialog.findViewById(R.id.txt);
        txt.setText("Edit Success!");
        btnSuccess = dialog.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Navigation.findNavController(view).navigate(R.id.action_nav_edit_to_nav_module);
            }
        });
        dialog.show();
    }
}