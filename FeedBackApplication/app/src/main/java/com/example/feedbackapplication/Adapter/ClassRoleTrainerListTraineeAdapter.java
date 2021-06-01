package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Enrollment;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassRoleTrainerListTraineeAdapter extends RecyclerView.Adapter<ClassRoleTrainerListTraineeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Enrollment> enrollments;

    public ClassRoleTrainerListTraineeAdapter(Context context, ArrayList<Enrollment> enrollments) {
        this.context = context;
        this.enrollments = enrollments;
    }

    @NonNull
    @Override
    public ClassRoleTrainerListTraineeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trainer_trainee_list_item, parent, false);

        return new ClassRoleTrainerListTraineeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);
        int position1 = position + 1;
        //dinh dang position
        final SpannableStringBuilder sb = new SpannableStringBuilder("Number: ");
        sb.append(String.valueOf(position1));
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtNumber.setText(sb);

        //dinh dang traineeID
        final SpannableStringBuilder sb1 = new SpannableStringBuilder("Trainee ID: ");
        sb1.append(String.valueOf(enrollment.getTraineeID()));
        sb1.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb1.setSpan(bss, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtTraineeID.setText(sb1);

        //get trainee name
        DatabaseReference refTrainee = FirebaseDatabase.getInstance().getReference().child("Trainee");
        Query query1 = refTrainee.orderByChild("UserName").equalTo(enrollment.getTraineeID());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //dinh dang traineeName
                    final SpannableStringBuilder sb3 = new SpannableStringBuilder("Trainee Name: ");
                    sb3.append(dataSnapshot.child("Name").getValue().toString());
                    final ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.rgb(0, 0, 0));
                    final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
                    sb3.setSpan(fcs1, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb3.setSpan(bss1, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    holder.txtTraineeName.setText(sb3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtNumber, txtTraineeID, txtTraineeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTraineeID = itemView.findViewById(R.id.txtTraineeID);
            txtTraineeName = itemView.findViewById(R.id.txtTraineeName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
        }
    }


}

