package com.example.project.organization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.project.R;
import com.example.project.organization.OrgDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class orgProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
   private TextView location;

    private TextView numberTextView;
    private TextView AboutMissionTextView;
    private ImageView galleryImageView;

    private ImageView back_button;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private final int GALLERY_REQ_CODE = 1000;
    private void updateImageView(String imageUrl) {
        if (isAdded() && imageUrl != null && !imageUrl.isEmpty()) {
            // Load the image with Glide and apply a circular transformation
            Glide.with(this)
                    .load(imageUrl)
                    .transform(new CircleCrop())
                    .into(galleryImageView);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_profile, container, false);

        // Initialize TextViews and ImageView
        galleryImageView = view.findViewById(R.id.imageView4);
        back_button = view.findViewById(R.id.imageView16);
        nameTextView = view.findViewById(R.id.name);
        emailTextView = view.findViewById(R.id.email);
      location=view.findViewById(R.id.Location);
        numberTextView = view.findViewById(R.id.number);
        AboutMissionTextView = view.findViewById(R.id.mission);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instead of handling back press here, let the hosting activity handle it
                getActivity().onBackPressed();
            }
        });




        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("organization");
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
            }
        });

        if (currentUser != null) {
            String userId = currentUser.getUid();

            if (userId != null) {
                Log.d("orgProfileFragment", "orgName: " + userId);

                Query query = databaseReference.child("organization").child(userId);

                query.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d("orgProfileFragment", "DataSnapshot: " + dataSnapshot.getValue());

                            // Update TextViews with data from Firebase
                            OrgDataModel user = dataSnapshot.getValue(OrgDataModel.class);
                            if (user != null) {
                                nameTextView.setText(user.getorgName());
                                emailTextView.setText(user.getmail());
                                location.setText(user.getLocation());
                                AboutMissionTextView.setText(user.getMission());
                                numberTextView.setText(user.getContact());
                                updateImageView(user.getProfileImageUrl());
                            }
                        } else {
                            Log.e("OrgProfileFragment", "Organizaton with ID " + userId + " does not exist");
                            // Handle the case where the user does not exist
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("OrgProfileFragment", "DatabaseError: " + databaseError.getMessage());
                        // Handle errors
                    }
                });

            } else {
                Log.e("OrgProfileFragment", "Orgnization Name is null");
                // Handle the case where userName is null
            }
        } else {
            Log.e("OrgProfileFragment", "currentOrg is null");
            // Handle the case where currentUser is null
            // Redirect the user to the login screen, for example
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Upload the image to Firebase Storage
                    uploadImageToFirebase(selectedImageUri);
                }
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Create a unique name for the image in Firebase Storage
        String imageName = "profile_image_" + System.currentTimeMillis() + ".jpg";

        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(imageName);

        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // Now, you can save the image URL or download URL in your database or wherever needed
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // uri is the download URL of the uploaded image
                        String imageUrl = uri.toString();
                        // Save imageUrl to your database
                        updateProfileImage(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to upload image
                    Log.e("OrgProfileFragment", "Image upload failed: " + e.getMessage());
                });
    }

    private void updateProfileImage(String imageUrl) {
        // Now, you can update the user's profile image URL in the database
        // For example, you can use databaseReference.child("profileImageUrl").setValue(imageUrl);
        // Update your database structure accordingly
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        // Assuming you have a "profileImageUrl" field in your "human" node
        DatabaseReference userReference = databaseReference.child(currentUser.getUid());
        userReference.child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> Log.d("OrgProfileFragment", "Image URL updated successfully in the database"))
                .addOnFailureListener(e -> Log.e("OrgProfileFragment", "Failed to update image URL in the database: " + e.getMessage()));
    }
}