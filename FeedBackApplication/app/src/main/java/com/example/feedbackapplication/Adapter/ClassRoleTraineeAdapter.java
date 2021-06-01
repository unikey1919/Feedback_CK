package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassRoleTraineeAdapter extends RecyclerView.Adapter<ClassRoleTraineeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Enrollment> enrollments;
    private ClassRoleTrainerListTraineeAdapter roleAdapter;
    private ClickListener clickListener;

    public ClassRoleTraineeAdapter(Context context, ArrayList<Enrollment> enrollments, ClickListener clickListener) {
        this.context = context;
        this.enrollments = enrollments;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ClassRoleTraineeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trainer_class_item, parent, false);

        return new ClassRoleTraineeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);
        //dinh dang tvClassID
        final SpannableStringBuilder sb = new SpannableStringBuilder("Class ID: ");
        sb.append(String.valueOf(enrollment.getClassID()));
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtClassID.setText(sb);

        //get class name
        DatabaseReference refClass = FirebaseDatabase.getInstance().getReference().child("Class");
        Query query1 = refClass.orderByChild("classID").equalTo(enrollment.getClassID());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //dinh dang class name
                    final SpannableStringBuilder sb1 = new SpannableStringBuilder("Class Name: ");
                    sb1.append(dataSnapshot.child("className").getValue().toString());
                    sb1.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb1.setSpan(bss, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    holder.txtClassName.setText(sb1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //get number of trainee
        DatabaseReference refEnroll = FirebaseDatabase.getInstance().getReference().child("Enrollment");
        Query query = refEnroll.orderByChild("classID").equalTo(enrollment.getClassID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //dinh dang number of trainee
                final SpannableStringBuilder sb2 = new SpannableStringBuilder("Number of Trainee: ");
                sb2.append(String.valueOf(snapshot.getChildrenCount()));
                final ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.rgb(0, 0, 0));
                final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
                sb2.setSpan(fcs1, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb2.setSpan(bss1, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.txtNumber.setText(sb2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //xu ly khi click button See
        holder.btnSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.see1Clicked(enrollment);
            }
        });

    }


    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtClassID, txtClassName, txtNumber;
        FloatingActionButton btnSee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassID = itemView.findViewById(R.id.txtClassID);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtNumber = itemView.findViewById(R.id.txtNumberofTrainee);
            btnSee = itemView.findViewById(R.id.btnSee);
        }
    }

    public interface ClickListener{
        void see1Clicked(Enrollment enrollment);
    }
}

