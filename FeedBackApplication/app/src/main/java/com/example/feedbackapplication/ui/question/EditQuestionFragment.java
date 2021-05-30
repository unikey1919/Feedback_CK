package com.example.feedbackapplication.ui.question;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

public class EditQuestionFragment extends Fragment {
    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView topicN;
    private TextInputEditText questionContent;
    private int questionID, topicId = 0;
    private Question question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_question, container, false);
        questionContent = view.findViewById(R.id.edtQuestion);
        topicN = view.findViewById(R.id.editTopicName);

        questionContent.setText(getArguments().getString("questionContent"));
        topicN.setText(getArguments().getString("topicName"));
        questionID = getArguments().getInt("questionID");
        topicId = getArguments().getInt("topicID");

        //Take data to dropdown topicName
        database = FirebaseDatabase.getInstance().getReference("Topic");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        topicN.setAdapter(adapter);
        fetchData();

        //update
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(questionID);
                String key1 = String.valueOf(topicId);
                Map<String,Object> map = new HashMap<>();
                map.put("topicName",topicN.getText().toString().trim());
                map.put("questionContent",questionContent.getText().toString().trim());

                FirebaseDatabase.getInstance().getReference()
                        .child("Question")
                        .child(key1)
                        .child(key)
                        .updateChildren(map);
                Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
            }
        });
        btnBack = view.findViewById(R.id.btnBack1);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question);
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
}