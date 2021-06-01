package com.example.feedbackapplication.Adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.feedbackapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.model.QuestionContent;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.SubItemViewHolder> {

    private List<QuestionContent> subItemList;

    QuestionAdapter(List<QuestionContent> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_question, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        QuestionContent subItem = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(subItem.getQuestion());

//        subItemViewHolder.radioButton1.setId(Integer.parseInt(subItem.getQuestionID()));
//        subItemViewHolder.radioButton2.setId(Integer.parseInt(subItem.getQuestionID()));
//        subItemViewHolder.radioButton3.setId(Integer.parseInt(subItem.getQuestionID()));
//        subItemViewHolder.radioButton4.setId(Integer.parseInt(subItem.getQuestionID()));
//        subItemViewHolder.radioButton5.setId(Integer.parseInt(subItem.getQuestionID()));


        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                subItemViewHolder.tvSubItemTitle.performClick();
                notifyDataSetChanged();
            }
        };

        timerHandler.postDelayed(timerRunnable, 1000);  // hold 0.5s to load firebase (load < 0.2s)

        subItemViewHolder.tvSubItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        RadioButton radioButton1;
        RadioButton radioButton2;
        RadioButton radioButton3;
        RadioButton radioButton4;
        RadioButton radioButton5;
        SubItemViewHolder(View itemView) {
            super(itemView);
            radioButton1 = itemView.findViewById(R.id.rad1);
            radioButton2 = itemView.findViewById(R.id.rad2);
            radioButton3 = itemView.findViewById(R.id.rad3);
            radioButton4 = itemView.findViewById(R.id.rad4);
            radioButton5 = itemView.findViewById(R.id.rad5);

            tvSubItemTitle = itemView.findViewById(R.id.txtview_feedback_question);
        }
    }
}
