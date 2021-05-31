package com.example.feedbackapplication.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.model.Item_ListFeedBackTrainee;

import java.util.List;

import com.example.feedbackapplication.R;

public class ListFeedBackAdapter extends RecyclerView.Adapter<ListFeedBackAdapter.ViewHolder> {
    private List<Item_ListFeedBackTrainee> loadItemListFeedBackTraineeList;
    public int a=0;

    public ListFeedBackAdapter(List ItemList) {
        loadItemListFeedBackTraineeList = ItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtFeedBackTitle;
        private final TextView txtClassID;
        private final TextView txtClassName;
        private final TextView txtModuleID;
        private final TextView txtModuleName;
        private final TextView txtEndTime;
        private final ImageButton EditQuestion;

        public ViewHolder(View view) {
            super(view);
            txtFeedBackTitle = (TextView) view.findViewById(R.id.txtFeedBackTitle);
            txtClassID = (TextView) view.findViewById(R.id.txtClassID);
            txtClassName = (TextView) view.findViewById(R.id.txtClassName);
            txtModuleID = (TextView) view.findViewById(R.id.txtModuleID);
            txtModuleName = (TextView) view.findViewById(R.id.txtModuleName);
            txtEndTime = (TextView) view.findViewById(R.id.txtEndTime);
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
        View ctView =
                //view_holder
                inflater.inflate(R.layout.feedback_item_traineelist, parent, false);

        ViewHolder viewHolder = new ViewHolder(ctView);
        a=0;
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
        Log.d("TAG", "oview ex:- "+x.gettxtEndTime());
        holder.txtFeedBackTitle.setText(x.gettxtFeedBackTitle());
        holder.txtModuleID.setText(x.gettxtModuleID());
        holder.txtModuleName.setText(x.gettxtModuleName());
//        holder.buttonRm.setText(x.getName2());
//        //--- DCM --
//
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                a = a + 1;
//                holder.buttonRm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        notifyDataSetChanged();
//                    }   // CC
//                });
//                holder.buttonRm.performClick();

                if(holder.txtClassID.getText()=="..." && holder.txtModuleID.getText()=="..." && holder.txtEndTime.getText()=="..."){
                  holder.EditQuestion.performClick();
                    Log.d("TAG", "remove somgthing:- "+holder.txtFeedBackTitle.getText());
                   // loadItemList.remove(position);
                }

                Log.d("TAGrun", "run: "+a);
            }
        };


        timerHandler.postDelayed(timerRunnable, 2000);

        //---



        // DO FEEDBACK HERE -----------------------------------------
        holder.EditQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadItemListFeedBackTraineeList.remove(position);
                Log.d("del", "onClick: "+ holder.txtFeedBackTitle.getText());
                notifyDataSetChanged();
            }
        });

//        if(holder.buttonRm.getText()=="..." || holder.desView.getText()=="..."){
//            holder.EditQuestion.performClick();
//            Log.d("TAG", "remove somgthing: "+holder.typeView.getText());
//            loadItemList.remove(position);
//        }

    }


    @Override
    public int getItemCount() {
        return loadItemListFeedBackTraineeList.size();
    }
}
