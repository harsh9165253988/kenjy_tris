package com.example.project.LocationOwner;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        if (currentUser != null) {
            String userName = currentUser.getDisplayName();

            if (userName != null) {
                Log.d("UserProfileFragment", "userName: " + userName);

                databaseReference = FirebaseDatabase.getInstance().getReference("human");

                // Read data from Firebase
                if (databaseReference != null) {
                    Query query = databaseReference.orderByChild("name").equalTo(userName);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("UserProfileFragment", "DataSnapshot: " + snapshot.getValue());

                                // Update TextViews with data from Firebase
                                userDataModel user = snapshot.getValue(userDataModel.class);
                                if (user != null) {
                                    nameTextView.setText(user.getName());
                                    emailTextView.setText(user.getEmail());
                                    ageTextView.setText(String.valueOf(user.getAge()));
                                    genderTextView.setText(user.getGender());
                                    numberTextView.setText(user.getMobile());
                                    skillTextView.setText(user.getSkills());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("UserProfileFragment", "DatabaseError: " + databaseError.getMessage());
                            // Handle errors
                        }
                    });
                } else {
                    Log.e("UserProfileFragment", "DatabaseReference is null");
                    // Handle the case where databaseReference is null
                }
            } else {
                Log.e("UserProfileFragment", "userName is null");
                // Handle the case where userName is null
            }
        } else {
            Log.e("UserProfileFragment", "currentUser is null");
            // Handle the case where currentUser is null
            // Redirect the user to the login screen, for example
        }

        return view;
    }
}