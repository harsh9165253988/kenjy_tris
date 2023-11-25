package com.example.project.organization;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.LocationOwner.organizationDashboard;
import com.example.project.R;
import com.example.project.adapter.vacancyAdapter;
import com.example.project.dataModels.Vacancy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class orgManageFragment extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<Vacancy> vacancyList;
    private vacancyAdapter adapter;
    private ImageView backArrow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_manage, container, false);

        ImageView menuIcon = view.findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(this::showPopupMenu);
        recyclerView = view.findViewById(R.id.recyclerView);
        backArrow=view.findViewById(R.id.backArrow);
        vacancyList = new ArrayList<>();
        adapter = new vacancyAdapter(vacancyList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("organization");

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), orgProfileFragment.class);
                startActivity(i);
                getActivity().finish();
            }
        });



        // Fetch data from Firebase and update the RecyclerView
        fetchDataFromFirebase();

        return view;
    }


    private void showCreateVacancyPopup() {
        addVacancyPopup.showPopupWindow(requireContext(), requireView(), databaseReference, new addVacancyPopup.OnVacancyCreatedListener() {
            @Override
            public void onVacancyCreated(Vacancy vacancy) {
                // Handle the created vacancy, update UI or perform other actions
            }
        });
    }

    private void fetchDataFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("organization").child(uid).child("vacancy");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    vacancyList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Vacancy vacancy = snapshot.getValue(Vacancy.class);
                        if (vacancy != null) {
                            vacancy.setId(snapshot.getKey());
                            vacancyList.add(vacancy);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });
            //backArrow is a image button

            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(getActivity(), organizationDashboard.class);
                    startActivity(i);

                }
            });
        }
    }


    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.manage_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_create_vacancy) {
                showCreateVacancyPopup();
                return true;
            } else {
                // Add more conditions using else if if needed
                return false;
            }
        });

        popupMenu.show();
    }



    private void showDeleteVacancyPopup(Vacancy vacancy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            DatabaseReference vacancyRef = FirebaseDatabase.getInstance().getReference("organization")
                    .child(uid)
                    .child("vacancy")
                    .child(vacancy.getId());

            vacancyRef.removeValue();
            vacancyList.remove(vacancy);

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged();
        }
    }
}
