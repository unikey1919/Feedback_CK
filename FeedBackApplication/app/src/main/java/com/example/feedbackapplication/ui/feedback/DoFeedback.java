package com.example.feedbackapplication.ui.feedback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.feedbackapplication.R;

//public class DoFeedback extends AppCompatDialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.trainee_do_feedback,null);
//        builder.setView(view);
//        return builder.create();
//    }
//}

public class DoFeedback extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trainee_do_feedback,null);
        builder.setView(view);
//        Dialog x = builder.create();
        Toast.makeText(getActivity(), "xxxxx",
                Toast.LENGTH_LONG/2).show();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme2);
        return builder.create();
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.trainee_do_feedback, container, false);
//setContentView(R.layout.trainee_do_feedback);
//        return view;
//    }
}
