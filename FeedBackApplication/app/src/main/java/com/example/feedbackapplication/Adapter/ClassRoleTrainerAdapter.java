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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClassRoleTrainerAdapter extends FirebaseRecyclerAdapter<Class, ClassRoleTrainerAdapter.MyViewHolder> {
    private ClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClassRoleTrainerAdapter(@NonNull FirebaseRecyclerOptions<Class> options, ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassRoleTrainerAdapter.MyViewHolder holder, int position, @NonNull Class cl) {

//        holder.txtClassID.setText("Class ID: " + cl.getClassID());
//        holder.txtClassName.setText("Class Name: " + cl.getClassName());
//        holder.txtCapacity.setText("Capacity: " + cl.getCapacity());
//        holder.txtStartDate.setText("Start Date: " + cl.getStartDate());
//        holder.txtEndDate.setText("End Date: " + cl.getEndDate());

        //get class name
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
                .inflate(R.layout.trainer_class_item, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassID, txtClassName, txtNumber;
        FloatingActionButton btnSee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassID = itemView.findViewById(R.id.txtClassID);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtNumber = itemView.findViewById(R.id.txtNumberofTrainee;
            btnSee = itemView.findViewById(R.id.btnSee);
        }
    }

    public interface ClickListener{
        void updateClicked(Class class1);
        void deleteClicked(Class class1);
    }
}
