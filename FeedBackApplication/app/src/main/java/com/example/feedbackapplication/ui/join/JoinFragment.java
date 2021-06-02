package com.example.feedbackapplication.ui.join;

import android.app.Activity;
import android.content.Intent;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JoinFragment extends Fragment {

    private JoinViewModel mViewModel;
    public Activity c;
    public Dialog d;
    public TextView ok;
    private TextView submit, close;
    private EditText inputCode;
    static String user;

    static Integer aa;

    public static JoinFragment newInstance() {
        return new JoinFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromDB();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDataFromDB();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Intent i = new Intent(root.getContext(), MainActivity.class);
        i.putExtra("role", "trainee");
        i.putExtra("username", user);
        i.putExtra("join", "a");
        startActivity(i);
        return root;
    }

    public void getDataFromDB() {
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        user = results.getString("username");
    }
}
