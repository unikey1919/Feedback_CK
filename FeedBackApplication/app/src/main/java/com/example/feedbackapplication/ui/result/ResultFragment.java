package com.example.feedbackapplication.ui.result;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.feedbackapplication.R;
import com.example.feedbackapplication.model.Answer;
import com.example.feedbackapplication.model.Class;
import com.example.feedbackapplication.model.Module;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultFragment extends Fragment implements OnChartValueSelectedListener {

    // for AutoCompleteTextView ClassName
    private final ArrayList<String> arrayListClassName = new ArrayList<>();
    // for AutoCompleteTextView ModuleName
    private final ArrayList<String> arrayListModuleName = new ArrayList<>();
    //pie chart info
    String[] info = {"not this plz","Strongly Disagree", "Disagree", "Neutral", "Agree", "Strong Agree"};
    private ArrayAdapter<String> adapterClassName;
    private AutoCompleteTextView actClassName;
    private ArrayAdapter<String> adapterModuleName;
    private AutoCompleteTextView actModuleName;
    private TextView txtClassName;
    private TextView txtModuleName;
    //bo 3 huy diet
    private ValueEventListener fetchDataForPieChart;
    private ValueEventListener fetchDataForPieChart2;
    private ValueEventListener fetchDataForPieChart3;
    //PieChart pieChart
    private PieChart pieChart;
    private ArrayList<Integer> integerArrayList;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.result_fragment, container, false);

        //getting views
        actClassName = root.findViewById(R.id.actClassName);
        actModuleName = root.findViewById(R.id.actModuleName);
        txtClassName = root.findViewById(R.id.txtClassName);
        txtModuleName = root.findViewById(R.id.txtModuleName);
        pieChart = root.findViewById(R.id.pieChart);
        Button btnShowOverview = root.findViewById(R.id.btnShowOverview);
        Button btnShowDetail = root.findViewById(R.id.btnShowDetail);

        //set text first
        actClassName.setText("All");
        actModuleName.setText("All");

        //Take data to dropdown classID //done
        adapterClassName = new ArrayAdapter<>(getContext(), R.layout.option_item, arrayListClassName);
        fetchDataForACTVClassName();
        actClassName.setAdapter(adapterClassName);
        actClassName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getContext(), "test_result", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getContext(), "test_results", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Retrieve data
                //currentClassName = s.toString();
                fetchDataForPieChart();
            }
        });
        //Take data to dropdown moduleName //done
        adapterModuleName = new ArrayAdapter<>(getContext(), R.layout.option_item, arrayListModuleName);
        fetchDataForACTVModuleName();
        actModuleName.setAdapter(adapterModuleName);
        actModuleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getContext(), "test_result", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getContext(), "test_results", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Retrieve data
                //currentModuleName = s.toString();
                //fetchDataForPieChart(module_Name, class_Name);
                fetchDataForPieChart();
            }
        });

        //btnShowOverview
        btnShowOverview.setOnClickListener(v -> {
            //dang o day
            fetchDataForPieChart();
            Toast.makeText(getContext(), "ahoy~", Toast.LENGTH_LONG).show();
        });

        //btnShowDetail
        btnShowDetail.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_result_to_nav_show_detail));

        return root;
    }

    //for className dropdown //done
    public void fetchDataForACTVClassName() {
        FirebaseDatabase.getInstance().getReference("Class")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListClassName.add("All");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Class temp = dataSnapshot.getValue(Class.class);
                            if (temp != null)
                                arrayListClassName.add(temp.getClassName());
                        }
                        adapterClassName.notifyDataSetChanged();
                        actClassName.setText(adapterClassName.getItem(0), false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    //for moduleName dropdown //done
    public void fetchDataForACTVModuleName() {
        FirebaseDatabase.getInstance().getReference("Module")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListModuleName.add("All");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot != null) {
                                Module temp = dataSnapshot.getValue(Module.class);
                                if (temp != null)
                                    arrayListModuleName.add(temp.getModuleName());
                            }
                        }
                        adapterModuleName.notifyDataSetChanged();
                        actModuleName.setText(adapterClassName.getItem(0), false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    ////for PieChart
    //step1: find moduleID in Module
    @SuppressLint("SetTextI18n")
    private void fetchDataForPieChart() {
        //get newest filter texts
        String class_Name = String.valueOf(actClassName.getText()); //ko xai ham contains vi className vs moduleName must be shown
        String module_Name = String.valueOf(actModuleName.getText());

        //query id cua new_module_Name
        integerArrayList = new ArrayList<>(); //cho list ve empty
        integerArrayList.add(0, 0);
        integerArrayList.add(1, 0);
        integerArrayList.add(2, 0);
        integerArrayList.add(3, 0);
        integerArrayList.add(4, 0);
        integerArrayList.add(5, 0);
        if (module_Name.equals("All")) {
            txtModuleName.setText("All");
            fetchDataForPieChart2(0, class_Name);
        } else {
            fetchDataForPieChart = FirebaseDatabase.getInstance().getReference("Module")
                    .orderByChild("moduleName").equalTo(module_Name).limitToFirst(1)
                    .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Module temp = dataSnapshot.getValue(Module.class);
                                        if (temp != null) {
                                            int temp1 = temp.getModuleID();
                                            if (temp1 != 0) {
                                                ////found module_ID
                                                flag = false;
                                                killFetch("Module", fetchDataForPieChart);
                                                txtModuleName.setText(temp.getModuleName());
                                                fetchDataForPieChart2(temp1, class_Name);
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no module with moduleName //Fetching ends
                                killFetch("Module", fetchDataForPieChart);
                                txtClassName.setText("404:Not Found");
                                Toast.makeText(getContext(), "No module matches "+module_Name, Toast.LENGTH_LONG).show();
                                updatePieChart();
                                fetchDataForPieChart();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //step2: find classID in Class
    @SuppressLint("SetTextI18n")
    private void fetchDataForPieChart2(int module_ID, String class_Name) {
        //query id cua new_module_Name
        if (class_Name.equals("All")) {
            txtClassName.setText("All"); //set className on screen
            fetchDataForPieChart3(module_ID, 0);
        } else {
            fetchDataForPieChart2 = FirebaseDatabase.getInstance().getReference("Class")
                    .orderByChild("className").equalTo(class_Name).limitToFirst(1)
                    .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = true;
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Class temp = dataSnapshot.getValue(Class.class);
                                        if (temp != null) {
                                            int temp1 = temp.getClassID();
                                            if (temp1 != 0) {
                                                ////found class_ID
                                                flag = false;
                                                killFetch("Class", fetchDataForPieChart2);
                                                txtClassName.setText(temp.getClassName());  //set className on screen
                                                fetchDataForPieChart3(module_ID, temp1);
                                            }
                                        }
                                    }
                                }
                            }
                            if (flag) {
                                //no class with class//Fetching ends
                                killFetch("Class", fetchDataForPieChart2);
                                txtClassName.setText("404:Not Found");
                                Toast.makeText(getContext(), "No class matches "+class_Name, Toast.LENGTH_LONG).show();
                                updatePieChart();
                                fetchDataForPieChart();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //step3: get list Answer
    private void fetchDataForPieChart3(int module_ID, int class_ID) {
        //query id cua new_module_Name
        if (class_ID == 0) {
            fetchDataForPieChart3 = FirebaseDatabase.getInstance().getReference("Answer")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Answer temp = dataSnapshot.getValue(Answer.class);
                                        if (temp != null) {
                                            if (module_ID == 0) {
                                                ////found Answer with className=All and moduleName=All
                                                int temp1 = temp.getValue();
                                                integerArrayList.set(temp1, integerArrayList.get(temp1)+1);
                                            } else {
                                                int temp1 = temp.getModuleID();
                                                if (temp1 != 0 && temp1 == module_ID) {
                                                    ////found Trainee_Comment with className=ALl and moduleID=module_ID
                                                    integerArrayList.set(temp1, integerArrayList.get(temp1)+1);
                                                }
                                            }
                                        }
                                    }
                                }
                                //no Trainee_Comment with className=All and moduleName=All //Fetching ends
                                killFetch("Answer", fetchDataForPieChart3);
                                updatePieChart();
                                fetchDataForPieChart();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } else {
            fetchDataForPieChart3 = FirebaseDatabase.getInstance().getReference("Answer")
                    .orderByChild("classID").equalTo(class_ID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot != null) {
                                        Answer temp = dataSnapshot.getValue(Answer.class);
                                        if (temp != null) {
                                            int temp1 = temp.getValue();
                                            if (module_ID == 0) {
                                                ////found Trainee_Comment with classID=class_ID and moduleName=All
                                                integerArrayList.set(temp1, integerArrayList.get(temp1)+1);
                                            } else {
                                                int temp2 = temp.getModuleID();
                                                if (temp2 != 0 && temp2 == module_ID) {
                                                    ////found Trainee_Comment with classID=class_ID and moduleID=module_ID
                                                    integerArrayList.set(temp1, integerArrayList.get(temp1)+1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //no Trainee_Comment with classID=class_ID and moduleName=All //Fetching ends
                            killFetch("Answer", fetchDataForPieChart3);
                            updatePieChart();
                            fetchDataForPieChart();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    //kill any ValueEventListener //done
    private void killFetch(String path, ValueEventListener valueEventListener) {
        if (valueEventListener != null)
            FirebaseDatabase.getInstance().getReference(path).removeEventListener(valueEventListener);
    }

    //pieChar stuff
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null || h == null)
            return;
        //Toast.makeText(getContext(), "Value: " + e.getY() + ", index: " + h.getX() + "data: " + info[h.getDataIndex() + 1], Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {
        // do nothing
    }

    private void updatePieChart() {

        //data here
        int total = 0;
        for (int i = 1; i < integerArrayList.size(); i++) { //can than size//da +1 vi co index0
            total += integerArrayList.get(i);
        }
        float f1 = 0;
        float f3 = 0;
        float f5 = 0;
        if (total > 0) {
            f1 = (float) (100 * integerArrayList.get(1) / total);
            f3 = (float) (100 * integerArrayList.get(3) / total);
            f5 = (float) (100 * integerArrayList.get(5) / total);
        }

        List<PieEntry> yValues = new ArrayList<>(); //in the chart
        yValues.add(new PieEntry(f1,info[1]));
        yValues.add(new PieEntry(f3,info[3]));
        yValues.add(new PieEntry(f5,info[5]));
        PieDataSet dataSet = new PieDataSet(yValues,"");
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setDrawEntryLabels(true);

        Description d = pieChart.getDescription();
        d.setEnabled(false);

        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false); //hole

        Legend l = pieChart.getLegend();
        l.setDrawInside(true);
        l.setEnabled(true);
        l.setFormSize(10);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setEnabled(false);
        l.setWordWrapEnabled(true);
        //l.setDrawInside(true); //legends outside the chart
        //l.setXEntrySpace(7f); //7dp by far to the chart
        //l.setYEntrySpace(0f); //
        //l.setYOffset(30f);

        int[] colors = {
                Color.parseColor("#FFCC99"),
                Color.parseColor("#FF9966"),
                Color.parseColor("#FF6633"),
                Color.parseColor("#FF6600"),
                Color.parseColor("#FF3300"),
                Color.parseColor("#CC3300")};
        dataSet.setColors(ColorTemplate.createColors(colors));

        data.setValueTextColor(Color.WHITE); //color of numbers

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(1.2f);
        dataSet.setValueLinePart2Length(0.4f);

        //pieChart.setOnChartValueSelectedListener(this);

    }

}