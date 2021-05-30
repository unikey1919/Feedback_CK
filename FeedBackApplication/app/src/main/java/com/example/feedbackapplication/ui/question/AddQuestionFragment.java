package com.example.feedbackapplication.ui.question;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.model.Question;
import com.google.android.material.textfield.TextInputEditText;
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
    private AutoCompleteTextView topicName, questionContent;
    private Question question;
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
        questionContent = view.findViewById(R.id.edtQuestion);
        TextView txtValidate = view.findViewById(R.id.txtValidate);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionContent == null){
                    txtValidate.setText("Please enter the question");
                } else {
                    int topicId = maxID + 1;
                    String topicN = topicName.getText().toString().trim();
                    int questionId = maxID + 1;
                    String content = questionContent.getText().toString().trim();

                    Question question = new Question(topicId, topicN, questionId,content);

                    reference.child(String.valueOf(questionId)).setValue(question);
                    Toast.makeText(v.getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
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
}