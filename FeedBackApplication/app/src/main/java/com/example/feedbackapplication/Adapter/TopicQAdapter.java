package com.example.feedbackapplication.Adapter;
import com.example.feedbackapplication.R;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.model.QuestionContent;
import com.example.feedbackapplication.model.QuestionTopic;

import java.util.List;

public class TopicQAdapter extends RecyclerView.Adapter<TopicQAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<QuestionTopic> itemList;

    public TopicQAdapter(List<QuestionTopic> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_topicq, viewGroup, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        QuestionTopic item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(item.getTopic());
        Log.d("waiting", "I got: "+item.getTopic());
        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getQuestion().size());

        // Create sub item view adapter
        QuestionAdapter subItemAdapter = new QuestionAdapter(item.getQuestion());

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);

//        itemViewHolder.tvItemTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notifyDataSetChanged();
//                Log.d("waiting", "run: waiting data");
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.txtview_feedback_topic);
            rvSubItem = itemView.findViewById(R.id.rv_question);
        }
    }
}
