package com.example.project.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.dataModels.organizationDetail;
import com.example.project.dataModels.userDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class followingAdapter extends RecyclerView.Adapter<followingAdapter.ViewHolder> {
    private List<String> followingList;
    private List<organizationDetail> followingDataList;

    public followingAdapter(List<String> followingList) {
        this.followingList = followingList;
        this.followingDataList = new ArrayList<>();
        fetchFollowing();
    }

    private void fetchFollowing() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("human").child(uid).child("following");

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("FetchFollowingIDs", "Number of following: " + dataSnapshot.getChildrenCount());

                    followingList.clear();
                    followingDataList.clear(); // Clear the data list when fetching new followers

                    for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
                        String followingId = followerSnapshot.getKey();
                        Log.d("FetchFollowerIDs", "Follower ID: " + followingId);

                        followingList.add(followingId);

                        // Notify that an item is inserted
                        int position = followingList.size() - 1;
                        notifyItemInserted(position);

                        fetchFollowingDetails(followingId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchFollowerIDs", "Error fetching following IDs: " + error.getMessage());
            }
        });
    }


    private void fetchFollowingDetails(String followingId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("organization").child(followingId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    Log.d("FetchFollowingDetails", "Following details exist for ID: " + followingId);

                    organizationDetail following = userSnapshot.getValue(organizationDetail.class);
                    followingDataList.add(following);

                    // Notify that data set changed
                    notifyDataSetChanged();
                } else {
                    Log.d("FetchFollowingDetails", "Following details do not exist for ID: " + followingId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchFollowingDetails", "Error fetching following details: " + error.getMessage());
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, email, contact, mission;
        ImageView profilephoto;

        public ViewHolder(View itemView) {
            super(itemView);
            orgName = itemView.findViewById(R.id.orgName);
            email = itemView.findViewById(R.id.orgEmail);
            contact = itemView.findViewById(R.id.orgContact);
            mission = itemView.findViewById(R.id.orgMission);
            profilephoto = itemView.findViewById(R.id.profImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < followingDataList.size()) {
            organizationDetail following = followingDataList.get(position);

            holder.orgName.setText(following.getOrgName());
            holder.email.setText(following.getMail());
            holder.contact.setText(following.getContact());
            holder.mission.setText(following.getMission());

            String profilePhotoUrl = following.getProfileImageUrl();

            Glide.with(holder.itemView.getContext())
                    .load(profilePhotoUrl)
                    .placeholder(R.drawable.profileimg)
                    .error(R.drawable.profileimg)
                    .into(holder.profilephoto);
        }
    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }
}
