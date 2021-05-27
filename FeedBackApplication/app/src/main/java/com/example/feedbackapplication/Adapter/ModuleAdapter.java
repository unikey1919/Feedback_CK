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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ModuleAdapter extends FirebaseRecyclerAdapter<Module, ModuleAdapter.MyViewHolder> {
    private ClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ModuleAdapter(@NonNull FirebaseRecyclerOptions<Module> options,ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ModuleAdapter.MyViewHolder holder, int position, @NonNull Module model) {

        holder.txtModuleID.setText("Module ID: " + model.getModuleID());
        holder.txtModuleName.setText("Module Name: " + model.getModuleName());
        holder.txtAdminID.setText("Admin ID: " + model.getAdminID());
//        holder.txtStartDate.setText("Start Date: " );
//        holder.txtEndDate.setText("End Date: " );
//        holder.txtFeedBack.setText("Feedback Title: " );
//        holder.txtFbStart.setText("Feedback StartTime: " );
//        holder.txtFbEnd.setText("Feedback EndTime: " );
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.updateClicked(model);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClicked(model);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtModuleID, txtModuleName, txtAdminID, txtStartDate, txtEndDate, txtFeedBack, txtFbStart, txtFbEnd;
        ImageView edit;
        FloatingActionButton btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleID = itemView.findViewById(R.id.txtModuleID);
            txtModuleName = itemView.findViewById(R.id.txtModuleName);
            txtAdminID = itemView.findViewById(R.id.txtAdminID);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate= itemView.findViewById(R.id.txtEndDate);
            txtFeedBack = itemView.findViewById(R.id.txtFeedBack);
            txtFbStart= itemView.findViewById(R.id.txtFbStart);
            txtEndDate= itemView.findViewById(R.id.txtFbEnd);
            edit = itemView.findViewById(R.id.btnEditModule);
            btnDelete = itemView.findViewById(R.id.btnDeleteModule);
        }
    }

    public interface ClickListener{
        void updateClicked(Module module);
        void deleteClicked(Module module);
    }
}
