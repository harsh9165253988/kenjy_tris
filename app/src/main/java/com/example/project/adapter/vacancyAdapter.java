package com.example.project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.dataModels.Vacancy;
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
        private TextView orgNameTextView;
        private TextView contact;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            skillsTextView = itemView.findViewById(R.id.skills);
            dustbinIcon = itemView.findViewById(R.id.dustbinIcon);
            orgNameTextView=itemView.findViewById(R.id.orgNameTextView);
            contact=itemView.findViewById(R.id.contact);
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open dialer when contact TextView is clicked
                    String phoneNumber = contact.getText().toString().trim();
                    dialPhoneNumber(phoneNumber);
                }
            });
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




        public void bind(Vacancy vacancy) {
            locationTextView.setText("Location: " + vacancy.getLocation());
            dateTimeTextView.setText("Date : " + vacancy.getDateTime());
            skillsTextView.setText("Preferred Skills: " + vacancy.getPreferredSkills());
            orgNameTextView.setText("Organization: " + vacancy.getOrgname());
            contact.setText("contact: " + vacancy.getContact());

        }
    }
}
