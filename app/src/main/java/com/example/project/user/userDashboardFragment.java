package com.example.project.user;

import static java.lang.Character.toLowerCase;

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
import com.example.project.adapter.organizationAdapter;
import com.example.project.dataModels.organizationDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userDashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private organizationAdapter adapter;
    private List<organizationDetail> dataList;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);
        dataList = new ArrayList<>();
        adapter = new organizationAdapter(dataList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase and add it to dataList
        fetchDataFromFirebase();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }

            private void filter(String newText) {
                List<organizationDetail> filteredList = new ArrayList<>(); // Create a filtered list based on the user's input text
                for (organizationDetail item : dataList) {

                    String location = item.getLocation(); // Organization location retrieve from OrganizationDetail
                    String name = item.getOrgName();     //Organization  Name retrive from OrganizationDetail

                    // Check if the location or name is not null and contains the input text (case-insensitive)
                    if ((location != null && location.toLowerCase().contains(newText.toLowerCase())) ||
                            (name != null && name.toLowerCase().contains(newText.toLowerCase()))) {

                        filteredList.add(item);     //organization filter list
                    }
                }

                // filter list ke hisab se Adepter ko Update kiya
                adapter.filterList(filteredList);
            }
        });
        return view;
    }

    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/organization");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    organizationDetail data = snapshot.getValue(organizationDetail.class);
                    if (data != null) {
                        dataList.add(data);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}