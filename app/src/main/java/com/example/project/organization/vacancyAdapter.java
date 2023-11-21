package com.example.project.organization;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class vacancyAdapter extends RecyclerView.Adapter<vacancyAdapter.ViewHolder> {
    private List<Vacancy> vacancyList;
    private Context context;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private boolean showApplyButton;
    // Set the showApplyButton value from the fragment
    public void setShowApplyButton(boolean showApplyButton) {
        this.showApplyButton = showApplyButton;
    }

    public vacancyAdapter(List<Vacancy> vacancyList, Context context) {
        this.vacancyList = vacancyList;
        this.context = context;
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
        holder.dustbinIcon.setOnClickListener(view -> showDeleteConfirmationDialog(vacancy));

        holder.bind(vacancy);
    }

    private void showDeleteConfirmationDialog(Vacancy vacancy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Vacancy");
        builder.setMessage("Are you sure you want to delete this vacancy?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Call the method to delete the vacancy
            deleteVacancy(vacancy);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing or handle cancellation
        });
        builder.show();
    }

    private void deleteVacancy(Vacancy vacancy) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference vacancyRef = FirebaseDatabase.getInstance().getReference("organization")
                    .child(userId)
                    .child("vacancy")
                    .child(vacancy.getId());

            vacancyRef.removeValue();
            vacancyList.remove(vacancy);

            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
        } else {
            // Handle the case where the user is not authenticated/logged in
            // You might want to show a message or take appropriate action
            Log.e("VacancyAdapter", "User not authenticated");
        }
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancyList = vacancies;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return vacancyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTextView;
        private TextView dateTimeTextView;
        private TextView skillsTextView;
        private ImageView dustbinIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            skillsTextView = itemView.findViewById(R.id.skillsTextView);
            dustbinIcon = itemView.findViewById(R.id.dustbinIcon);
        }

        public void bind(Vacancy vacancy) {
            locationTextView.setText("Location: " + vacancy.getLocation());
            dateTimeTextView.setText("Date & Time: " + vacancy.getDateTime());
            skillsTextView.setText("Preferred Skills: " + vacancy.getPreferredSkills());
        }
    }
}
