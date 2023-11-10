package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userSignup extends AppCompatActivity {

    private Button requestButton;
    private TextView alreadySignin;
    private EditText emailEditText;
    private EditText passwordEditText;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        final EditText fullname = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        final EditText number = findViewById(R.id.editTextMobile);
        final EditText age = findViewById(R.id.editTextAge);
        passwordEditText = findViewById(R.id.editPassword);

        requestButton = findViewById(R.id.buttonSign);
        alreadySignin = findViewById(R.id.already_signin);
        Spinner spinnerJob = findViewById(R.id.spinneJoblist);
        TextInputLayout textInputLayout = findViewById(R.id.joblist);
        textInputLayout.setDefaultHintTextColor(ContextCompat.getColorStateList(this, android.R.color.white));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.job_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJob.setAdapter(adapter);
        spinnerJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Set the selected item text color to black
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editTextMobile, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passworderror);

        awesomeValidation.addValidation(this, R.id.editTextAge, new SimpleCustomValidation() {
            @Override
            public boolean compare(String input) {
                try {
                    int userAge = Integer.parseInt(input);
                    return userAge >= 14;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }, R.string.ageerror);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    registerUser();
                }
            }
        });

        alreadySignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), userLogin.class);
                startActivity(i);
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(userSignup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(userSignup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}