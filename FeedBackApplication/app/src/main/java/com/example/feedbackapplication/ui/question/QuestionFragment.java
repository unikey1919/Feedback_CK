package com.example.feedbackapplication.ui.question;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.Adapter.QuestionAdapter;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.model.Question;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionFragment extends Fragment implements QuestionAdapter.ClickListener {

    private QuestionViewModel mViewModel;
    private QuestionAdapter adapter;
    private RecyclerView rcvQuestion;
    private DatabaseReference database;
    private ArrayList<String> list;
    private ValueEventListener listener;
    private ArrayAdapter<String> adapter1;
    private AutoCompleteTextView topicN;
    private FloatingActionButton btnNew;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.question_fragment, container, false);

        //Take data to dropdown topicName
        topicN = root.findViewById(R.id.actTopic);
        database = FirebaseDatabase.getInstance().getReference("Topic");
        list = new ArrayList<String>();
        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.option_item,list);
        topicN.setAdapter(adapter1);

        DatabaseReference database1= FirebaseDatabase.getInstance().getReference().child("Question");
        rcvQuestion = root.findViewById(R.id.rcvQuestion);
        rcvQuestion.setHasFixedSize(true);
        rcvQuestion.setLayoutManager(new LinearLayoutManager(root.getContext()));
        //Retrieve data
        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(database1, Question.class)
                        .build();
        adapter = new QuestionAdapter(options,this);
        rcvQuestion.setAdapter(adapter);

        //Save data
        btnNew = root.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_question_to_nav_add_question);
            }
        });
        fetchData();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void updateClicked(Question question) {
        Bundle bundle = new Bundle();
        bundle.putInt("topicId",question.getTopicID());
        bundle.putString("topicName",question.getTopicName());
        bundle.putInt("questionId",question.getQuestionID());
        bundle.putString("questionContent",question.getQuestionContent());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_question_to_nav_edit_question,bundle);
    }

    public void deleteClicked(Question question) {
        String key = String.valueOf(question.getQuestionID());
        FirebaseDatabase.getInstance().getReference()
                .child("Question")
                .child(key)
                .setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                    }
                });

    }
// ram het nen debug hoi lau nha
    public void fetchData(){
        listener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                adapter1.notifyDataSetChanged();
//                topicN.setText(adapter1.getItem(0),false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}