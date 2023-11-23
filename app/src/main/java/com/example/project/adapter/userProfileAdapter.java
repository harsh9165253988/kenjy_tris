package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R; // Replace with your actual package name
import com.example.project.dataModels.userDataModel;

import java.util.List;

public class userProfileAdapter extends RecyclerView.Adapter<userProfileAdapter.UserViewHolder> {
    private Context context;
    private List<userDataModel> userList;

    public userProfileAdapter(Context context, List<userDataModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_user_profile, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        userDataModel user = userList.get(position);

        // Customize the binding based on your userDataModel attributes
        holder.fullNameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
        holder.ageTextView.setText(user.getAge());
        holder.genderTextView.setText(user.getGender());
        holder.phoneNumberTextView.setText(user.getMob());
        holder.skillsTextView.setText(user.getSkl());

        // Add bindings for other attributes as needed
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView, emailTextView, ageTextView, genderTextView, phoneNumberTextView, skillsTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.name); // Replace with your actual TextView IDs
            emailTextView = itemView.findViewById(R.id.email);
            ageTextView = itemView.findViewById(R.id.age);
            genderTextView = itemView.findViewById(R.id.gender);
            phoneNumberTextView = itemView.findViewById(R.id.number);
            skillsTextView = itemView.findViewById(R.id.skill);

            // Add other TextViews as needed
        }
    }
}