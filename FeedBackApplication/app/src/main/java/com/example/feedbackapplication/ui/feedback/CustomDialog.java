package com.example.feedbackapplication.ui.feedback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;

public class CustomDialog extends DialogFragment {
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtAddress;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_popup,null);
        builder.setView(view);
        TextView close = view.findViewById(R.id.buttonCloseCode);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMain(v);
            }
        });

        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);

        return builder.create();
    }

    public void goBackMain(View v) {
        Intent i = new Intent(v.getContext(), MainActivity.class);
        i.putExtra("role", "trainee");
        i.putExtra("username", "trainee");
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Button positive = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
//        positive.setTextColor(Color.parseColor("#669900"));
//        Button negative = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
//        negative.setTextColor(Color.parseColor("#669900"));
    }
}
