package com.example.project;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.organization.orgainizationSignIn;
import com.example.project.organization.organizationSignup;
import com.example.project.user.userLogin;
import com.example.project.user.userSignup;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void animateUniverse(View view) {
        Button universeButton = findViewById(R.id.universeButton);
        ObjectAnimator scaleAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.universe_scale);
        scaleAnimator.setTarget(universeButton);
        scaleAnimator.start();
        universeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, orgainizationSignIn.class);
                startActivity(intent);
            }
        });

    }

    public void animatePlanet(View view) {
        Button planetButton = findViewById(R.id.planetButton);
        ObjectAnimator rotateAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.planet_rotate);
        rotateAnimator.setTarget(planetButton);
        rotateAnimator.start();
        //Button userSign = findViewById(R.id.planetButton);
        planetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NextActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, userLogin.class);
                startActivity(intent);
            }
        });
    }
}