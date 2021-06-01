package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.model.Question;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuestionAdapter extends FirebaseRecyclerAdapter<Question, QuestionAdapter.MyViewHolder> {
    private QuestionAdapter.ClickListener clickListener;

    public QuestionAdapter(@NonNull FirebaseRecyclerOptions<Question> options, QuestionAdapter.ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionAdapter.MyViewHolder holder, int position, @NonNull Question model) {

        holder.txtTopicID.setText("Topic ID: " + model.getTopicID());
        holder.txtTopicName.setText("Topic Name: " + model.getTopicName());
        holder.txtQuestionID.setText("Question ID: " + model.getQuestionID());
        holder.txtQuestionContent.setText("Question Content: " + model.getQuestionContent());
        holder.flt_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClicked(model);
            }
        });

        holder.flt_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.updateClicked(model);
            }
        });

    }

    @NonNull
    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTopicID, txtTopicName, txtQuestionID, txtQuestionContent;
        FloatingActionButton flt_Delete, flt_Edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTopicID = itemView.findViewById(R.id.txtTopicID);
            txtTopicName = itemView.findViewById(R.id.txtTopicName);
            txtQuestionID = itemView.findViewById(R.id.txtQuestionID);
            txtQuestionContent = itemView.findViewById(R.id.txtQuestionContent);
            flt_Delete = itemView.findViewById(R.id.flt_Delete);
            flt_Edit = itemView.findViewById(R.id.flt_Edit);
        }
    }

    public interface ClickListener {
        void updateClicked(Question question);
        void deleteClicked(Question question);
    }
}
