package com.example.project;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.organization.orgainizationSignIn;
import com.example.project.user.userLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//notification ke liye

      /*  Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.backarrow,null);
       // BitmapDrawable bitmapDrawable=drawable;



        BitmapDrawable bitmapDrawable = null;

        if (drawable instanceof BitmapDrawable) {
            bitmapDrawable = (BitmapDrawable) drawable;
        } else {
            // Handle the case where the Drawable is not a BitmapDrawable
            // You might want to create a default BitmapDrawable or handle it differently based on your needs
            // For now, I'm assuming creating a new BitmapDrawable with a default Bitmap
            Bitmap defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            bitmapDrawable = new BitmapDrawable(getResources(), defaultBitmap);
        }

       // Bitmap largeIcon = bitmapDrawable.getBitmap();





        Bitmap largeIcon=bitmapDrawable.getBitmap();


        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.N){
            notification=new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.backarrow)
                    .setContentText("New vaccancy")
                    .setSubText("New message kenji")
                    .setChannelId(CHANNEL_ID)
                    .build();

            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"New channel",NotificationManager.IMPORTANCE_HIGH));

        }else {
            notification=new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.backarrow)
                    .setContentText("New vaccancy")
                    .setSubText("New message kenji")
                    .build();
        }
// nm.notify(NOTIFICATION_ID,notification);   */

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

    //setContentView(R.layout.item_vacancy);

}