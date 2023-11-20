package com.example.project.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.project.LocationOwner.organizationDashboard;
import com.example.project.LocationOwner.userDashboard;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class orgainizationSignIn extends AppCompatActivity {
    private AwesomeValidation awesomeValidation;
    private Button signin;
    private TextView signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgainization_sign_in);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        signin = findViewById(R.id.buttonLogin);
        signup = findViewById(R.id.new_member);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passworderror);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    // If validations pass, attempt to sign in the user
                    signInUser();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), organizationSignup.class);
                startActivity(i);
            }
        });
    }

    private void signInUser() {
        String email = ((TextView) findViewById(R.id.editTextEmail)).getText().toString().trim();
        String password = ((TextView) findViewById(R.id.editPassword)).getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Check if the user is a organization
                        checkUserTypeAndNavigate(mAuth.getCurrentUser().getUid());
                    } else {
                        // Sign-in failed
                        Toast.makeText(getApplicationContext(), "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUserTypeAndNavigate(String userId) {
        DatabaseReference humanRef = FirebaseDatabase.getInstance().getReference("organization").child(userId);

        humanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is a human, proceed with the login
                    Toast.makeText(getApplicationContext(), "Sign-in successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), organizationDashboard.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                } else {
                    // User is not a human
                    Toast.makeText(getApplicationContext(), "You are not authorized to log in", Toast.LENGTH_SHORT).show();
                    mAuth.signOut(); // Sign out the user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}