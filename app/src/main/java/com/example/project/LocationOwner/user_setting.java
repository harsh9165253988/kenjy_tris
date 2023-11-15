package com.example.project.LocationOwner;

import static android.app.PendingIntent.getActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import com.example.project.R;
import com.example.project.user.userLogin;
import com.google.firebase.auth.FirebaseAuth;

public class user_setting extends AppCompatActivity {

    Button logoutBtn;
    AlertDialog.Builder builder;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        logoutBtn = findViewById(R.id.button);
        builder = new AlertDialog.Builder(this);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                builder.setTitle("Alert").setMessage("Do You Want To LogOut This Application").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Finish the current activity to remove it from the stack
                                finish();


                                Intent intent = new Intent(user_setting.this, userLogin.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }



}





}