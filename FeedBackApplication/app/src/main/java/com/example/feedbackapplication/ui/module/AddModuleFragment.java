package com.example.feedbackapplication.ui.module;

<<<<<<< HEAD
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
=======
>>>>>>> Ghi-danh
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddModuleFragment extends Fragment {
<<<<<<< HEAD
    private Button btnBack,btnAdd,btnSuccess;
    private DatabaseReference database,reference, databaseFb;
    private ValueEventListener listener, listener1;
    private ArrayList<String> list, listTitle;
    private ArrayAdapter<String> adapter, adapterTitle;
    private AutoCompleteTextView adminID, fbTitle ;
    private TextInputEditText moduleName, startDate, endDate, feedbackStartDate, feedbackEndDate;
    private TextInputLayout tilModuleName, tilStartDate, tilEndDate, tilFbStart, tilFbEnd;
=======
    private Button btnBack,btnAdd;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView adminID;
    private TextInputEditText moduleName, moduleID;
    private Module module;
>>>>>>> Ghi-danh
    private int maxID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_module, container, false);

        //Take data to dropdown adminID
        adminID = view.findViewById(R.id.actAdminID);
        database = FirebaseDatabase.getInstance().getReference("Admin");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        adminID.setAdapter(adapter);

        //database module
        reference = FirebaseDatabase.getInstance().getReference().child("Module");
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

        //Insert Data
        moduleName = view.findViewById(R.id.txt_ip_edt_ModuleName);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                if(!validateModuleName() | !validateStartDate() | !validateEndDate() | !validateFbStartDate() | !validateFbEndDate()){
                    return;
                }
                else {
                    String name = moduleName.getText().toString().trim();
                    String adminId = adminID.getText().toString().trim();
                    int moduleID = maxID + 1;
                    String start = startDate.getText().toString().trim();
                    String end = endDate.getText().toString().trim();
                    String fTitle = fbTitle.getText().toString().trim();
                    String fbStart = feedbackStartDate.getText().toString().trim();
                    String fbEnd = feedbackEndDate.getText().toString().trim();

                    Module module = new Module(moduleID,adminId,name,start,end,fTitle,fbStart,fbEnd);
                    reference.child(String.valueOf(moduleID)).setValue(module);
                    SuccessDialog();
                }
=======
                String name = moduleName.getText().toString().trim();
                String adminId = adminID.getText().toString().trim();
                int moduleID = maxID + 1;
                Module module = new Module(moduleID,adminId,name);
>>>>>>> Ghi-danh

                reference.child(String.valueOf(moduleID)).setValue(module);
                Toast.makeText(v.getContext(),"Add successfully" ,Toast.LENGTH_SHORT).show();
            }
        });

        //Back
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_to_nav_module);
            }
        });

        fetchData();
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
               adminID.setText(adapter.getItem(0),false);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
<<<<<<< HEAD

    public void fetchDataFbTitle(){
        listener1 = databaseFb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listTitle.add(dataSnapshot.child("Title").getValue().toString());
                }
                adapterTitle.notifyDataSetChanged();
                fbTitle.setText(adapterTitle.getItem(0),false);

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
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
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

    private void SuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loginfail_background));
        dialog.setCancelable(false);
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





=======
}
>>>>>>> Ghi-danh
