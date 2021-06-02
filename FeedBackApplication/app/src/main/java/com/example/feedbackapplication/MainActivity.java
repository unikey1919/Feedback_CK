package com.example.feedbackapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.feedbackapplication.Adapter.ModuleAdapter;
import com.example.feedbackapplication.model.Module;
import com.example.feedbackapplication.ui.join.JoinFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private Button btnLogin;
    private static String KEY_ROLE = "default";
    private static String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get role for each user
        try {
            KEY_ROLE = getIntent().getStringExtra("role");
            String username = getIntent().getStringExtra("username");
            Toast.makeText(this, "Show username in main: "+username, Toast.LENGTH_LONG/2).show();

            if (KEY_ROLE.equals("admin")) {
                setContentView(R.layout.admin_layout);
            }
            if (KEY_ROLE.equals("trainer")) {
                setContentView(R.layout.trainer_layout);
            }
            if (KEY_ROLE.equals("trainee")) {
                setContentView(R.layout.trainee_layout);
            }
        } catch (Exception e) {
            setContentView(R.layout.activity_main);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_assignment, R.id.nav_class, R.id.nav_module, R.id.nav_enrollment,
                R.id.nav_feedback, R.id.nav_result, R.id.nav_question, R.id.nav_question, R.id.nav_contact,
                R.id.nav_logout, R.id.nav_join,R.id.nav_add_module, R.id.nav_add_assignment, R.id.nav_edit_module)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int a = item.getItemId();
        if(a==R.id.nav_join2)
            Log.d("TAGq", "xxxxx "+item+" *** "+a);
        Log.d("TAGq", "onOptionsItemSelected: "+item+" *** "+a);
        return super.onOptionsItemSelected(item);
    }


    public Bundle getMyData() {
        //send data to fragment
        KEY_ROLE = getIntent().getStringExtra("role");
        UserName = getIntent().getStringExtra("username");
        Bundle hm = new Bundle();
        hm.putString("username",UserName);
        if(KEY_ROLE == null){
            hm.putString("val1","null");
            KEY_ROLE = "cant null to equal";
        }
        if(KEY_ROLE.equals("admin")){
            hm.putString("val1","admin");
        }
        if(KEY_ROLE.equals("trainer")){
            hm.putString("val1","trainer");
        }
        if(KEY_ROLE.equals("trainee")){
            hm.putString("val1","trainee");
        }
        return hm;
    }

}