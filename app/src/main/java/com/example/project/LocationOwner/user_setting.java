package com.example.project.LocationOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.R;
import com.example.project.user.userLogin;
import com.google.firebase.auth.FirebaseAuth;

public class user_setting extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        logoutBtn = findViewById(R.id.button);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user from Firebase
                firebaseAuth.signOut();

                // Redirect to the login activity
                Intent intent = new Intent(user_setting.this, userLogin.class);
                startActivity(intent);

                // Finish the current activity to remove it from the stack
                finish();
            }
        });
    }
}