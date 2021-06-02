package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.model.Item_ListFeedBackTrainee;

import java.util.List;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.ui.feedback.ShowFeedBack_trainee;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ListFeedBackAdapter extends RecyclerView.Adapter<ListFeedBackAdapter.ViewHolder> {
    private ClickListener_doFeedBack clickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     */
    public ListFeedBackAdapter(List ItemList, ListFeedBackAdapter.ClickListener_doFeedBack clickListener) {
        loadItemListFeedBackTraineeList = ItemList;
        this.clickListener = clickListener;
    }


    private List<Item_ListFeedBackTrainee> loadItemListFeedBackTraineeList;
    public int a = 0;

//    public ListFeedBackAdapter(List ItemList) {
//        loadItemListFeedBackTraineeList = ItemList;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtFeedBackTitle;
        private final TextView txtClassID;
        private final TextView txtClassName;
        private final TextView txtModuleID;
        private final TextView txtModuleName;
        private final TextView txtEndTime;
        private final TextView txtStatus;
        private final ImageButton EditQuestion;

        public ViewHolder(View view) {
            super(view);
            txtFeedBackTitle = (TextView) view.findViewById(R.id.txtFeedBackTitle);
            txtClassID = (TextView) view.findViewById(R.id.txtClassID);
            txtClassName = (TextView) view.findViewById(R.id.txtClassName);
            txtModuleID = (TextView) view.findViewById(R.id.txtModuleID);
            txtModuleName = (TextView) view.findViewById(R.id.txtModuleName);
            txtEndTime = (TextView) view.findViewById(R.id.txtEndTime);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            EditQuestion = (ImageButton) view.findViewById(R.id.btnEditQuestion);

        }

        public TextView gettxtFeedBackTitle() {
            return txtFeedBackTitle;
        }

        public TextView gettxtClassID() {
            return txtClassID;
        }

        public TextView gettxtClassName() {
            return txtClassName;
        }

        public TextView gettxtModuleID() {
            return txtModuleID;
        }

        public TextView gettxtModuleName() {
            return txtModuleName;
        }

        public TextView gettxtEndTime() {
            return txtEndTime;
        }

        public ImageButton EditQuestion() {
            return EditQuestion;
        }
    }

    @NonNull
    @Override
    public ListFeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Nạp layout cho View
        View ctView = inflater.inflate(R.layout.feedback_item_traineelist, parent, false);

        ViewHolder viewHolder = new ViewHolder(ctView);
        a = 0;
        return viewHolder;
        // return null;
    }

    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    @Override
    public void onBindViewHolder(@NonNull ListFeedBackAdapter.ViewHolder holder, int position) {
        Item_ListFeedBackTrainee x = loadItemListFeedBackTraineeList.get(position);
        holder.txtClassID.setText(x.gettxtClassID());
        holder.txtClassName.setText(x.gettxtClassName());
        holder.txtEndTime.setText(x.gettxtEndTime());
        holder.txtFeedBackTitle.setText(x.gettxtFeedBackTitle());
        holder.txtModuleID.setText( x.gettxtModuleID());
        holder.txtModuleName.setText(x.gettxtModuleName());
        if(x.gettxtModuleID().equals("...")){}
        else
            holder.txtModuleID.setText("Module ID: " +x.gettxtModuleID());

//        Log.d("okelalala", "load item....:" + x.gettxtModuleID());
//        //--- DCM --
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                if(holder.txtClassID.getText()=="..." && holder.txtEndTime.getText()=="...")
                    holder.txtClassID.performClick();
                notifyDataSetChanged();
            }
        };
        timerHandler.postDelayed(timerRunnable, 1500);  // hold 0.5s to load firebase (load < 0.2s)

        holder.txtStatus.setText(x.getImage());

        if (x.getImage().equals("Status: Completed")) {
            holder.EditQuestion.setEnabled(false);
            holder.EditQuestion.setVisibility(View.INVISIBLE);
        }
//            holder.txtStatus.setText("Status: Complete");
//            holder.EditQuestion.setVisibility(View.INVISIBLE);
//        }

//        if (holder.txtClassID.getText() == "..." && holder.txtModuleID.getText() == "..." && holder.txtEndTime.getText() == "...") {
//            holder.txtStatus.setText("...");
//            holder.EditQuestion.setVisibility(View.INVISIBLE);
//        } else
//        if (x.getImage() == "1") {
//            holder.txtStatus.setText("Status: Complete");
//            holder.EditQuestion.setVisibility(View.INVISIBLE);
//        } else {
//            holder.txtStatus.setText("Status: InComplete");
//            holder.EditQuestion.setVisibility(View.VISIBLE);
//        }

        // DO FEEDBACK HERE -----------------------------------------
        holder.EditQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.txtClassID.getText()=="..." && holder.txtEndTime.getText()=="...") {
                    loadItemListFeedBackTraineeList.remove(position);
                    Log.d("delll", "onClick: " + holder.txtFeedBackTitle.getText());
                    notifyDataSetChanged();
                }else {
                    clickListener.updateClicked(x);
                }
            }
        });

        holder.txtClassID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.txtClassID.getText()=="..." && holder.txtEndTime.getText()=="...") {
                    loadItemListFeedBackTraineeList.remove(position);
                    notifyDataSetChanged();
                    Log.d("okelalala", "remove...." + holder.txtFeedBackTitle.getText());

                }
            }
        });
    }

    public interface ClickListener_doFeedBack {
        void updateClicked(Item_ListFeedBackTrainee item);
    }


    @Override
    public int getItemCount() {
        return loadItemListFeedBackTraineeList.size();
    }
}
