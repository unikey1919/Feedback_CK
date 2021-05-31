package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Assignment;
import com.example.feedbackapplication.model.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AssignmentAdapter extends FirebaseRecyclerAdapter<Assignment, AssignmentAdapter.MyViewHolder> {
    private ClickListener clickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AssignmentAdapter(@NonNull FirebaseRecyclerOptions<Assignment> options, ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtNo.setText("No:" + position);
        holder.txtTrainerName.setText("Trainer Name: " + model.getTrainerID());
        holder.txtCode.setText("Registration Code: " + model.getCode());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_item, parent, false);

        return new AssignmentAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtModuleName, txtClassName, txtTrainerName, txtCode, txtNo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleName = itemView.findViewById(R.id.txtModuleName);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtTrainerName = itemView.findViewById(R.id.txtTrainerName);
            txtCode = itemView.findViewById(R.id.txtCode);
            txtNo = itemView.findViewById(R.id.txtNo);
        }
    }

    public interface ClickListener{
        void updateClicked(Module module);
        void deleteClicked(Module module);
    }


}
