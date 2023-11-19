package com.example.project.organization;

import android.app.Activity;
import android.content.Intent;
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

    EditText name;
    EditText email;
    EditText amnt;
    Button bnt;
    String clientId= "AcUwCwR18eKqblPf-a5XfEECOU89mlYQGYFNXL5O1OHdjmblTOHDsJoi-fNg5juE7kzUFqJ64F-MIu6X";
    int PAYPAL_REQUEST_CODE=123;
    public  static PayPalConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        name = findViewById(R.id.namedonation);

        bnt = findViewById(R.id.paydonation);
        amnt = findViewById(R.id.amountdonation);

        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DonationActivity", "Donate button clicked");
                getPayment();
            }
        });

    }

        private void getPayment(){

            String amounts=amnt.getText().toString();
            PayPalPayment payment=new PayPalPayment(new BigDecimal(String.valueOf(amounts)),"USD","KenjyTris",PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent=new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

            startActivityForResult(intent,PAYPAL_REQUEST_CODE);



        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString();
                        JSONObject object = new JSONObject(paymentDetails);
                        // Handle payment details as needed
                    } catch (JSONException e) {
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



