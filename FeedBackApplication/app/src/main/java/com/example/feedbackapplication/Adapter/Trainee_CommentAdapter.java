////not ready to use



package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.example.feedbackapplication.model.Trainee;
import com.example.feedbackapplication.model.Trainee_Comment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Trainee_CommentAdapter extends RecyclerView.Adapter<Trainee_CommentAdapter.MyViewHolder> {
    Context context;
    ArrayList<Trainee_Comment> comments;

    public Trainee_CommentAdapter(Context context, ArrayList<Trainee_Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public Trainee_CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);

        return new Trainee_CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trainee_Comment comment = comments.get(position);
        //boc dua len
        holder.txtNumber.setText(String.valueOf(position+1));
        holder.txtTraineeID.setText(String.valueOf(comment.getTraineeID()));
        holder.txtContent.setText(String.valueOf(comment.getComment()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtNumber, txtTraineeID, txtContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtTraineeID = itemView.findViewById(R.id.txtTraineeID);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }

}

