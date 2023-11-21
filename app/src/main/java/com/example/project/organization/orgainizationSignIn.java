package com.example.project.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.project.LocationOwner.organizationDashboard;
import com.example.project.LocationOwner.userDashboard;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class orgainizationSignIn extends AppCompatActivity {
    private AwesomeValidation awesomeValidation;
    private Button signin;
    private TextView signup;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgainization_sign_in);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        signin = findViewById(R.id.buttonLogin);
        signup = findViewById(R.id.new_member);
        progressBar=findViewById(R.id.progressBar);
        loadingText=findViewById(R.id.loadingText);

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
                // Show loading UI
                progressBar.setVisibility(View.VISIBLE);
                loadingText.setVisibility(View.VISIBLE);

                // Perform background task (simulate with a delay for illustration)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Simulate some work
                            Thread.sleep(3000);
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
                                Intent i = new Intent(getApplicationContext(), organizationSignup.class);
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

    private void signInUser() {
        String email = ((TextView) findViewById(R.id.editTextEmail)).getText().toString().trim();
        String password = ((TextView) findViewById(R.id.editPassword)).getText().toString().trim();
        showLoadingUI();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideLoadingUI();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), organizationDashboard.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                        // You can add additional logic here, such as navigating to the main activity
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "Sign in failed. " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}