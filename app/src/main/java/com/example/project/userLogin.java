package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class userLogin extends AppCompatActivity {

    Button requestButton;
    TextView alreadySignup;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        requestButton = (Button) findViewById(R.id.buttonLogin);
        alreadySignup = (TextView) findViewById(R.id.new_member);
        requestButton.setOnClickListener(mMyListener);
        alreadySignup.setOnClickListener(mMyListener);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passworderror);
    }
    private View.OnClickListener mMyListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.buttonLogin) {
                if (awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(), "It's Working!", Toast.LENGTH_LONG).show();
                }
            } else if (v.getId() == R.id.new_member) {
                Intent i = new Intent(getApplicationContext(), userSignup.class);
                startActivity(i);
            }
        }

    };
    }
