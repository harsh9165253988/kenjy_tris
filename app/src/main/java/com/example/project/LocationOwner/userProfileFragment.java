package com.example.project.LocationOwner;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.userDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// ... (your imports and other code)

public class userProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private TextView numberTextView;
    private TextView skillTextView;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize TextViews
        nameTextView = view.findViewById(R.id.name);
        emailTextView = view.findViewById(R.id.email);
        ageTextView = view.findViewById(R.id.age);
        genderTextView = view.findViewById(R.id.gender);
        numberTextView = view.findViewById(R.id.number);
        skillTextView = view.findViewById(R.id.skill);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference(); // You might need to adjust the path

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the current user's ID

            Log.d("UserProfileFragment", "userID: " + userId);

            // Rest of your code remains the same, just replace the hard-coded user ID with the dynamic one
            Query query = databaseReference.child("human").child(userId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("UserProfileFragment", "DataSnapshot: " + dataSnapshot.getValue());

                        // Update TextViews with data from Firebase
                        userDataModel user = dataSnapshot.getValue(userDataModel.class);
                        if (user != null) {
                            nameTextView.setText(user.getName());
                            emailTextView.setText(user.getEmail());
                            ageTextView.setText(user.getAge());
                            genderTextView.setText(user.getGender());
                            numberTextView.setText(user.getMob());
                            skillTextView.setText(user.getSkl());
                        }
                    } else {
                        Log.e("UserProfileFragment", "User with ID " + userId + " does not exist");
                        // Handle the case where the user does not exist
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UserProfileFragment", "DatabaseError: " + databaseError.getMessage());
                    // Handle errors
                }
            });
        } else {
            Log.e("UserProfileFragment", "currentUser is null");
            // Handle the case where currentUser is null
            // Redirect the user to the login screen, for example
        }

        return view;
    }
}
