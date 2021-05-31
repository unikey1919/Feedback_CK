package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClassAdapter extends FirebaseRecyclerAdapter<Class, ClassAdapter.MyViewHolder> {
    private ClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClassAdapter(@NonNull FirebaseRecyclerOptions<Class> options, ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassAdapter.MyViewHolder holder, int position, @NonNull Class cl) {

        holder.txtClassID.setText("Class ID: " + cl.getClassID());
        holder.txtClassName.setText("Class Name: " + cl.getClassName());
        holder.txtCapacity.setText("Capacity: " + cl.getCapacity());
        holder.txtStartDate.setText("Start Date: " + cl.getStartDate());
        holder.txtEndDate.setText("End Date: " + cl.getEndDate());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.updateClicked(cl);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClicked(cl);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassID, txtClassName, txtCapacity, txtStartDate, txtEndDate;
        FloatingActionButton btnDelete, btnEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassID = itemView.findViewById(R.id.txtClassID);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtCapacity = itemView.findViewById(R.id.txtCapacity);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate= itemView.findViewById(R.id.txtEndDate);
            btnEdit = itemView.findViewById(R.id.btnEditClass);
            btnDelete = itemView.findViewById(R.id.btnDeleteClass);
        }
    }

    public interface ClickListener{
        void updateClicked(Class class1);
        void deleteClicked(Class class1);
    }
}
