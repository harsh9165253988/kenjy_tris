package com.example.project.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.dataModels.userDataModel;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private List<userDataModel> dataList;

    public userAdapter(List<userDataModel> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, name,email,contact,skills;
        ImageView profilephoto;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.userEmail);
            contact=itemView.findViewById(R.id.userContact);
            skills=itemView.findViewById(R.id.skills);
            profilephoto = itemView.findViewById(R.id.profImage);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open email when email TextView is clicked
                    String emailId = email.getText().toString().trim();
                    composeEmail(emailId);
                }
            });

            // Set click listener for contact TextView
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open dialer when contact TextView is clicked
                    String phoneNumber = contact.getText().toString().trim();
                    dialPhoneNumber(phoneNumber);
                }
            });
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

    public void filterList(List<userDataModel> filteredList) {
        dataList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userDataModel currentItem = dataList.get(position);

        holder.userName.setText(currentItem.getName());
        holder.email.setText(currentItem.getEmail());
        holder.contact.setText(currentItem.getMob());
        holder.skills.setText(currentItem.getSkl());


        String profilePhotoUrl = currentItem.getProfileImageUrl();

        // Use Glide to load the profile photo into the ImageView
        Glide.with(holder.itemView.getContext())
                .load(profilePhotoUrl)
                .placeholder(R.drawable.profileimg) // You can set a placeholder image
                .error(R.drawable.profileimg) // You can set an error image
                .into(holder.profilephoto);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}