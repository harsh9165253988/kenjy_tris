package com.example.project.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.organization.donation;
import com.example.project.dataModels.organizationDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class organizationAdapter extends RecyclerView.Adapter<organizationAdapter.ViewHolder> {
    private List<organizationDetail> data;

    public organizationAdapter(List<organizationDetail> dataList) {
        this.data = dataList;
    }

    public void filterList(List<organizationDetail> filteredList) {
        data = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, orgLocation, orgEmail, orgContact, orgMission;
        Button donateButton, followButton;
        ImageView orgImage;

        public ViewHolder(View itemView) {
            super(itemView);
            orgName = itemView.findViewById(R.id.orgName);
            orgLocation = itemView.findViewById(R.id.orgLocation);
            donateButton = itemView.findViewById(R.id.donate);
            orgEmail = itemView.findViewById(R.id.orgEmail);
            orgContact = itemView.findViewById(R.id.orgContact);
            orgMission = itemView.findViewById(R.id.orgMission);
            followButton = itemView.findViewById(R.id.followButton);
            orgImage = itemView.findViewById(R.id.profImage);

            orgEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open email when email TextView is clicked
                    String email = orgEmail.getText().toString().trim();
                    composeEmail(email);
                }
            });

            // Set click listener for contact TextView
            orgContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open dialer when contact TextView is clicked
                    String phoneNumber = orgContact.getText().toString().trim();
                    dialPhoneNumber(phoneNumber);
                }
            });

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        organizationDetail clickedOrganization = data.get(position);
                        String organizationId = clickedOrganization.getOrganizationId();

                        // Toggle follow status
                        toggleFollowStatus(organizationId);
                    }
                }
            });

            donateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        organizationDetail clickedOrganization = data.get(position);

                        // Pass the necessary details to the donation activity
                        Intent intent = new Intent(itemView.getContext(), donation.class);
                        intent.putExtra("organizationDetail", clickedOrganization);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        private void toggleFollowStatus(String organizationId) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                DatabaseReference organizationRef = FirebaseDatabase.getInstance().getReference()
                        .child("organization")
                        .child(organizationId)
                        .child("followers")
                        .child(userId);

                DatabaseReference userFollowingRef = FirebaseDatabase.getInstance().getReference()
                        .child("human")
                        .child(userId)
                        .child("following")
                        .child(organizationId);

                organizationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User is already following, so unfollow (delete the node)
                            organizationRef.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(itemView.getContext(), "Unfollowed", Toast.LENGTH_SHORT).show();
                                            updateFollowButton(false);
                                            userFollowingRef.removeValue(); // Remove from user's following node
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "Error unfollowing", e);
                                        }
                                    });
                        } else {
                            // User is not following, so follow (add the node)
                            organizationRef.setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(itemView.getContext(), "Followed", Toast.LENGTH_SHORT).show();
                                            updateFollowButton(true);
                                            userFollowingRef.setValue(true); // Add to user's following node
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "Error following", e);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "Error checking followers", error.toException());
                    }
                });
            }
        }

        private void updateFollowButton(boolean isFollowing) {
            if (isFollowing) {
                followButton.setEnabled(true);
                followButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.darker_gray));
                followButton.setText("Following");
            } else {
                followButton.setEnabled(true);
                followButton.setBackgroundResource(R.drawable.gradient_colour); // Replace with the original background
                followButton.setText("Follow");
            }
        }

        private void composeEmail(String emailAddress) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + emailAddress));

            // Use createChooser to present the user with a list of email apps to choose from
            Intent chooser = Intent.createChooser(intent, "Choose an email app");

            if (chooser.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                itemView.getContext().startActivity(chooser);
            } else {
                Toast.makeText(itemView.getContext(), "No email app found", Toast.LENGTH_SHORT).show();
            }
        }

        private void dialPhoneNumber(String phoneNumber) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            // Use createChooser to present the user with a list of dialer apps to choose from
            Intent chooser = Intent.createChooser(intent, "Choose a dialer app");

            if (chooser.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                itemView.getContext().startActivity(chooser);
            } else {
                Toast.makeText(itemView.getContext(), "No dialer app found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizationcardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        organizationDetail currentItem = data.get(position);

        holder.orgName.setText(currentItem.getOrgName());
        holder.orgLocation.setText(currentItem.getLocation());
        holder.orgEmail.setText(currentItem.getMail());
        holder.orgMission.setText(currentItem.getMission());
        holder.orgContact.setText(currentItem.getContact());
        String profilePhotoUrl = currentItem.getProfileImageUrl();

        // Use Glide to load the profile photo into the ImageView
        Glide.with(holder.itemView.getContext())
                .load(profilePhotoUrl)
                .placeholder(R.drawable.profileimg) // You can set a placeholder image
                .error(R.drawable.profileimg) // You can set an error image
                .into(holder.orgImage);
//
//        // Set the state of the followButton based on the organization's followers
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String userId = user.getUid();
//            String organizationId = currentItem.getOrganizationId();
//
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
//                    .child("organization")
//                    .child(organizationId)
//                    .child("followers")
//                    .child(userId);
//
//
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    // Check if the user is a follower
//                    boolean isFollowing = snapshot.exists() && (boolean) snapshot.getValue();
//
//                    // Update UI based on the user's following status
//                    holder.updateFollowButton(isFollowing);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e("TAG", "Error checking followers", error.toException());
//                }
//            });
//        }
//    }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
