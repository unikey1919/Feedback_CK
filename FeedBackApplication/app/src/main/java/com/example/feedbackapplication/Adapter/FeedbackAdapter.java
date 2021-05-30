package com.example.feedbackapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.FeedBack;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedbackAdapter extends FirebaseRecyclerAdapter<FeedBack, FeedbackAdapter.MyViewHolder> {
    private FeedbackAdapter.ClickListener clickListener;

    public FeedbackAdapter(@NonNull FirebaseRecyclerOptions<FeedBack> options, FeedbackAdapter.ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull FeedbackAdapter.MyViewHolder holder, int position, @NonNull FeedBack model) {
        holder.txtFeedBackID.setText("Feedback ID: " + model.getFeedbackID());
        holder.txtTitle.setText("Title: " + model.getTitle());
        holder.txtAdminID.setText("Admin ID: " + model.getAdminID());
        holder.fltView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { clickListener.viewClicked(model);;
            }
        });
        holder.fltEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.updateClicked(model);
            }
        });

        holder.fltDelete.setOnClickListener(new View.OnClickListener() {
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
                .inflate(R.layout.feedback_item, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFeedBackID, txtTitle, txtAdminID;
        FloatingActionButton fltView, fltDelete, fltEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFeedBackID = itemView.findViewById(R.id.txtFeedbackID);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAdminID = itemView.findViewById(R.id.txtAdminID);
            fltView= itemView.findViewById(R.id.flt_View);
            fltEdit= itemView.findViewById(R.id.flt_Edit);
            fltDelete = itemView.findViewById(R.id.flt_Delete);
        }
    }

    public interface ClickListener{
        void viewClicked(FeedBack feedBack);
        void updateClicked(FeedBack feedBack);
        void deleteClicked(FeedBack feedBack);
    }
}
