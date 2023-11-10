package com.example.project;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    }

    public void animatePlanet(View view) {
        Button planetButton = findViewById(R.id.planetButton);
        ObjectAnimator rotateAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.planet_rotate);
        rotateAnimator.setTarget(planetButton);
        rotateAnimator.start();
        Button userSign = findViewById(R.id.planetButton);
        userSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NextActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, userSignup.class);
                startActivity(intent);
            }
        });
    }
}
