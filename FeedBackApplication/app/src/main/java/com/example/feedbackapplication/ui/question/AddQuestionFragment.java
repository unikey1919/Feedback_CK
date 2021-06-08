package com.example.feedbackapplication.ui.question;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Question;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddQuestionFragment extends Fragment {

    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView topicName;
    private TextInputLayout inputQuestion;
    private Question question;
    private TextInputEditText questionContent;
    private int maxID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);

        //Take data to dropdown topicID
        topicName = view.findViewById(R.id.editTopicName);
        database = FirebaseDatabase.getInstance().getReference("Topic");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        topicName.setAdapter(adapter);

        //database question
        reference = FirebaseDatabase.getInstance().getReference().child("Question");
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
        inputQuestion = view.findViewById(R.id.edtContent);
        questionContent = view.findViewById(R.id.edtQuestion);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! validate()){
                    return;
                } else {
                    int topicId = maxID + 1;
                    String topicN = topicName.getText().toString().trim();
                    int questionId = maxID + 1;
                    String content = questionContent.getText().toString().trim();

                    Question question = new Question(topicId, topicN, questionId,content);

                    reference.child(String.valueOf(questionId)).setValue(question);
                    Toast.makeText(v.getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                    SuccessDialog(Gravity.CENTER);
                    Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question);
                }
            }
        });

        //Back
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question);
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
                topicName.setText(adapter.getItem(0),false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SuccessDialog(int gravity){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_success);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        Button btnAddSuccess = dialog.findViewById(R.id.btnAddSuccess);
        btnAddSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean validate(){
        String question = inputQuestion.getEditText().getText().toString().trim();
        if(question.isEmpty()){
            inputQuestion.setError("Please enter the question");
            return false;
        } else {
            inputQuestion.setError(null);
            return true;
        }
    }
}