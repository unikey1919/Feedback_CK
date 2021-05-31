package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Enrollment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EnrollmentAdapter extends FirebaseRecyclerAdapter<Enrollment, EnrollmentAdapter.MyViewHolder> {
    private final ClickListener clickListener;
    public List<String> classNameList;
    public List<String> traineeNameList;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EnrollmentAdapter(@NonNull FirebaseRecyclerOptions<Enrollment> options, ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Enrollment model) {
        FirebaseDatabase.getInstance().getReference("Trainee")
                .orderByChild("UserName").equalTo(model.getTraineeID()).limitToFirst(1)
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
                .orderByChild("classID").equalTo(model.getClassID()).limitToFirst(1)
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
        holder.txtTraineeID.setText(String.valueOf(model.getTraineeID()));
        holder.txtClassID.setText(String.valueOf(model.getClassID()));
        holder.btnView.setOnClickListener(v -> clickListener.viewClicked(model));
        holder.btnEdit.setOnClickListener(v -> clickListener.updateClicked(model));
        holder.btnDelete.setOnClickListener(v -> clickListener.deleteClicked(model));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enrollment_item, parent, false);

        return new MyViewHolder(view);
    }

    public interface ClickListener {
        void viewClicked(Enrollment enrollment);

        void updateClicked(Enrollment enrollment);

        void deleteClicked(Enrollment enrollment);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassID, txtClassName, txtTraineeID, txtTraineeName;
        //ImageView edit;
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