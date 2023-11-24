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

public class followersAdapter extends RecyclerView.Adapter<followersAdapter.ViewHolder> {
    private List<String> followersList;
    private List<userDataModel> followersDataList;

    public followersAdapter(List<String> followersList) {
        this.followersList = followersList;
        this.followersDataList = new ArrayList<>();
        fetchFollowers();
    }

    private void fetchFollowers() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference().child("organization").child(uid).child("followers");

        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("FetchFollowerIDs", "Number of followers: " + dataSnapshot.getChildrenCount());

                    followersList.clear();
                    followersDataList.clear(); // Clear the data list when fetching new followers

                    for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
                        String followerId = followerSnapshot.getKey();
                        Log.d("FetchFollowerIDs", "Follower ID: " + followerId);

                        followersList.add(followerId);

                        // Notify that an item is inserted
                        int position = followersList.size() - 1;
                        notifyItemInserted(position);

                        fetchFollowerDetails(followerId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchFollowerIDs", "Error fetching follower IDs: " + error.getMessage());
            }
        });
    }


    private void fetchFollowerDetails(String followerId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("human").child(followerId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    Log.d("FetchFollowerDetails", "Follower details exist for ID: " + followerId);

                    userDataModel follower = userSnapshot.getValue(userDataModel.class);
                    followersDataList.add(follower);

                    // Notify that data set changed
                    notifyDataSetChanged();
                } else {
                    Log.d("FetchFollowerDetails", "Follower details do not exist for ID: " + followerId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchFollowerDetails", "Error fetching follower details: " + error.getMessage());
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, email, contact, skills;
        ImageView profilephoto;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.userEmail);
            contact = itemView.findViewById(R.id.userContact);
            skills = itemView.findViewById(R.id.skills);
            profilephoto = itemView.findViewById(R.id.profImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < followersDataList.size()) {
            userDataModel follower = followersDataList.get(position);

            holder.userName.setText(follower.getName());
            holder.email.setText(follower.getEmail());
            holder.contact.setText(follower.getMob());
            holder.skills.setText(follower.getSkl());

            String profilePhotoUrl = follower.getProfileImageUrl();

            Glide.with(holder.itemView.getContext())
                    .load(profilePhotoUrl)
                    .placeholder(R.drawable.profileimg)
                    .error(R.drawable.profileimg)
                    .into(holder.profilephoto);
        }
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }
}
