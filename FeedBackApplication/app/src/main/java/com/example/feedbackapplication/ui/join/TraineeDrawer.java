//package com.example.feedbackapplication.ui.join;
//import com.example.feedbackapplication.R;
//import com.google.android.material.navigation.NavigationView;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class TraineeDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//    private Button btnCustomDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "ehhehehe",
//                Toast.LENGTH_LONG/2).show();
//        btnCustomDialog = (Button) findViewById(R.id.nav_join2);
//        btnCustomDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JoinFragment2 customDialog = new JoinFragment2();
//                customDialog.show(getSupportFragmentManager(), "customDialog");
//            }
//        });
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        switch (item.getItemId()) {
//
//            case R.id.nav_maths: {
//                //do somthing
//                break;
//            }
//        }
//        //close navigation drawer
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//}
