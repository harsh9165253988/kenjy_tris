package com.example.project.LocationOwner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.project.LocationOwner.userDashboardFragment;
import com.example.project.LocationOwner.userManageFragment;
import com.example.project.LocationOwner.userProfileFragment;
import com.example.project.R;
import com.example.project.organization.orgDashboardFragment;
import com.example.project.organization.orgManageFragment;
import com.example.project.organization.orgProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class organizationDashboard extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_dashboard);
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
                    fragment = new orgDashboardFragment();
                } else if (itemId == R.id.bottom_nav_manage) {
                    fragment = new orgManageFragment();
                } else if (itemId == R.id.bottom_nav_profile) {
                    fragment = new orgProfileFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}