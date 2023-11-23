// addVacancyPopup class

package com.example.project.organization;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.airbnb.lottie.LottieAnimationView;
import com.example.project.R;
import com.example.project.dataModels.Vacancy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

// ... (imports)

public class addVacancyPopup {

    public interface OnVacancyCreatedListener {
        void onVacancyCreated(Vacancy vacancy);
    }

    public static void showPopupWindow(Context context, View anchorView, DatabaseReference databaseReference, OnVacancyCreatedListener listener) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_add_vacancy_popup, null);

        Spinner spinnerLocation = popupView.findViewById(R.id.spinnerlocation);
        EditText editTextDateTime = popupView.findViewById(R.id.editTextDateTime);
        EditText editTextPreferredSkills = popupView.findViewById(R.id.editTextPreferredSkills);
        Button buttonCreateVacancy = popupView.findViewById(R.id.buttonCreateVacancy);


        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        String currentDate=sdf.format(new Date());
        editTextDateTime.setText(currentDate);
        // Populate the spinner with your data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Set background to transparent
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        buttonCreateVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = spinnerLocation.getSelectedItem().toString();
                String dateTime = editTextDateTime.getText().toString();
                String preferredSkills = editTextPreferredSkills.getText().toString();

                // Initialize LottieAnimationView
                LottieAnimationView lottieAnimationView = popupView.findViewById(R.id.lottieAnimationView);

                // Start the animation
                lottieAnimationView.playAnimation();

                // Validate input if needed

                // Notify the listener about the created vacancy
                if (listener != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    Vacancy vacancy = new Vacancy(location, dateTime, preferredSkills);
                    databaseReference.child(uid).child("vacancy").push().setValue(vacancy);
                    listener.onVacancyCreated(vacancy);
                }

                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });
    }
}