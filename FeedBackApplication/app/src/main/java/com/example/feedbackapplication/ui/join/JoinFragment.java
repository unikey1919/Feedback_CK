package com.example.feedbackapplication.ui.join;

import android.app.Activity;
import android.content.Intent;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JoinFragment extends Fragment {

    private JoinViewModel mViewModel;
    public Activity c;
    public Dialog d;
    public TextView ok;
    private TextView submit, close;
    private EditText inputCode;
    static String user;

    static Integer aa;

    public static JoinFragment newInstance() {
        return new JoinFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDataFromDB();
        View root = inflater.inflate(R.layout.join_fragment, container, false);
        inputCode = root.findViewById(R.id.inputCode);
        submit = root.findViewById(R.id.buttonSubbmitCode);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCode customDialog = new PopupCode();
                Bundle bundle = new Bundle();
                customDialog.setArguments(bundle);
                getDataFromDB();
                String userCode = inputCode.getText().toString().trim();
//                                          Integer kq = checkCode(userCode);
//                                          Log.d("TAGGGGG", "userCode is ********* " + userCode + "**");
                if (userCode == null || userCode.equals("")) {
                    Log.d("TAG2", "userCode is *///*** " + userCode);
                    bundle.putString("kq", "Null");
                    customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                } else {
                    checkcode2(userCode);
//                    if (classidCompleteD == false) {
//                    } else {
//                        classidCompleteD = true;
//                        if (kq != null) {
//                            if (checkAlready(kq) == 1) { //tra cuu trung: 1 là ok
//                                bundle.putString("kq", "True");
//                                insert2database(kq);
//                                goBackMain(v);
//                            } else {
//                                bundle.putString("kq", "Invalid");
//                            }
//                        } else {
//                            bundle.putString("kq", "False");
//                            // Toast.makeText(getActivity(), "Code Sai " + userCode, Toast.LENGTH_LONG / 2).show();
//                        }
//                        customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
//                    }
                }
                //Toast.makeText(getActivity(), "Chua nhap code", Toast.LENGTH_LONG / 2).show();

                //
//                Log.d("TAG", "onClick: kqq: " + " -- " + kq);
//                Toast.makeText(getActivity(), "kqq: "+classid + " -- "+ kq, Toast.LENGTH_LONG ).show();
            }// onclick

        });

        close = root.findViewById(R.id.buttonCloseCode);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMain(v);
            }
        });
        return root;
        //return inflater.inflate(R.layout.join_fragment, container, false);
    }

    public void goBackMain(View v) {
        Intent i = new Intent(v.getContext(), MainActivity.class);
        i.putExtra("role", "trainee");
        i.putExtra("username", user);
        startActivity(i);
    }

    public void insert2database(Integer classid) {
        HashMap enr2 = new HashMap();
        enr2.put("traineeID", user);
        HashMap<String, Integer> enr3 = new HashMap<String, Integer>();
        enr3.put("classID", classid);

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
/////////////////////////////////////////////
    //////////////////////
    public void checkcode2(String userCode) {
//        Log.d("TAG22",  "cd "+userCode);
        Bundle bundle = new Bundle();
        PopupCode customDialog = new PopupCode();
        customDialog.setArguments(bundle);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference assignmentRef = rootRef.child("Assignment");
        Query codeQuery = assignmentRef.orderByChild("Code").equalTo(userCode);
        classid = null;
        codeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() == null) {   // code sai
                    bundle.putString("kq", "False");
                    customDialog.show(getActivity().getSupportFragmentManager(), "customDialog");
                } else {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Integer name = dataSnapshot.child("ClassID").getValue(Integer.class);
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
