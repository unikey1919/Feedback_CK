package com.example.feedbackapplication.ui.join;

import com.example.feedbackapplication.MainActivity;
import com.example.feedbackapplication.R;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import com.example.feedbackapplication.R;

public class TraineeDrawer extends AppCompatActivity {
    private Button btnCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAGe", "cccccccccccccccccc");

        MenuItem mNavigationView = (MenuItem) findViewById(R.id.nav_join);
        mNavigationView.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("TAGe", "ccccccrrrrrrrrrrcc");
                return false;
            }
        });
    }



        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Log.d("TAGe", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                return true;


    }



}
