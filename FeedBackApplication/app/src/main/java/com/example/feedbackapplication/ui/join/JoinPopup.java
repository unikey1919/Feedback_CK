package com.example.feedbackapplication.ui.join;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JoinPopup extends DialogFragment {

    private JoinViewModel mViewModel;
    public Activity c;
    public Dialog d;
    public TextView ok;
    private TextView submit, close;
    private EditText inputCode;
    static String user;


    @Override
    public void onStart() {
        super.onStart();
        Button positive = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setTextColor(Color.parseColor("#669900"));
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        getDataFromDB();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_popup,null);
        builder.setView(view);
        TextView close = view.findViewById(R.id.buttonCloseCode);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMain(v);
            }
        });
//
        inputCode = view.findViewById(R.id.inputCode);
        TextView submit = view.findViewById(R.id.buttonSubbmitCode);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCode customDialog = new PopupCode();
                Bundle bundle = new Bundle();
                customDialog.setArguments(bundle);
                String userCode = inputCode.getText().toString().trim();
                if (userCode == null || userCode.equals("")) {
                    Log.d("TAG2", "userCode is *///*** " + userCode);
                    bundle.putString("kq", "Null");
                    customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                } else {
                    checkcode2(userCode);
                }
            }
        });

        close = view.findViewById(R.id.buttonCloseCode);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMain(v);
            }
        });

        return builder.create();
    }

    public void goBackMain(View v) {
        Intent i = new Intent(v.getContext(), MainActivity.class);
        i.putExtra("role", "trainee");
        i.putExtra("username", user);
        i.putExtra("join", "b");
        Log.d("TAG", "goBackMain: 9999999 " + user);
        startActivity(i);
    }

    public void insert2database(Integer classid) {
        HashMap enr2 = new HashMap();
        enr2.put("traineeID", user);
        HashMap<String, Integer> enr3 = new HashMap<String, Integer>();
        enr3.put("classID", classid);
        enr3.put("status", 0);

        enr3.putAll(enr2);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("Enrollment").push();
        Log.d("TAGGGGG", "insert2database: " + classid.toString() + user);

        tasksRef.setValue(enr3);

    }

    public Integer resulf = null;
    public boolean resulfCompletED = false;


    public Integer classid = null;
    public boolean classidCompleteD = false;

    public void checkcode2(String userCode) {
//        Log.d("TAG22",  "cd "+userCode);
        Bundle bundle = new Bundle();
        PopupCode customDialog = new PopupCode();
        customDialog.setArguments(bundle);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference assignmentRef = rootRef.child("Assignment");
        Query codeQuery = assignmentRef.orderByChild("code").equalTo(userCode);
        classid = null;
        codeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() == null) {   // code sai
                    bundle.putString("kq", "False");
                    customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                } else {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Integer name = dataSnapshot.child("classID").getValue(Integer.class);
                        Log.d("TAG22", name.toString());
                        classid = name;
                        if (classid != null) {
                            checkAlready2(classid);
                            break;
                        }
                    }
                }

                Log.d("TAG22", "all in 1 " + ds.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("TAG22", "After attaching listener " + classid);
    }

    private void checkAlready2(Integer classid) {
        Bundle bundle = new Bundle();
        PopupCode customDialog = new PopupCode();
        customDialog.setArguments(bundle);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference assignmentRef = rootRef.child("Enrollment");
        Query codeQuery = assignmentRef.orderByChild("traineeID").equalTo(user);
        resulf = 1;
        codeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                int dem = 0;
                if( ds.getValue()==null){
                    Log.d("TAG_let", "INSERTTTTTT" +  classid.toString());
                    bundle.putString("kq", "True");
                    insert2database(classid);
                    customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                }
                for (DataSnapshot dataSnapshot : ds.getChildren()) {
                    Integer classFound = dataSnapshot.child("classID").getValue(Integer.class);
                    Log.d("TAGx", "classFound:-- " + ds.getChildrenCount());
                    Log.d("TAGx", "classFound:++ " + ds);
                    if (classid == classFound) {
                        bundle.putString("kq", "Invalie");
                        customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                        break;
                    } else {
                        dem++;
                        if (ds.getChildrenCount() == dem ) {
                            Log.d("TAG_let", "INSERTTTTTT" +  classid.toString());
                            bundle.putString("kq", "True");
                            insert2database(classid);
                            customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                            break;
                        }
                    }
                }
                Log.d("TAG22", "all" + ds + classid.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }


    public void getDataFromDB() {
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        user = results.getString("username");
    }
}
