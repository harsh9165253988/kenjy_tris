package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userSignup extends AppCompatActivity {

    private Button requestButton;
    private RadioButton M;
    private  RadioButton F;
    private TextView alreadySignin;
    private EditText emailEditText;
    private EditText passwordEditText;
private  EditText fullname;
    private  EditText number;
    private  EditText age;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        fullname = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        number = findViewById(R.id.editTextMobile);
        age = findViewById(R.id.editTextAge);
        passwordEditText = findViewById(R.id.editPassword);
        M=findViewById(R.id.radioMale);
        F=findViewById(R.id.radioFemale);
        requestButton = findViewById(R.id.buttonSign);
        alreadySignin = findViewById(R.id.already_signin);



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
                    rootnode = FirebaseDatabase.getInstance();
                    reference =rootnode.getReference("human");

                    String name,email,mob,ag,pd,m,f,gender;
                    name = fullname.getEditableText().toString();
                    email = emailEditText.getEditableText().toString();
                    mob =number .getEditableText().toString();
                    ag= age.getEditableText().toString();
                    pd = passwordEditText.getEditableText().toString();
                    m = M.isChecked() ? "Male" : "";
                    f = F.isChecked() ? "Female" : "";

                    if (M.isChecked()) {
                        gender = "Male";
                    } else if (F.isChecked()) {
                        gender = "Female";
                    } else {
                        // Handle the case where neither radio button is checked
                        gender = ""; // or set it to a default value
                    }



                    humandetail hd=new humandetail(name,email,mob,ag,pd,gender);

                    reference.child(name).setValue(hd);

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
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(userSignup.this, "Signup successful", Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(userSignup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}