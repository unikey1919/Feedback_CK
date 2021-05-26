package com.example.feedbackapplication.ui.join;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;

public class JoinFragment extends Fragment {

    private JoinViewModel mViewModel;
    public Activity c;
    public Dialog d;
    public TextView ok;
    private TextView submit, close;

    public static JoinFragment newInstance() {
        return new JoinFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.join_fragment, container, false);
        submit = root.findViewById(R.id.buttonSubbmitCode);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Show popup resulf",
                        Toast.LENGTH_LONG/2).show();

                PopupCode customDialog = new PopupCode();
                customDialog.show(getActivity().getSupportFragmentManager(),"customDialog");
            }
        });

        close = root.findViewById(R.id.buttonCloseCode);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),MainActivity.class);
                i.putExtra("role","trainee");
                startActivity(i);
            }
        });
        return root;
        //return inflater.inflate(R.layout.join_fragment, container, false);
    }


}