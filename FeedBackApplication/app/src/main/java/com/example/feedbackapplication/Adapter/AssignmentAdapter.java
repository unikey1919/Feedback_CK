package com.example.feedbackapplication.Adapter;

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
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AssignmentAdapter extends FirebaseRecyclerAdapter<Assignment, AssignmentAdapter.MyViewHolder>  {
    private ClickListener clickListener;
    private String role;
    private String className, moduleName, key;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AssignmentAdapter(@NonNull FirebaseRecyclerOptions<Assignment> options, ClickListener clickListener,String role) {
        super(options);
        this.clickListener = clickListener;
        this.role = role;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Assignment model) {
        //get module name
        DatabaseReference refModule = FirebaseDatabase.getInstance().getReference().child("Module");
        Query query = refModule.orderByChild("moduleID").equalTo(model.getModuleID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    holder.txtModuleName.setText("Module Name: " + dataSnapshot.child("moduleName").getValue().toString());
                    moduleName = dataSnapshot.child("moduleName").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //get class name
        DatabaseReference refClass = FirebaseDatabase.getInstance().getReference().child("Class");
        Query query1 = refClass.orderByChild("classID").equalTo(model.getClassID());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    holder.txtClassName.setText("Class Name: " + dataSnapshot.child("className").getValue().toString());
                    className = dataSnapshot.child("className").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtNo.setText("No:" + position);
        holder.txtTrainerName.setText("Trainer Name: " + model.getTrainerID());
        holder.txtCode.setText( model.getCode());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key =getRef(position).getKey();
                clickListener.updateClicked(model,moduleName
                        ,className,key);
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_item, parent, false);

        return new AssignmentAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtModuleName, txtClassName, txtTrainerName, txtCode, txtNo, txtCode1;
        FloatingActionButton btnEdit, btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleName = itemView.findViewById(R.id.txtModuleName);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtTrainerName = itemView.findViewById(R.id.txtTrainerName);
            txtCode = itemView.findViewById(R.id.txtCode1);
            txtNo = itemView.findViewById(R.id.txtNo);
            btnEdit = itemView.findViewById(R.id.btnEditAssignment);
            btnDelete = itemView.findViewById(R.id.btnDeleteAssignment);

            if(role.equals("trainer")){
                btnDelete.setVisibility(itemView.GONE);
                btnEdit.setVisibility(itemView.GONE);
            }
        }
    }

    public interface ClickListener{
        void updateClicked(Assignment model,String moduleName,String className,String position);
        void deleteClicked(Assignment model);
    }


}