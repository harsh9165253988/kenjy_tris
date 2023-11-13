package com.example.project.LocationOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.userDataModel;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {
    private List<userDataModel> dataList;

    public userAdapter(List<userDataModel> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle, recPriority;

        public ViewHolder(View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.recTitle);
            recPriority = itemView.findViewById(R.id.recPriority);
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
        userDataModel currentItem = dataList.get(position);

        holder.recTitle.setText(currentItem.getName());
        holder.recPriority.setText(currentItem.getAge());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}