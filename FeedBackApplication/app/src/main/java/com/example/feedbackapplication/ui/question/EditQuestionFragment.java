package com.example.feedbackapplication.ui.question;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.model.Question;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditQuestionFragment extends Fragment {
    private static final String TAG = "EditQuestionFragment";
    private Button btnBack,btnSave;
    private DatabaseReference database,reference;
    private ValueEventListener listener;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView topicN;
    private AutoCompleteTextView questionContent;
    private TextInputLayout inputQuestion;
    private int questionID, topicId;
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
        questionID = getArguments().getInt("questionId");
        topicId = getArguments().getInt("topicId");

        //update
        inputQuestion = view.findViewById(R.id.edtContent);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! validate()) {
                    return;
                } else {
                    String key = String.valueOf(questionID);
                    String key1 = String.valueOf(topicId);
                    Map<String,Object> map = new HashMap<>();
                    map.put("topicName",topicN.getText().toString().trim());
                    map.put("questionContent",questionContent.getText().toString().trim());

                    FirebaseDatabase.getInstance().getReference()
                            .child("Question")
                            .child(key)
                            .updateChildren(map);
                    Toast.makeText(v.getContext(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                    SuccessDialog(Gravity.CENTER);
                    Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question);
                }

            }
        });
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question);
            }
        });
        return view;
    }

    private void SuccessDialog(int gravity){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_success);
        TextView textView = dialog.findViewById(R.id.textViewSuccess);
        textView.setText("Edit question success");

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