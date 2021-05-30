package com.example.feedbackapplication.ui.feedback;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Button;
import android.widget.ImageButton;

import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFragment extends Fragment {

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

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        btnLogin = root.findViewById(R.id.btnLoginHome);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

//        ImageButton doFeedback = root.findViewById(R.id.imageButton);
//        doFeedback = root.findViewById(R.id.imageButton);
//        doFeedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DoFeedback doFeedback1 = new DoFeedback();
//                doFeedback1.show(getActivity().getSupportFragmentManager(),"show"); //add
//
//            }
//        });

        return  root;
    }

}