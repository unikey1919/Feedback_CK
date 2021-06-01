package com.example.feedbackapplication.ui.assignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Module;
import com.google.android.material.textfield.TextInputEditText;

public class EditAssignmentFragment extends Fragment {
    private TextInputEditText classID,className, moduleID, moduleName;
    private Button btnAdd, btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_assignment, container, false);

        //init
        classID = root.findViewById(R.id.txt_ip_edt_ClassID);
        moduleID = root.findViewById(R.id.txt_ip_edt_ModuleID);
        className = root.findViewById(R.id.txt_ip_edt_ClassName);
        moduleName = root.findViewById(R.id.txt_ip_edt_ModuleName);

        //on screen

        classID.setText(Integer.toString(getArguments().getInt("classID")));
        moduleID.setText(Integer.toString(getArguments().getInt("moduleID")));
        className.setText(getArguments().getString("className"));
        moduleName.setText(getArguments().getString("moduleName"));


        return root;
    }
}