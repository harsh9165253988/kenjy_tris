package com.example.project.LocationOwner;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.project.R;
import com.example.project.organization.Vacancy;
import com.example.project.organization.vacancyAdapter;
import com.example.project.userDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ... (imports)

public class userManageFragment extends Fragment {
    private DatabaseReference usersReference;
    private DatabaseReference vacanciesReference;
    private RecyclerView recyclerView;
    private vacancyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manage, container, false);

        usersReference = FirebaseDatabase.getInstance().getReference("human");
        vacanciesReference = FirebaseDatabase.getInstance().getReference("organization");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new vacancyAdapter(new ArrayList<>(),requireContext());
        recyclerView.setAdapter(adapter);

        // Fetch current user skills
        fetchCurrentUserSkills();

        return view;
    }

    private void fetchCurrentUserSkills() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            usersReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userDataModel user = dataSnapshot.getValue(userDataModel.class);
                        if (user != null) {
                            Object sklObject = user.getSkl();

                            if (sklObject instanceof String) {
                                List<String> userSkl = Arrays.asList(((String) sklObject).split(","));
                                fetchVacanciesBasedOnSkills(userSkl);
                            } else if (sklObject instanceof List<?>) {
                                fetchVacanciesBasedOnSkills((List<String>) sklObject);
                            } else {
                                // Handle other cases if needed
                            }
                        } else {
                            Log.e("UserManageFragment", "userDataModel is null");
                        }
                    } else {
                        Log.e("UserManageFragment", "DataSnapshot does not exist for user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UserManageFragment", "Database error: " + databaseError.getMessage());
                    // Handle error
                }
            });
        } else {
            Log.e("UserManageFragment", "Current user is null");
        }
    }
    private void fetchVacanciesBasedOnSkills(List<String> userSkl) {
        vacanciesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Vacancy> filteredVacancies = new ArrayList<>();

                for (DataSnapshot organizationSnapshot : dataSnapshot.getChildren()) {
                    if (organizationSnapshot.child("vacancy").exists()) {
                        for (DataSnapshot vacancySnapshot : organizationSnapshot.child("vacancy").getChildren()) {
                            Vacancy vacancy = vacancySnapshot.getValue(Vacancy.class);
                            // Check if vacancy and preferredSkills are not null
                            if (vacancy != null && vacancy.hasRequiredSkills(userSkl) && vacancy.getPreferredSkills() != null) {
                                filteredVacancies.add(vacancy);
                            }
                        }
                    }
                }

                adapter.setVacancies(filteredVacancies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserManageFragment", "Database error: " + databaseError.getMessage());
                // Handle error
            }
        });
    }



}