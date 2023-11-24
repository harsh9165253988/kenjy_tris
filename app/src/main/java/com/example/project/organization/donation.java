package com.example.project.organization;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class donation extends AppCompatActivity {

    Button bnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);


        bnt = findViewById(R.id.paydonation);


        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open email when email TextView is clicked
                String upinum = bnt.getText().toString().trim();
               // makedonation(upinum);


               // private void makedonation()

                    String paymentAppUri = "https://www.phonepe.com" ;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(paymentAppUri));

                    // Use createChooser to present the user with a list of dialer apps to choose from
                    Intent chooser = Intent.createChooser(intent, "Choose a payment app");

                    if (chooser.resolveActivity(view.getContext().getPackageManager()) != null) {
                        view.getContext().startActivity(chooser);
                    } else {
                        Toast.makeText(view.getContext(), "No dialer app found", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }











}



