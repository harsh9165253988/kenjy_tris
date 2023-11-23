package com.example.project.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.project.R;
import com.example.project.dataModels.humandetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class userSignup extends AppCompatActivity {

    private Button requestButton;
    private RadioButton M;
    private  RadioButton F;
    private TextView alreadySignin;

    // private  TextView skill;
    private EditText emailEditText;
    private EditText passwordEditText;
    private  EditText fullname;
    private  EditText number;
    private  EditText age;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;
    private  TextView jobSelectonMenu;
    boolean [] selectjob;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<Integer> jobList =new ArrayList<>();
        String[] jobarray = getResources().getStringArray(R.array.job_options);
        setContentView(R.layout.activity_user_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        fullname = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        number = findViewById(R.id.editTextMobile);
        // skill= findViewById(R.id.jobSelection);
        age = findViewById(R.id.editTextAge);
        passwordEditText = findViewById(R.id.editPassword);
        M=findViewById(R.id.radioMale);
        F=findViewById(R.id.radioFemale);
        requestButton = findViewById(R.id.buttonSign);
        alreadySignin = findViewById(R.id.already_signin);
        jobSelectonMenu=findViewById(R.id.jobSelection);
        selectjob= new boolean[jobarray.length];
        jobSelectonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(userSignup.this);
                builder.setTitle("Select job");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(jobarray, selectjob, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b){
                            jobList.add(i);
                            Collections.sort(jobList);
                        }else {
                            jobList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int j=0;j<jobList.size();j++){
                            stringBuilder.append(jobarray[jobList.get(j)]);
                            if(j!=jobList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        jobSelectonMenu.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<selectjob.length;j++){
                            selectjob[j]=false;
                            jobList.clear();
                            jobSelectonMenu.setText("");
                        }
                    }
                });
                builder.show();
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
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String userName=fullname.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName) // Set the display name to the user's input
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Profile", "User profile updated.");
                                                }
                                            }
                                        });
                                rootnode = FirebaseDatabase.getInstance();
                                reference =rootnode.getReference("human");

                                String name,email,mob,ag,pd,m,f,gender,skl;
                                name = fullname.getEditableText().toString();
                                email = emailEditText.getEditableText().toString();
                                skl =jobSelectonMenu.getText().toString();
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



                                humandetail hd=new humandetail(name,email,mob,ag,pd,gender,skl);

                                reference.child(uid).setValue(hd);



                            }

                                    Intent i=new Intent(userSignup.this, userLogin.class);
                                    startActivity(i);
                                    finish();


                            Toast.makeText(userSignup.this, "Signup successful", Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(userSignup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}