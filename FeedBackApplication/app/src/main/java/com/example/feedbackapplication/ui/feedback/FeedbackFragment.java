package com.example.feedbackapplication.ui.feedback;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
<<<<<<< HEAD
import android.widget.TextView;
import android.widget.Toast;
=======
>>>>>>> Ghi-danh

import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class FeedbackFragment extends Fragment implements FeedbackAdapter.ClickListener {

    private FeedbackViewModel myViewModel;
    private FeedbackAdapter adapter;
    private RecyclerView rcvFeedback;
    private DatabaseReference database;
    private ArrayList<FeedBack> arrayList;
    private FloatingActionButton btnInsert;
    private Button btnCancelAct, btnYesAct;
=======
public class FeedbackFragment extends Fragment {
>>>>>>> Ghi-danh

    private FeedbackViewModel mViewModel;
    private RecyclerView rcv_feedback;
    private HomeViewModel homeViewModel;
    private Button btnLogin;
    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.feedback_fragment, container, false);
        return root;
    }

<<<<<<< HEAD
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void viewClicked(FeedBack feedBack) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",feedBack.getFeedbackID());
        bundle.putString("title",feedBack.getTitle());
        bundle.putString("adminID",feedBack.getAdminID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_feedback_to_nav_detail_feedback,bundle);
    }

    @Override
    public void updateClicked(FeedBack feedBack) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",feedBack.getFeedbackID());
        bundle.putString("title",feedBack.getTitle());
        bundle.putString("adminID",feedBack.getAdminID());
        Navigation.findNavController(getView()).navigate(R.id.action_nav_feedback_to_nav_edit_feedback,bundle);
    }

    @Override
    public void deleteClicked(FeedBack feedBack) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_question_active_dialog);
        TextView textView = dialog.findViewById(R.id.textViewAUSure1);
        textView.setText("Do you want to delete this item?");

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(false);
        btnCancelAct = dialog.findViewById(R.id.btnCancelAct);
        btnYesAct = dialog.findViewById(R.id.btnYesAct);
        btnCancelAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYesAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(feedBack.getFeedbackID());
                FirebaseDatabase.getInstance().getReference()
                        .child("Feedback")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(),"Update successfully" ,Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }
=======
>>>>>>> Ghi-danh
}