package com.example.project.LocationOwner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.project.LocationOwner.userDashboardFragment;
import com.example.project.LocationOwner.userManageFragment;
import com.example.project.LocationOwner.userProfileFragment;
import com.example.project.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class userDashboard extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new userDashboardFragment()).commit();
        bottomMenu();
    }


    private void bottomMenu() {


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int itemId) {
                Fragment fragment = null;

                if (itemId == R.id.bottom_nav_dashboard) {
                    fragment = new userDashboardFragment();
                } else if (itemId == R.id.bottom_nav_manage) {
                    fragment = new userManageFragment();
                } else if (itemId == R.id.bottom_nav_profile) {
                    fragment = new userProfileFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}