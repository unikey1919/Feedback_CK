package com.example.feedbackapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.ui.feedback.DoFeedback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnLogin;
    private RecyclerView rcv_Category;
    static String role ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        getDataFromDB();
        if(role.equals("null"))
        {
            root = inflater.inflate(R.layout.fragment_home, container, false);
            btnLogin = root.findViewById(R.id.btnLoginHome);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        if(role.equals("admin"))
        {
            root = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
        }
        if(role.equals("trainer"))
        {
            root = inflater.inflate(R.layout.fragment_dashboard_trainer, container, false);
        }
        if(role.equals("trainee"))
        {
            root = inflater.inflate(R.layout.fragment_dashboard_trainee, container, false);
        }

        return root;
    }

    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        role = results.getString("val1");
    }
}