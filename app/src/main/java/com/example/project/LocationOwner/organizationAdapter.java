package com.example.project.LocationOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.organization.organizationDetail;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, orgLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            orgName = itemView.findViewById(R.id.orgName);
            orgLocation = itemView.findViewById(R.id.orgLocation);
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}