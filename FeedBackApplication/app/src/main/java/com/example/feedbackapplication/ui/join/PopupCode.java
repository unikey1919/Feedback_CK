package com.example.feedbackapplication.ui.join;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;

public class PopupCode extends AppCompatDialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getDataFromDB();

        Bundle bundle = getArguments();
        String kq = bundle.getString("kq","");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_notifi_code,null);

        ImageView img = view.findViewById(R.id.image_notifi);
        TextView txt = view.findViewById(R.id.txt_notifi);
        if (kq.equals("True")){
            img.setImageResource(R.drawable.ic_check);
            txt.setText("Join Success!");
        }
        else if (kq.equals("False")){
            txt.setText("Invalid Registation code!!!");
            img.setImageResource(R.drawable.ic_deny);
        }
        else if (kq.equals("Null")){
            txt.setText("Input Null!!!");
            img.setImageResource(R.drawable.ic_deny);
        }
        else {
            txt.setText("Dax tham gia!!!");
            img.setImageResource(R.drawable.ic_deny);
        }
        builder.setView(view);

        Button btn_addCategory = view.findViewById(R.id.btnOk);
        btn_addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMain(v);
            }
        });
        return builder.create();
    }

    public void goBackMain(View v){
        Intent i = new Intent(v.getContext(), MainActivity.class);
        i.putExtra("role", "trainee");
        i.putExtra("username",user );
        startActivity(i);
    }
    static String user;
    public void getDataFromDB(){
        MainActivity activity = (MainActivity) getActivity();
        Bundle results = activity.getMyData();
        user = results.getString("username");
    }
}
