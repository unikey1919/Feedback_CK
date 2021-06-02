package com.example.feedbackapplication.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.Trainee_CommentAdapter;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.model.Trainee_Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowDetailFragment extends Fragment {

    // for AutoCompleteTextView ClassName
    private final ArrayList<String> arrayListClassName = new ArrayList<>();
    // for AutoCompleteTextView ModuleName
    private final ArrayList<String> arrayListModuleName = new ArrayList<>();
    private ArrayAdapter<String> adapterClassName;
    private AutoCompleteTextView actClassName;
    private ArrayAdapter<String> adapterModuleName;
    private AutoCompleteTextView actModuleName;

    // for Trainee_Comment RecyclerView
    private ArrayList<Trainee_Comment> commentArrayList;
    private Trainee_CommentAdapter commentAdapter;
    private RecyclerView rcvComment;

    //bo 3 huy diet
    private ValueEventListener fetchDataForRCVComment;
    private ValueEventListener fetchDataForRCVComment2;
    private ValueEventListener fetchDataForRCVComment3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.show_detail_fragment, container, false);

        //getting views
        actClassName = root.findViewById(R.id.actClassName);
        actModuleName = root.findViewById(R.id.actModuleName);
        Button btnShowOverview = root.findViewById(R.id.btnShowOverview);
        Button btnViewComment = root.findViewById(R.id.btnViewComment);
        Button btnShowDetail = root.findViewById(R.id.btnShowDetail);

        //Take data to rcvComment

        //Take data to dropdown classID //done
        adapterClassName = new ArrayAdapter<>(getContext(), R.layout.option_item, arrayListClassName);
        fetchDataForACTVClassName();
        actClassName.setAdapter(adapterClassName);

        //Take data to dropdown moduleName //done
        adapterModuleName = new ArrayAdapter<>(getContext(), R.layout.option_item, arrayListModuleName);
        fetchDataForACTVModuleName();
        actModuleName.setAdapter(adapterModuleName);

        //btnShowOverview
        btnShowOverview.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_show_detail_to_nav_result));
        //btnViewComment
        btnViewComment.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_show_detail_to_nav_view_comment));
        //btnShowDetail //dang o day
        btnShowDetail.setOnClickListener(v -> {
            //current Names
            String class_Name = String.valueOf(actClassName.getText());
            String module_Name = String.valueOf(actModuleName.getText());

            Toast.makeText(getActivity(), "test_result", Toast.LENGTH_LONG).show();
        });

        return root;
    }

    //for className dropdown //done
    public void fetchDataForACTVClassName() {
        FirebaseDatabase.getInstance().getReference("Class")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListClassName.add("All");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Class temp = dataSnapshot.getValue(Class.class);
                            if (temp != null)
                                arrayListClassName.add(temp.getClassName());
                        }
                        adapterClassName.notifyDataSetChanged();
                        actClassName.setText(adapterClassName.getItem(0), false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    //for moduleName dropdown //done
    public void fetchDataForACTVModuleName() {
        FirebaseDatabase.getInstance().getReference("Module")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListModuleName.add("All");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot != null) {
                                Module temp = dataSnapshot.getValue(Module.class);
                                if (temp != null)
                                    arrayListModuleName.add(temp.getModuleName());
                            }
                        }
                        adapterModuleName.notifyDataSetChanged();
                        actModuleName.setText(adapterClassName.getItem(0), false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    ////for rcvComment
    //tep1: find moduleID in Module
    private void fetchDataForRCVComment(String module_Name, String class_Name) {
        //query id cua new_module_Name
        commentArrayList = new ArrayList<>(); //cho list comment ve empty
        if (module_Name.equals("All")) {
            fetchDataForRCVComment2(0, class_Name);
        } else {
            fetchDataForRCVComment = FirebaseDatabase.getInstance().getReference("Module")
                    .orderByChild("moduleName").equalTo(module_Name).limitToFirst(1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Module temp = dataSnapshot.getValue(Module.class);
                                        if (temp != null) {
                                            int temp1 = temp.getModuleID();
                                            if (temp1 != 0) {
                                                ////found module_ID
                                                flag = false;
                                                killFetch("Module", fetchDataForRCVComment);
                                                fetchDataForRCVComment2(temp1, class_Name);
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no module with moduleName //Fetching ends
                                killFetch("Module", fetchDataForRCVComment);
                                upDateRCVComment();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //step2: find classID in Class
    private void fetchDataForRCVComment2(int module_ID, String class_Name) {
        //query id cua new_module_Name
        if (class_Name.equals("All")) {
            fetchDataForRCVComment3(module_ID, 0);
        } else {
            fetchDataForRCVComment2 = FirebaseDatabase.getInstance().getReference("Class")
                    .orderByChild("className").equalTo(class_Name).limitToFirst(1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Class temp = dataSnapshot.getValue(Class.class);
                                        if (temp != null) {
                                            int temp1 = temp.getClassID();
                                            if (temp1 != 0) {
                                                ////found class_ID
                                                flag = false;
                                                killFetch("Class", fetchDataForRCVComment2);
                                                fetchDataForRCVComment3(module_ID, temp1);
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no module with moduleName//Fetching ends
                                killFetch("Class", fetchDataForRCVComment2);
                                upDateRCVComment();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //step3: get list Trainee_Comment
    private void fetchDataForRCVComment3(int module_ID, int class_ID) {
        //query id cua new_module_Name
        if (class_ID == 0) {
            fetchDataForRCVComment3 = FirebaseDatabase.getInstance().getReference("Trainee_Comment")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Trainee_Comment temp = dataSnapshot.getValue(Trainee_Comment.class);
                                        if (temp != null) {
                                            if (module_ID == 0) {
                                                ////found Trainee_Comment with className=All and moduleName=All //Fetching ends
                                                flag = false;
                                                killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                                commentArrayList.add(temp);
                                                upDateRCVComment();
                                            } else {
                                                int temp1 = temp.getModuleID();
                                                if (temp1 != 0 && temp1 == module_ID) {
                                                    ////found Trainee_Comment with className=ALl and moduleID=module_ID //Fetching ends
                                                    flag = false;
                                                    killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                                    commentArrayList.add(temp);
                                                    upDateRCVComment();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no Trainee_Comment with className=All and moduleName=All //Fetching ends
                                killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                upDateRCVComment();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } else {
            fetchDataForRCVComment3 = FirebaseDatabase.getInstance().getReference("Trainee_Comment")
                    .orderByChild("classID").equalTo(class_ID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Trainee_Comment temp = dataSnapshot.getValue(Trainee_Comment.class);
                                        if (temp != null) {
                                            if (module_ID == 0) {
                                                ////found Trainee_Comment with classID=class_ID and moduleName=All //Fetching ends
                                                flag = false;
                                                killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                                commentArrayList.add(temp);
                                                upDateRCVComment();
                                            } else {
                                                int temp1 = temp.getModuleID();
                                                if (temp1 != 0 && temp1 == module_ID) {
                                                    ////found Trainee_Comment with classID=class_ID and moduleID=module_ID //Fetching ends
                                                    flag = false;
                                                    killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                                    commentArrayList.add(temp);
                                                    upDateRCVComment();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no Trainee_Comment with classID=class_ID and moduleName=All //Fetching ends
                                killFetch("Trainee_Comment", fetchDataForRCVComment3);
                                upDateRCVComment();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //kill any ValueEventListener //done
    private void killFetch(String path, ValueEventListener valueEventListener) {
        if (valueEventListener != null)
            FirebaseDatabase.getInstance().getReference(path).removeEventListener(valueEventListener);
    }

    //update RecyclerView contains comments
    private void upDateRCVComment() {
        commentAdapter = new Trainee_CommentAdapter(
                getContext(),
                commentArrayList);
        rcvComment.setAdapter(commentAdapter);
        Toast.makeText(getActivity(), "commentArrayList: " + commentArrayList.size(), Toast.LENGTH_LONG).show();
    }
}