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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BetterEnrollmentAdapter extends RecyclerView.Adapter<BetterEnrollmentAdapter.MyViewHolder> {
    private final BetterEnrollmentAdapter.ClickListener clickListener;
    Context context;
    ArrayList<Enrollment> enrollments;

    public BetterEnrollmentAdapter(ClickListener clickListener, Context context, ArrayList<Enrollment> enrollments) {
        this.clickListener = clickListener;
        this.context = context;
        this.enrollments = enrollments;
    }

    @NonNull
    @Override
    public BetterEnrollmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enrollment_item, parent, false);

        return new BetterEnrollmentAdapter.MyViewHolder(view);
    }

    public interface ClickListener {
        void viewClicked(Enrollment enrollment);

        void updateClicked(Enrollment enrollment);

        void deleteClicked(Enrollment enrollment);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);

        FirebaseDatabase.getInstance().getReference("Trainee")
                .orderByChild("UserName").equalTo(enrollment.getTraineeID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot != null) {
                                String temp = String.valueOf(dataSnapshot.child("Name").getValue(String.class));
                                holder.txtTraineeName.setText(temp);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("classID").equalTo(enrollment.getClassID()).limitToFirst(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot != null) {
                                String temp = String.valueOf(dataSnapshot.child("className").getValue(String.class));
                                holder.txtClassName.setText(temp);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        holder.txtTraineeID.setText(String.valueOf(enrollment.getTraineeID()));
        holder.txtClassID.setText(String.valueOf(enrollment.getClassID()));
        holder.btnView.setOnClickListener(v -> clickListener.viewClicked(enrollment));
        holder.btnEdit.setOnClickListener(v -> clickListener.updateClicked(enrollment));
        holder.btnDelete.setOnClickListener(v -> clickListener.deleteClicked(enrollment));
    }

    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtClassID, txtClassName, txtTraineeID, txtTraineeName;
        FloatingActionButton btnView;
        FloatingActionButton btnEdit;
        FloatingActionButton btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTraineeID = itemView.findViewById(R.id.txtTraineeID);
            txtTraineeName = itemView.findViewById(R.id.txtTraineeName);
            txtClassID = itemView.findViewById(R.id.txtClassID);
            txtClassName = itemView.findViewById(R.id.txtClassName);

            btnView = itemView.findViewById(R.id.btnViewEnrollment);
            btnEdit = itemView.findViewById(R.id.btnEditEnrollment);
            btnDelete = itemView.findViewById(R.id.btnDeleteEnrollment);
        }
    }

}

