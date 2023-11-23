package com.example.project.adapter;

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

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private List<userDataModel> dataList;

    public userAdapter(List<userDataModel> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle, recPriority;
        ImageView profilephoto;

        public ViewHolder(View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.recTitle);
            recPriority = itemView.findViewById(R.id.recPriority);
            profilephoto = itemView.findViewById(R.id.profImage);
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

        holder.recTitle.setText(currentItem.getName());
        holder.recPriority.setText(currentItem.getAge());

        // Assuming the profile photo URL is stored in the 'profilePhotoUrl' field of your userDataModel
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