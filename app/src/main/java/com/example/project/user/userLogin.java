
package com.example.project.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.project.LocationOwner.userDashboard;
import com.example.project.R;
import com.example.project.user.userSignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userLogin extends AppCompatActivity {

    private Button requestButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView alreadySignup;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editPassword);
        requestButton = findViewById(R.id.buttonLogin);
        alreadySignup = findViewById(R.id.new_member);

        // Validation rules
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passworderror);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    loginUser();
                }
            }
        });

        alreadySignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), userSignup.class);
                startActivity(i);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Check if the user is a human
                            checkUserTypeAndNavigate(firebaseAuth.getCurrentUser().getUid());
                        } else {
                            // Sign-in failed
                            Toast.makeText(getApplicationContext(), "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void checkUserTypeAndNavigate(String userId) {
        DatabaseReference humanRef = FirebaseDatabase.getInstance().getReference("human").child(userId);

        humanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is a human, proceed with the login
                    Toast.makeText(getApplicationContext(), "Sign-in successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), userDashboard.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                } else {
                    // User is not a human
                    Toast.makeText(getApplicationContext(), "You are not authorized to log in", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut(); // Sign out the user
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