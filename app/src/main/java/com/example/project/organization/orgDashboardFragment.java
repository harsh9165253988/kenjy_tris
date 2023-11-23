package com.example.project.organization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.userAdapter;
import com.example.project.dataModels.userDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class orgDashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private userAdapter adapter;
    private List<userDataModel> dataList;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        adapter = new userAdapter(dataList);
        searchView = view.findViewById(R.id.search);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase and add it to dataList
        fetchDataFromFirebase();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // This method is called when the user submits the query (e.g., presses the Enter key).
            // Since we don't have specific actions for query submission in this case, we return false.
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // This method is called when the text in the SearchView changes.
            // It is used to filter the data based on the user's input and update the RecyclerView accordingly.
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText); // Call the filter method with the new input text
                return true;     // Return true to indicate that the input has been handled
            }


            private void filter(String newText) {
                List<userDataModel> filteredList = new ArrayList<>(); // Create a filtered list based on the user's input text
                for (userDataModel item : dataList) {
                    String skills = item.getSkl();       // Organization skills retrieve from userDetail
                    String name = item.getName();        //Organization  Name retrive from userDetail

                    // Check if the skills or name is not null and contains the input text (case-insensitive)
                    if ((skills != null && skills.toLowerCase().contains(newText.toLowerCase())) ||
                            (name != null && name.toLowerCase().contains(newText.toLowerCase()))) {
                        filteredList.add(item);     //user filter list
                    }
                }

                // filter list ke hisab se Adapter ko Update kiya
                adapter.filterList(filteredList);
            }
        });

        return view;
    }
    private void setupRecyclerView() {
        adapter = new userAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDataFromFirebase() {
        // Use Firebase Database or Firestore to fetch data
        // Update dataList with the fetched data
        // Notify the adapter about the data change
        // Example: Use Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/human");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userDataModel data = snapshot.getValue(userDataModel.class);
                    dataList.add(data);
                }


                if (adapter == null) {
                    // Initialize adapter and set up RecyclerView only once
                    setupRecyclerView();
                } else {
                    // Notify the adapter about the data change
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}