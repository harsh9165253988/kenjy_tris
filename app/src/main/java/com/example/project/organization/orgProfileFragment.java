package com.example.project.organization;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.project.LocationOwner.organizationDashboard;
import com.example.project.R;
import com.example.project.dataModels.OrgDataModel;
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
    AlertDialog.Builder builder;
    private ImageView back_button;
    private Button logoutbtn;

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
        emailTextView = view.findViewById(R.id.mail);
        location=view.findViewById(R.id.Location);
        numberTextView = view.findViewById(R.id.number);
        AboutMissionTextView = view.findViewById(R.id.mission);
        logoutbtn=view.findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        builder = new AlertDialog.Builder(getActivity());

        ImageView imageViewFollowers = view.findViewById(R.id.imageView3);
        imageViewFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on follower image
                Intent intent = new Intent(getActivity(), followersActivity.class);
                startActivity(intent);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before logging out
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked Yes, sign out
                                firebaseAuth.signOut();

                                dialog.dismiss();
                                Intent i=new Intent(getActivity(),orgainizationSignIn.class);

                                startActivity(i);
                                getActivity().finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked No, do nothing or dismiss the dialog
                                dialog.dismiss();

                            }
                        })
                        .show();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i =new Intent(getActivity(), organizationDashboard.class) ;
              startActivity(i);
              getActivity().finish();
            }
        });



        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Log.e("orgProfileFragment", "Organizaton with ID " + userId + " does not exist");
                            // Handle the case where the user does not exist
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("orgProfileFragment", "DatabaseError: " + databaseError.getMessage());
                        // Handle errors
                    }
                });

            } else {
                Log.e("orgProfileFragment", "userId is null");
                // Handle the case where userName is null
            }
        } else {
            Log.e("orgProfileFragment", "currentUser is null");

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

                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // uri is the download URL of the uploaded image
                        String imageUrl = uri.toString();
                        // Save imageUrl to your database
                        updateProfileImage(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to upload image
                    Log.e("orgProfileFragment", "Image upload failed: " + e.getMessage());
                });
    }

    private void updateProfileImage(String imageUrl) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        DatabaseReference userReference = databaseReference.child(currentUser.getUid());
        userReference.child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> Log.d("orgProfileFragment", "Image URL updated successfully in the database"))
                .addOnFailureListener(e -> Log.e("orgProfileFragment", "Failed to update image URL in the database: " + e.getMessage()));
    }
}