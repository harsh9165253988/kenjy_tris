
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
import android.widget.ProgressBar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.project.LocationOwner.userDashboard;
import com.example.project.R;
import com.example.project.user.userSignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
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
    private ProgressBar progressBar;
    private TextView loadingText;

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
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

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
                // Show loading UI
                progressBar.setVisibility(View.VISIBLE);
                loadingText.setVisibility(View.VISIBLE);

                // Perform background task (simulate with a delay for illustration)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000); // Simulate some work
                        } catch (InterruptedException e) {

                        }
                        // Hide loading UI when the task is complete
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Hide loading UI
                                progressBar.setVisibility(View.GONE);
                                loadingText.setVisibility(View.GONE);

                                // Start the new activity
                                Intent i = new Intent(getApplicationContext(), userSignup.class);
                                startActivity(i);
                            }
                        });
                    }
                }).start();
            }
        });


    }

    private void showLoadingUI() {
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
    }

    private void hideLoadingUI() {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }


    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Show loading UI
        showLoadingUI();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Hide loading UI when the task is complete
                        hideLoadingUI();

                        if (task.isSuccessful()) {
                            // Sign-in successful
                            Toast.makeText(getApplicationContext(), "Sign-in successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), userDashboard.class);
                            startActivity(intent);
                            finish(); // Close the current activity
                        } else {
                            // Sign-in failed
                            Toast.makeText(getApplicationContext(), "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    }


