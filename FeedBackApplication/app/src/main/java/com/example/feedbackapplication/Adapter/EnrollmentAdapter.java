package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Enrollment;
import com.example.feedbackapplication.model.Trainee;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EnrollmentAdapter extends FirebaseRecyclerAdapter<Enrollment, EnrollmentAdapter.MyViewHolder> {
    public List<String> classNameList;
    public List<String> traineeNameList;
    private ClickListener clickListener;
    private List<com.example.feedbackapplication.model.Class> classList;
    private List<com.example.feedbackapplication.model.Trainee> traineeList;

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

    public void fetchData() {
        FirebaseDatabase.getInstance().getReference("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classNameList = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Class temp = dataSnapshot.getValue(Class.class);
                    if (temp != null) {
                        //classList.add(temp);
                        //classNameList.add(temp.getClassName());
                        String x = temp.getClassName();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference("Trainee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                traineeList = null;
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        Trainee temp = new Trainee();
                        temp.setUserName(dataSnapshot.child("UserName").getValue(String.class));
                        temp.setName(dataSnapshot.child("Name").getValue(String.class));
                        //traineeNameList.add(temp.getName());
                        //traineeList.add(i++,temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String fetchClassName(int id) {
        String className = "no_info";
        if (classList != null) {
            for (com.example.feedbackapplication.model.Class class_desu : classList) {
                if (class_desu.getClassID() == id) {
                    return class_desu.getClassName();
                }
            }
        }
        return className;
    }

    public String fetchTraineeName(String id) {
        String traineeName = "no_info";
        if (traineeList != null) {
            for (Trainee trainee : traineeList) {
                if (trainee.getUserName().equals(id)) {
                    return trainee.getName();
                }
            }
        }
        return traineeName;
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
                                String temp = dataSnapshot.child("Name").getValue(String.class);
                                if (temp != null) {
                                    temp = "Trainee Name: " + temp;
                                    holder.txtTraineeName.setText(temp);
                                }
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
                                String temp = dataSnapshot.child("className").getValue(String.class);
                                if (temp != null) {
                                    temp = "Class Name: " + temp;
                                    holder.txtClassName.setText(temp);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        holder.txtTraineeID.setText("Trainee ID: " + model.getTraineeID());
        holder.txtClassID.setText("Class ID: " + model.getClassID());

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
}
