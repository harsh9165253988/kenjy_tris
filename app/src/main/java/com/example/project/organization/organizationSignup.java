package com.example.project.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.project.R;
import com.example.project.organization.orgainizationSignIn;
import com.example.project.organizationdetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class organizationSignup extends AppCompatActivity {
    private AwesomeValidation awesomeValidation;
    private Button signUp;
    private TextView signIn;
    private TextView unam;
    private TextView mail;
    private TextView mobnu;
    private TextView misn;
    private TextView paswd;
   private Spinner lc;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_signup);

        mAuth =FirebaseAuth.getInstance();

        signUp = findViewById(R.id.buttonSign);
        signIn = findViewById(R.id.already_signin);
        unam=findViewById(R.id.editTextName);
        mail=findViewById(R.id.editTextEmail);
        mobnu=findViewById(R.id.editTextMobile);
        misn=findViewById(R.id.mission);
        paswd=findViewById(R.id.editPassword);
        lc= findViewById(R.id.spinneJoblist);
        mAuth = FirebaseAuth.getInstance();
        Spinner spinnerJob = findViewById(R.id.spinneJoblist);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.locations,
                android.R.layout.simple_spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lc.setAdapter(adapter);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.mission, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.mission);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editTextMobile, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.editPassword, regexPassword, R.string.passworderror);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    // If validations pass, attempt to sign up the user
                    signUpUser();

                    rootnode=FirebaseDatabase.getInstance();
                    reference=rootnode.getReference("organization");


                   String OrgName,Mail,Contact,Mission,Password,Location;
                    OrgName = unam.getEditableText().toString();
                    Mail = mail.getEditableText().toString();
                    Contact=mobnu.getText().toString();
                    Mission=misn .getEditableText().toString();
                    Password = paswd.getEditableText().toString();
                    Location=lc.getSelectedItem().toString();




                    organizationdetail od=new organizationdetail(OrgName,Mail,Contact,Mission,Password,Location);
                    reference.child(OrgName).setValue(od);

                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), orgainizationSignIn.class);
                startActivity(i);
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinnerJob.setAdapter(adapter);

        // Set a listener to handle the selection
        spinnerJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // If the hint item is selected, set the text color to gray
                if (position == 0) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);
                } else {
                    // Set the selected item text color to black
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Set the hint as the default selection
        spinnerJob.setSelection(0, false);
    }



    private void signUpUser() {
        String email = ((TextView) findViewById(R.id.editTextEmail)).getText().toString().trim();
        String password = ((TextView) findViewById(R.id.editPassword)).getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();
                        // You can add additional logic here, such as navigating to the main activity
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "Sign up failed. " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}





