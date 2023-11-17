package com.example.project.organization;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.messaging.FirebaseMessaging;

import android.widget.PopupWindow;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class addVacancyPopup {

    public static final String FCM_TOPIC="vacancy_notification";
    public static final String CHANNEL_ID = "My Channel";
    public static final int NOTIFICATION_ID = 100;

    public interface OnVacancyCreatedListener {
        void onVacancyCreated(Vacancy vacancy);
    }

    public static void showPopupWindow(Context context, View anchorView, DatabaseReference databaseReference, OnVacancyCreatedListener listener) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_add_vacancy_popup, null);

        EditText editTextLocation = popupView.findViewById(R.id.editTextLocation);
        EditText editTextDateTime = popupView.findViewById(R.id.editTextDateTime);
        EditText editTextPreferredSkills = popupView.findViewById(R.id.editTextPreferredSkills);
        Button buttonCreateVacancy = popupView.findViewById(R.id.buttonCreateVacancy);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Set background to transparent
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        buttonCreateVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                String location = editTextLocation.getText().toString();
                String dateTime = editTextDateTime.getText().toString();
                String preferredSkills = editTextPreferredSkills.getText().toString();

                // Validate input if needed

                // Notify the listener about the created vacancy
                if (listener != null) {
                    Vacancy vacancy = new Vacancy(location, dateTime, preferredSkills);
                    databaseReference.child(uid).child("vacancy").push().setValue(vacancy);
                    listener.onVacancyCreated(vacancy);

                    // Subscribe to FCM topic
                    FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC);


                    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.backarrow, null);
                    BitmapDrawable bitmapDrawable = null;
                    Notification notification;

                    if (drawable instanceof BitmapDrawable) {
                        bitmapDrawable = (BitmapDrawable) drawable;
                    } else {
                        Bitmap defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                        bitmapDrawable = new BitmapDrawable(context.getResources(), defaultBitmap);
                    }

                    Bitmap largeIcon = bitmapDrawable.getBitmap();

                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (nm != null && !nm.areNotificationsEnabled()) {
                        // If notifications are not enabled, prompt the user to enable them
                        // You can navigate the user to the app settings to enable notifications
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        ContextCompat.startActivity(context, intent, null);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        notification = new Notification.Builder(context)
                                .setLargeIcon(largeIcon)
                                .setSmallIcon(R.drawable.backarrow)
                                .setContentText("New vacancy:"+vacancy.getLocation())
                                .setSubText("New message kenji")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setChannelId(CHANNEL_ID)
                                .build();

                        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New channel", NotificationManager.IMPORTANCE_HIGH));

                    } else {
                        notification = new Notification.Builder(context)
                                .setLargeIcon(largeIcon)
                                .setSmallIcon(R.drawable.backarrow)
                                .setContentText("New vacancy"+vacancy.getLocation())
                                .setSubText("New message kenji")
                                .build();
                    }

                    Log.d("Notification", "Creating notification");
                    nm.notify(NOTIFICATION_ID, notification);

                    // Dismiss the popup window
                    popupWindow.dismiss();
                }
            }
        });
    }
    private static void createNotificationChannel(NotificationManager nm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check if the channel exists before creating it
            NotificationChannel channel = nm.getNotificationChannel(CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, "New channel", NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(channel);
            }
        }
    }
}
