package com.example.project.LocationOwner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.organization.donation;
import com.example.project.organization.organizationDetail;

import java.util.List;

public class organizationAdapter extends RecyclerView.Adapter<organizationAdapter.ViewHolder> {
    private static List<organizationDetail> data;

    public organizationAdapter(List<organizationDetail> dataList) {
        this.data = dataList;
    }

    public void filterList(List<organizationDetail> filteredList) {
        data = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, orgLocation;
        Button donateButton;

        public ViewHolder(View itemView) {
            super(itemView);
            orgName = itemView.findViewById(R.id.orgName);
            orgLocation = itemView.findViewById(R.id.orgLocation);
            donateButton = itemView.findViewById(R.id.donate);

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
