package com.example.project.organization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;
import com.example.project.organization.vacancyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// app/src/main/java/com/example/yourpackage/YourFragment.java

// orgManageFragment.java

// ... (imports)

public class orgManageFragment extends Fragment {
    private DatabaseReference databaseReference;

    private Button btnCreateVacancy;
    private Button btnDeleteVacancy;
    private RecyclerView recyclerView;
    private List<Vacancy> vacancyList;
    private vacancyAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_org_manage, container, false);

        ImageView menuIcon = view.findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(this::showPopupMenu);
        recyclerView = view.findViewById(R.id.recyclerView);

        vacancyList = new ArrayList<>();
        adapter = new vacancyAdapter(vacancyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("organization");
        ArrayList<Integer> jobList =new ArrayList<>();
        String[] jobarray = getResources().getStringArray(R.array.job_options);




        // Handle btnDeleteVacancy click if needed

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
        String uid = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("organization").child(uid).child("vacancy");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vacancyList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vacancy vacancy = snapshot.getValue(Vacancy.class);
                    vacancyList.add(vacancy);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
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

        // Inflate the custom layout for the spinner
        View spinnerLayout = LayoutInflater.from(requireContext()).inflate(R.layout.activity_add_vacancy_popup, null);

        // Find the Spinner in the custom layout
        Spinner spinnerLocation = spinnerLayout.findViewById(R.id.spinnerlocation);

        // Populate the spinner with your data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        // Add the custom layout to the PopupMenu
        popupMenu.setForceShowIcon(true);
        popupMenu.show();
        popupMenu.getMenu().getItem(0).setActionView(spinnerLayout);

        return;
    }

}