package com.example.project.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.project.user.userManageFragment;
import com.example.project.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class userDashboard extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new userDashboardFragment()).commit();
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

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof userDashboardFragment) {
            // If the current fragment is userDashboardFragment
            if (doubleBackToExitPressedOnce) {
                // If the back button is pressed twice within a certain time frame, exit the app
                super.onBackPressed();
                finish();
            } else {
                // Show a toast or message indicating that the user should press back again to exit
                doubleBackToExitPressedOnce = true;
                // Use a Handler to reset the doubleBackToExitPressedOnce variable after a specified delay
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000); // 2000 milliseconds = 2 seconds
            }
        } else {
            // If it's not the userDashboardFragment, navigate back to it
            chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new userDashboardFragment()).commit();
        }
    }
}
