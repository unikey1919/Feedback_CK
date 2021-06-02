package com.example.feedbackapplication.ui.feedback;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.Adapter.QuestionAdapter;
import com.example.feedbackapplication.Adapter.TopicQAdapter;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.QuestionContent;
import com.example.feedbackapplication.model.QuestionTopic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
//public class DoFeedback extends AppCompatDialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.trainee_do_feedback,null);
//        builder.setView(view);
//        return builder.create();
//    }
//}

public class DoFeedback extends Fragment {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<QuestionContent> getquesExample() {
        List<QuestionContent> subItemList = new ArrayList<>();
        for (int i=0; i<2; i++) {
            QuestionContent subItem = new QuestionContent("Câu hỏi "+i, String.valueOf(i) );
            subItemList.add(subItem);
        }
        return subItemList;
    }

    private List<QuestionTopic> getTopicExample() {    // (Work well)
        List<QuestionTopic> itemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuestionTopic item = new QuestionTopic("Topic " + i, getquesExample());
            itemList.add(item);
        }
        return  itemList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                               @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.trainee_do_feedback, container, false);
        /// phần cũ ------------
        String ModuleID= getArguments().getString("modID");
        String ClassId= getArguments().getString("claID");
        String User= getArguments().getString("user");
        String modName= getArguments().getString("modName");
        String claName= getArguments().getString("claName");
        TextView username = root.findViewById(R.id.name_user);
        TextView txtModuleName = root.findViewById(R.id.txtModuleName);
        TextView textClassname = root.findViewById(R.id.textClassname);

        txtModuleName.setText(modName);
        textClassname.setText(claName);
        username.setText(User);

        // phần mới (test):
        RecyclerView rvItem = root.findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());//Integer.parseInt(ModuleID)

        gettopic(Integer.parseInt(ModuleID), rvItem, layoutManager);

        TopicQAdapter itemAdapter =new TopicQAdapter(getTopicExample());
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);


        return root;
    }
    static ArrayList<String> ques;
    static ArrayList<String> topi;

    public List<QuestionTopic> getEverythingElse(Integer quesID,List<QuestionTopic> itemList)
    {
        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference FeedQuesRef = rootRef.child("Question");
        Query codeQuery = FeedQuesRef.orderByChild("questionID").equalTo(quesID);
        ques = new ArrayList<>();
        topi = new ArrayList<>();

        codeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() != null) {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        String questionContent = dataSnapshot.child("questionContent").getValue(String.class); // co question content
                        Integer topicID = dataSnapshot.child("topicID").getValue(Integer.class); // co topic id
                        String topicName = dataSnapshot.child("topicName").getValue(String.class); // co  topic content
                        ques.add(questionContent);
                        topi.add(topicName);
                        Log.d("TAG", "onDataChange: ques "+ques);
                        Log.d("TAG", "onDataChange: topi"+topi);

                        Log.d("lamon", "ques content d:----------- " +questionContent+ " " +topicID+ " "+topicName);
                    }
                    // tien hanh add:
                    List<QuestionTopic> itemList = new ArrayList<>();
                    for(int i=0; i<ques.size(); i++)
                    {
                        ArrayList<String> X = new ArrayList<>();
                        List<QuestionContent> itemListc = new ArrayList<>();
                        for(int j=0; j<ques.size(); j++)
                        {
                           if(topi.get(i)==topi.get(j)) {
                               X.add(ques.get(j));
                               QuestionContent subItem = new QuestionContent(ques.get(j), String.valueOf(i) );
                               itemListc.add(subItem);
                           }
                            QuestionTopic Item = new QuestionTopic(topi.get(i), itemListc);
                            itemList.add(Item);
//                           itemList.add(topi.get(i),)
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return itemList;
    }

    private List<QuestionTopic> gettopic(int ModuleID, RecyclerView rvItem, LinearLayoutManager layoutManager) {
        List<QuestionTopic> itemList = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference FeedQuesRef = rootRef.child("Feedback_Question");
        Query codeQuery = FeedQuesRef.orderByChild("ModuleID").equalTo(ModuleID);      //
        codeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                if (ds.getValue() != null) {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Integer QuestionID = dataSnapshot.child("QuestionID").getValue(Integer.class); // da co question id
                        TopicQAdapter itemAdapter =new TopicQAdapter(getEverythingElse(QuestionID,itemList));
                        Log.d("fdf", "onDataChange: -------"+ itemAdapter);
                        rvItem.setAdapter(itemAdapter);
                        rvItem.setLayoutManager(layoutManager);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return itemList;
    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
