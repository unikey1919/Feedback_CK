package com.example.feedbackapplication.ui.enrollment;

import androidx.annotation.NonNull;

import com.example.feedbackapplication.model.Enrollment;
import com.example.feedbackapplication.model.Trainee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseHelper {

    private static String just_a_string;
    private static int just_a_number = 0;
    private static Enrollment enrollment;
    private static Trainee trainee;
    private  static Class just_a_class;

    public static int fetchClassID(String className) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Class");
        reference.orderByChild("ClassName").equalTo(className).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        just_a_number = Integer.parseInt(dataSnapshot.child("ClassID").getValue(Integer.class).toString());
                    }
                } else {//
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return just_a_number;
    }

    public static String fetchTraineeName(String traineeID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trainee");
        reference.orderByChild("UserName").equalTo(traineeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if(dataSnapshot!=null)
                            trainee = dataSnapshot.getValue(Trainee.class);
                        if (trainee!=null)
                            just_a_string = trainee.getName();
                        //just_a_string = dataSnapshot.child("Name").getValue(String.class);
                    }
                } else {
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(just_a_string.isEmpty())
            return "";
        return just_a_string;
    }

    public static String fetchEnrollmentKey(int classID, String traineeID) {
        just_a_string = null;
        FirebaseDatabase.getInstance().getReference("Enrollment")
                .orderByChild("classID").equalTo(classID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                        //String temp = dataSnapshot.child("className").getValue(String.class);
                        if (enrollment!=null) {
                            if(enrollment.getTraineeID().equals(traineeID))
                                just_a_string = dataSnapshot.getKey();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return just_a_string;
    }

    //done
    public static String fetchSomeThing(int classID, String traineeID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Query query = reference.child("Enrollment").equalTo(classID,"classID");
        //database enrollment
        reference = FirebaseDatabase.getInstance().getReference("Enrollment");
        reference.orderByChild("classID").equalTo(classID).limitToFirst(2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        just_a_number = Integer.parseInt(dataSnapshot.child("classID").getValue(Integer.class).toString());
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return String.valueOf(just_a_number);
    }

}
