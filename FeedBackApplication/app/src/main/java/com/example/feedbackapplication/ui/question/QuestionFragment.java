package com.example.feedbackapplication.ui.question;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
=======
>>>>>>> Ghi-danh

import com.example.feedbackapplication.R;

public class QuestionFragment extends Fragment {

    private QuestionViewModel mViewModel;
<<<<<<< HEAD
    private QuestionAdapter adapter;
    private RecyclerView rcvQuestion;
    private DatabaseReference database;
    private ArrayList<String> list;
    private ValueEventListener listener;
    private ArrayAdapter<String> adapter1;
    private AutoCompleteTextView topicN;
    private FloatingActionButton btnNew;
    private Button btnCancelAct, btnYesAct;

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
=======
>>>>>>> Ghi-danh

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_fragment, container, false);
    }

    @Override
<<<<<<< HEAD
    public void updateClicked(Question question) {
        Bundle bundle = new Bundle();
        bundle.putInt("topicId",question.getTopicID());
        bundle.putString("topicName",question.getTopicName());
        bundle.putInt("questionId",question.getQuestionID());
        bundle.putString("questionContent",question.getQuestionContent());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_question_to_nav_edit_question,bundle);
    }

    public void deleteClicked(Question question) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_question_active_dialog);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(false);
        btnCancelAct = dialog.findViewById(R.id.btnCancelAct);
        btnYesAct = dialog.findViewById(R.id.btnYesAct);
        btnCancelAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYesAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(question.getQuestionID());
                FirebaseDatabase.getInstance().getReference()
                        .child("Question")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }

    public void fetchData(){
        listener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
=======
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        // TODO: Use the ViewModel
>>>>>>> Ghi-danh
    }

}