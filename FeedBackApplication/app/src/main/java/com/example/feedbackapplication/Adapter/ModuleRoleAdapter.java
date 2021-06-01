package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ModuleRoleAdapter extends RecyclerView.Adapter<ModuleRoleAdapter.MyViewHolder> {

    Context context;
    ArrayList<Module> modules;

    public ModuleRoleAdapter(Context context, ArrayList<Module> modules) {
        this.context = context;
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleRoleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);

        return new ModuleRoleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Module module = modules.get(position);
        if(module == null){
            holder.txtModuleID.setText("ModuleID deleted, not found");
        }
        else {
            holder.txtModuleID.setText("Module ID: " + module.getModuleID());
            holder.txtModuleName.setText("Module Name: " + module.getModuleName());
            holder.txtAdminID.setText("Admin ID: " + module.getAdminID());
            holder.txtStartDate.setText("Start Date: " + module.getStartDate());
            holder.txtEndDate.setText("End Date: " + module.getEndDate() );
            holder.txtFeedBack.setText("Feedback Title: " + module.getFeedbackTitle());
            holder.txtFbStart.setText("Feedback StartTime: " + module.getFeedbackStartDate() );
            holder.txtFbEnd.setText("Feedback EndTime: " + module.getFeedbackEndDate() );
        }

    }


    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtModuleID, txtModuleName, txtAdminID, txtStartDate, txtEndDate, txtFeedBack, txtFbStart, txtFbEnd;
        ImageView edit;
        FloatingActionButton btnDelete,btnEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleID = itemView.findViewById(R.id.txtModuleID);
            txtModuleName = itemView.findViewById(R.id.txtModuleName);
            txtAdminID = itemView.findViewById(R.id.txtAdminID);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate= itemView.findViewById(R.id.txtEndDate);
            txtFeedBack = itemView.findViewById(R.id.txtFeedBack);
            txtFbStart= itemView.findViewById(R.id.txtFbStart);
            txtFbEnd= itemView.findViewById(R.id.txtFbEnd);
            edit = itemView.findViewById(R.id.btnEditModule);
            btnDelete = itemView.findViewById(R.id.btnDeleteModule);
            btnEdit = itemView.findViewById(R.id.btnEditModule);
            btnDelete.setVisibility(itemView.GONE);
            btnEdit.setVisibility(itemView.GONE);
        }
    }





}

