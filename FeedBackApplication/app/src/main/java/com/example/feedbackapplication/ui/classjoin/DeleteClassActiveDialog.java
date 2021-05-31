package com.example.feedbackapplication.ui.classjoin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.Adapter.ClassAdapter;
import com.example.feedbackapplication.LoginActivity;
import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class DeleteClassActiveDialog extends AppCompatDialogFragment {
    private Button btnYes,btnCancel;
    private int classID = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_class_active_dialog,null);

        btnCancel = view.findViewById(R.id.btnCancelAct);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnYes = view.findViewById(R.id.btnYesAct);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(classID);
                FirebaseDatabase.getInstance().getReference()
                        .child("Class")
                        .child(key)
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DeleteClassSuccessDialog delSuccess = new DeleteClassSuccessDialog();
                                delSuccess.show(getActivity().getSupportFragmentManager(),"deletesuccess");
                                dismiss();
                            }
                        });
            }
        });
        builder.setView(view);
        return builder.create();
    }

    //Gui du lieu qua tu EventBus
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true)
    public void onClassEvent2(Class cl) {
        classID = cl.getClassID();
    }

}
