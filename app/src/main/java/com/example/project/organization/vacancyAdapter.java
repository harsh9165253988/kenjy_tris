package com.example.project.organization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class vacancyAdapter extends RecyclerView.Adapter<vacancyAdapter.ViewHolder> {
    private List<Vacancy> vacancyList;

    public vacancyAdapter(List<Vacancy> vacancyList) {
        this.vacancyList = vacancyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacancy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vacancy vacancy = vacancyList.get(position);
        holder.bind(vacancy);
    }

    @Override
    public int getItemCount() {
        return vacancyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTextView;
        private TextView dateTimeTextView;
        private TextView skillsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            skillsTextView = itemView.findViewById(R.id.skillsTextView);
        }

        public void bind(Vacancy vacancy) {
            locationTextView.setText("Location: " + vacancy.getLocation());
            dateTimeTextView.setText("Date & Time: " + vacancy.getDateTime());
            skillsTextView.setText("Preferred Skills: " + vacancy.getPreferredSkills());
        }
    }
}