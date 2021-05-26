package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ModuleAdapter extends FirebaseRecyclerAdapter<Module, ModuleAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ModuleAdapter(@NonNull FirebaseRecyclerOptions<Module> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ModuleAdapter.MyViewHolder holder, int position, @NonNull Module model) {

        holder.txtModuleID.setText("Module ID: " + model.getModuleID());
        holder.txtModuleName.setText("Module Name: " + model.getModuleName());
        holder.txtAdminID.setText("Admin ID: " + model.getAdminID());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtModuleID, txtModuleName, txtAdminID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleID = itemView.findViewById(R.id.txtModuleID);
            txtModuleName = itemView.findViewById(R.id.txtModuleName);
            txtAdminID = itemView.findViewById(R.id.txtAdminID);
        }
    }
}
