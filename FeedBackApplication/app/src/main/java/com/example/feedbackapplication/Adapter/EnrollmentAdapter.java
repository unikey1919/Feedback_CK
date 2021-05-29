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

public class EnrollmentAdapter extends FirebaseRecyclerAdapter<Enrollment, EnrollmentAdapter.MyViewHolder> {
    private ClickListener clickListener;

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

        holder.txtTraineeID.setText("Trainee ID: " + model.getTrainee());
        holder.txtTraineeName.setText("Trainee Name: " + model.getTrainee());
        holder.txtClassID.setText("Class ID: " + model.getClassId());
        holder.txtClassName.setText("Class Name: " + model.getClassId());

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

    public interface ClickListener {
        void viewClicked(Enrollment enrollment);

        void updateClicked(Enrollment enrollment);

        void deleteClicked(Enrollment enrollment);
    }
}
