package com.example.feedbackapplication.Adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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

public class ClassAdapter extends FirebaseRecyclerAdapter<Class, ClassAdapter.MyViewHolder> {
    private ClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClassAdapter(@NonNull FirebaseRecyclerOptions<Class> options, ClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassAdapter.MyViewHolder holder, int position, @NonNull Class cl) {
        //khai bao 2 dinh dang
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        //dinh dang Class ID
        final SpannableStringBuilder sb = new SpannableStringBuilder("Class ID: ");
        sb.append(String.valueOf(cl.getClassID()));
        sb.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtClassID.setText(sb);

        //dinh dang Class Name
        final SpannableStringBuilder sb1 = new SpannableStringBuilder("Class Name: ");
        sb1.append(cl.getClassName());
        sb1.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb1.setSpan(bss, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtClassName.setText(sb1);

        //dinh dang Capacity
        final SpannableStringBuilder sb2 = new SpannableStringBuilder("Capacity: ");
        sb2.append(String.valueOf( cl.getCapacity()));
        sb2.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb2.setSpan(bss, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtCapacity.setText(sb2);

        //dinh dang Start Date
        final SpannableStringBuilder sb3 = new SpannableStringBuilder("Start Date: ");
        sb3.append(cl.getStartDate());
        sb3.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb3.setSpan(bss, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtClassID.setText(sb3);

        //dinh dang End Date
        final SpannableStringBuilder sb4 = new SpannableStringBuilder("End Date: ");
        sb4.append(cl.getEndDate());
        sb4.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb4.setSpan(bss, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.txtClassID.setText(sb4);

        //xu ly su kien click button Edit
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.updateClicked(cl);

            }
        });

        //xu ly su kien click button Delete
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
                .inflate(R.layout.class_item, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassID, txtClassName, txtCapacity, txtStartDate, txtEndDate;
        FloatingActionButton btnDelete, btnEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassID = itemView.findViewById(R.id.txtClassID);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtCapacity = itemView.findViewById(R.id.txtCapacity);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate= itemView.findViewById(R.id.txtEndDate);
            btnEdit = itemView.findViewById(R.id.btnEditClass);
            btnDelete = itemView.findViewById(R.id.btnDeleteClass);
        }
    }

    public interface ClickListener{
        void updateClicked(Class class1);
        void deleteClicked(Class class1);
    }
}
